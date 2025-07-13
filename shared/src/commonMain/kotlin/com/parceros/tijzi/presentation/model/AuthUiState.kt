// shared/src/commonMain/kotlin/com/parceros/tijzi/presentation/model/AuthUiState.kt
package com.parceros.tijzi.presentation.model

import com.parceros.tijzi.domain.model.Country

/**
 * Estado de UI compartido entre Android e iOS
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentStep: AuthStep = AuthStep.LOGIN,

    // Datos del usuario
    val selectedCountry: Country? = null,
    val phoneNumber: String = "",
    val otpCode: String = "",
    val selectedChannel: OtpChannel? = null,

    // Estados específicos
    val countries: List<Country> = emptyList(),
    val isPhoneValid: Boolean = false,
    val isOtpValid: Boolean = false,
    val canRequestNewOtp: Boolean = true,
    val countdown: Int = 0,

    // Geolocalización
    val detectedCountry: Country? = null,
    val isLocationDetected: Boolean = false,

    // Estados de validación
    val phoneError: String? = null,
    val otpError: String? = null,

    // Estados de UI específicos
    val isCountryPickerOpen: Boolean = false,
    val isKeyboardVisible: Boolean = false
)

/**
 * Pasos del flujo de autenticación
 */
enum class AuthStep {
    LOGIN,           // Pantalla 1 - Login principal
    OTP_VERIFICATION, // Pantalla 2 - Verificación OTP
    RESEND_CODE,     // Pantalla 3 - Reenvío de código
    WELCOME          // Pantalla 4 - Bienvenida
}

/**
 * Canales de envío de OTP
 */
enum class OtpChannel(val displayName: String, val iconName: String) {
    WHATSAPP("WhatsApp", "whatsapp"),
    SMS("SMS", "sms"),
    TELEGRAM("Telegram", "telegram")
}

/**
 * Eventos del usuario - Compartidos entre plataformas
 */
sealed class AuthEvent {
    // Pantalla 1 - Login
    data class CountrySelected(val country: Country) : AuthEvent()
    data class PhoneNumberChanged(val phoneNumber: String) : AuthEvent()
    data class ChannelSelected(val channel: OtpChannel) : AuthEvent()
    object RequestOtp : AuthEvent()
    object ToggleCountryPicker : AuthEvent()

    // Pantalla 2 - OTP Verification
    data class OtpCodeChanged(val otpCode: String) : AuthEvent()
    object VerifyOtp : AuthEvent()
    object GoToResendCode : AuthEvent()

    // Pantalla 3 - Resend Code
    object ResendCodeWithSameChannel : AuthEvent()
    data class ResendCodeWithNewChannel(val channel: OtpChannel) : AuthEvent()

    // Generales
    object ClearError : AuthEvent()
    object BackToLogin : AuthEvent()
    object DetectLocation : AuthEvent()
    object NavigateBack : AuthEvent()

    // UI específicos
    object KeyboardShown : AuthEvent()
    object KeyboardHidden : AuthEvent()
}

/**
 * Eventos de navegación - Para que cada plataforma maneje su navegación
 */
sealed class AuthNavigationEvent {
    object NavigateToLogin : AuthNavigationEvent()
    object NavigateToOtpVerification : AuthNavigationEvent()
    object NavigateToResendCode : AuthNavigationEvent()
    object NavigateToWelcome : AuthNavigationEvent()
    object NavigateBack : AuthNavigationEvent()
    object ShowError : AuthNavigationEvent()
    data class ShowToast(val message: String) : AuthNavigationEvent()
}