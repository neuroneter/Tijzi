// shared/src/commonMain/kotlin/com/parceros/tijzi/presentation/viewmodel/AuthViewModel.kt
package com.parceros.tijzi.presentation.viewmodel

import com.parceros.tijzi.data.repository.CountryRepository
import com.parceros.tijzi.data.repository.SessionRepository
import com.parceros.tijzi.domain.usecase.RequestOtpUseCase
import com.parceros.tijzi.domain.usecase.VerifyOtpUseCase
import com.parceros.tijzi.presentation.model.AuthEvent
import com.parceros.tijzi.presentation.model.AuthNavigationEvent
import com.parceros.tijzi.presentation.model.AuthStep
import com.parceros.tijzi.presentation.model.AuthUiState
import com.parceros.tijzi.presentation.model.OtpChannel
import com.parceros.tijzi.util.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel compartido entre Android e iOS
 * Maneja toda la lógica de estado y navegación
 */
class AuthViewModel(
    private val requestOtpUseCase: RequestOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val countryRepository: CountryRepository,
    private val sessionRepository: SessionRepository,
    private val scope: CoroutineScope // Será inyectado desde cada plataforma
) {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<AuthNavigationEvent>()
    val navigationEvents: SharedFlow<AuthNavigationEvent> = _navigationEvents.asSharedFlow()

    private var countdownJob: Job? = null

    init {
        loadCountries()
        checkExistingSession()
    }

    /**
     * Punto de entrada principal para todos los eventos
     */
    fun onEvent(event: AuthEvent) {
        when (event) {
            // Pantalla 1 - Login
            is AuthEvent.CountrySelected -> selectCountry(event.country)
            is AuthEvent.PhoneNumberChanged -> updatePhoneNumber(event.phoneNumber)
            is AuthEvent.ChannelSelected -> selectChannel(event.channel)
            AuthEvent.RequestOtp -> requestOtp()
            AuthEvent.ToggleCountryPicker -> toggleCountryPicker()

            // Pantalla 2 - OTP Verification
            is AuthEvent.OtpCodeChanged -> updateOtpCode(event.otpCode)
            AuthEvent.VerifyOtp -> verifyOtp()
            AuthEvent.GoToResendCode -> goToResendCode()

            // Pantalla 3 - Resend Code
            AuthEvent.ResendCodeWithSameChannel -> resendCodeWithSameChannel()
            is AuthEvent.ResendCodeWithNewChannel -> resendCodeWithNewChannel(event.channel)

            // Generales
            AuthEvent.ClearError -> clearError()
            AuthEvent.BackToLogin -> backToLogin()
            AuthEvent.DetectLocation -> detectLocation()
            AuthEvent.NavigateBack -> navigateBack()

            // UI específicos
            AuthEvent.KeyboardShown -> _uiState.update { it.copy(isKeyboardVisible = true) }
            AuthEvent.KeyboardHidden -> _uiState.update { it.copy(isKeyboardVisible = false) }
        }
    }

    // === MÉTODOS PRIVADOS ===

    private fun loadCountries() {
        scope.launch {
            _uiState.update { it.copy(isLoading = true) }

            countryRepository.getCountries().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                countries = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Result.Failure -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                error = "Error cargando países: ${result.exception.message}"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun checkExistingSession() {
        scope.launch {
            sessionRepository.isLoggedIn().collect { isLoggedIn ->
                if (isLoggedIn) {
                    _uiState.update { it.copy(currentStep = AuthStep.WELCOME) }
                    _navigationEvents.emit(AuthNavigationEvent.NavigateToWelcome)
                }
            }
        }
    }

    private fun selectCountry(country: com.parceros.tijzi.domain.model.Country) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCountry = country,
                phoneError = null,
                isCountryPickerOpen = false
            )
        }
        validatePhoneNumber()
    }

    private fun updatePhoneNumber(phoneNumber: String) {
        _uiState.update { currentState ->
            currentState.copy(
                phoneNumber = phoneNumber,
                phoneError = null
            )
        }
        validatePhoneNumber()
    }

    private fun selectChannel(channel: OtpChannel) {
        _uiState.update { it.copy(selectedChannel = channel) }
    }

    private fun toggleCountryPicker() {
        _uiState.update { currentState ->
            currentState.copy(isCountryPickerOpen = !currentState.isCountryPickerOpen)
        }
    }

    private fun validatePhoneNumber() {
        val currentState = _uiState.value
        val country = currentState.selectedCountry
        val phoneNumber = currentState.phoneNumber

        if (country != null && phoneNumber.isNotBlank()) {
            // Aquí usarías tu ValidationUtils real
            val isValid = phoneNumber.length >= 7 // Validación simplificada por ahora
            _uiState.update { it.copy(isPhoneValid = isValid) }
        } else {
            _uiState.update { it.copy(isPhoneValid = false) }
        }
    }

    private fun requestOtp() {
        val currentState = _uiState.value
        val country = currentState.selectedCountry
        val phoneNumber = currentState.phoneNumber
        val channel = currentState.selectedChannel

        if (country == null || phoneNumber.isBlank() || channel == null) {
            _uiState.update { it.copy(error = "Por favor completa todos los campos") }
            return
        }

        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            requestOtpUseCase(country.phoneCode, phoneNumber).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                currentStep = AuthStep.OTP_VERIFICATION,
                                canRequestNewOtp = false
                            )
                        }
                        startCountdown()
                        _navigationEvents.emit(AuthNavigationEvent.NavigateToOtpVerification)
                    }
                    is Result.Failure -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                error = result.exception.message ?: "Error enviando código"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateOtpCode(otpCode: String) {
        // Limitar a 6 dígitos y solo números
        val cleanOtp = otpCode.filter { it.isDigit() }.take(6)

        _uiState.update { currentState ->
            currentState.copy(
                otpCode = cleanOtp,
                otpError = null,
                isOtpValid = cleanOtp.length == 6
            )
        }

        // Auto-verificar cuando se completen 6 dígitos
        if (cleanOtp.length == 6) {
            verifyOtp()
        }
    }

    private fun verifyOtp() {
        val currentState = _uiState.value
        val country = currentState.selectedCountry
        val phoneNumber = currentState.phoneNumber
        val otpCode = currentState.otpCode

        if (country == null || phoneNumber.isBlank() || otpCode.length != 6) {
            _uiState.update { it.copy(otpError = "Código incompleto") }
            return
        }

        scope.launch {
            _uiState.update { it.copy(isLoading = true, otpError = null) }

            verifyOtpUseCase(country.phoneCode, phoneNumber, otpCode).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                currentStep = AuthStep.WELCOME
                            )
                        }
                        _navigationEvents.emit(AuthNavigationEvent.NavigateToWelcome)
                    }
                    is Result.Failure -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                otpError = result.exception.message ?: "Código incorrecto"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun goToResendCode() {
        _uiState.update { it.copy(currentStep = AuthStep.RESEND_CODE) }
        scope.launch {
            _navigationEvents.emit(AuthNavigationEvent.NavigateToResendCode)
        }
    }

    private fun resendCodeWithSameChannel() {
        requestOtp()
    }

    private fun resendCodeWithNewChannel(channel: OtpChannel) {
        _uiState.update { it.copy(selectedChannel = channel) }
        requestOtp()
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = scope.launch {
            for (seconds in 60 downTo 0) {
                _uiState.update { it.copy(countdown = seconds) }
                delay(1000)
            }
            _uiState.update { it.copy(canRequestNewOtp = true, countdown = 0) }
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null, phoneError = null, otpError = null) }
    }

    private fun backToLogin() {
        countdownJob?.cancel()
        _uiState.update { currentState ->
            currentState.copy(
                currentStep = AuthStep.LOGIN,
                otpCode = "",
                otpError = null,
                countdown = 0,
                canRequestNewOtp = true
            )
        }
        scope.launch {
            _navigationEvents.emit(AuthNavigationEvent.NavigateToLogin)
        }
    }

    private fun detectLocation() {
        // TODO: Implementar geolocalización específica por plataforma
        // Por ahora, simular detección de Colombia
        scope.launch {
            delay(1000) // Simular tiempo de detección
            val colombia = _uiState.value.countries.find { it.phoneCode == "+57" }
            if (colombia != null) {
                _uiState.update { currentState ->
                    currentState.copy(
                        detectedCountry = colombia,
                        selectedCountry = colombia,
                        isLocationDetected = true
                    )
                }
            }
        }
    }

    private fun navigateBack() {
        val currentStep = _uiState.value.currentStep
        when (currentStep) {
            AuthStep.OTP_VERIFICATION -> backToLogin()
            AuthStep.RESEND_CODE -> {
                _uiState.update { it.copy(currentStep = AuthStep.OTP_VERIFICATION) }
                scope.launch {
                    _navigationEvents.emit(AuthNavigationEvent.NavigateToOtpVerification)
                }
            }
            AuthStep.WELCOME -> backToLogin()
            AuthStep.LOGIN -> {
                // No hacer nada, ya está en login
            }
        }
    }

    /**
     * Método para limpiar recursos - llamar desde cada plataforma
     */
    fun onCleared() {
        countdownJob?.cancel()
    }
}