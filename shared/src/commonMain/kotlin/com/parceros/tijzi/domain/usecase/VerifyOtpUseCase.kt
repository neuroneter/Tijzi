// shared/src/commonMain/kotlin/com/parceros/tijzi/domain/usecase/VerifyOtpUseCase.kt
package com.parceros.tijzi.domain.usecase

import com.parceros.tijzi.data.repository.AuthRepository
import com.parceros.tijzi.data.repository.CountryRepository
import com.parceros.tijzi.data.repository.SessionRepository
import com.parceros.tijzi.domain.model.Country
import com.parceros.tijzi.domain.model.UserSession
import com.parceros.tijzi.util.Result
import com.parceros.tijzi.util.ValidationUtils
import com.parceros.tijzi.util.ValidationResult
import com.parceros.tijzi.util.combine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class VerifyOtpUseCase(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    private val countryRepository: CountryRepository
) {
    operator fun invoke(
        countryCode: String,
        phoneNumber: String,
        otp: String
    ): Flow<Result<UserSession>> = flow {
        // 🔥 PASO 1: Obtener lista de países para validación de teléfono
        var countries: List<Country> = emptyList()
        countryRepository.getCountries().collect { countriesResult ->
            when (countriesResult) {
                is Result.Success -> {
                    countries = countriesResult.data
                }
                is Result.Failure -> {
                    // Si no podemos obtener países, usar validación genérica
                    countries = emptyList()
                }
            }
        }

        // 🔥 PASO 2: Validar entrada (enfocado en datos del usuario)
        val validationResult = validateInput(countryCode, phoneNumber, otp, countries)
        if (validationResult.isInvalid) {
            emit(Result.Failure(Exception(validationResult.firstError)))
            return@flow
        }

        // 🔥 PASO 3: Limpiar y preparar datos
        val cleanCountryCode = countryCode.trim()
        val cleanPhoneNumber = ValidationUtils.cleanPhoneNumber(phoneNumber)
        val cleanOtp = otp.trim()

        // 🔥 PASO 4: Proceder con la verificación
        authRepository.verifyOtp(cleanCountryCode, cleanPhoneNumber, cleanOtp)
            .flatMapConcat { verificationResult ->
                when (verificationResult) {
                    is Result.Success -> {
                        val userSession = verificationResult.data
                        sessionRepository.saveSession(userSession)
                            .map { saveResult ->
                                when (saveResult) {
                                    is Result.Success -> Result.Success(userSession)
                                    is Result.Failure -> Result.Failure(saveResult.exception)
                                }
                            }
                    }
                    is Result.Failure -> {
                        flow { emit(Result.Failure(verificationResult.exception)) }
                    }
                }
            }
            .collect { result ->
                emit(result)
            }
    }

    private fun validateInput(
        countryCode: String,
        phoneNumber: String,
        otp: String,
        countries: List<Country>
    ): ValidationResult {
        val validations = listOf(
            validateCountryCodeFormat(countryCode), // Solo formato básico
            validatePhoneNumber(phoneNumber, countryCode, countries), // Validación completa
            validateOtp(otp) // Validación completa del OTP
        )

        return validations.combine()
    }

    /**
     * Validación básica del código de país (viene de UI, pero verificamos formato)
     */
    private fun validateCountryCodeFormat(countryCode: String): ValidationResult {
        return when {
            countryCode.isBlank() ->
                ValidationResult.Invalid("Error interno: código de país no seleccionado")

            !ValidationUtils.isValidCountryCode(countryCode) ->
                ValidationResult.Invalid("Error interno: formato de código de país inválido")

            else -> ValidationResult.Valid
        }
    }

    /**
     * Validación completa del número de teléfono (usuario lo digita)
     */
    private fun validatePhoneNumber(
        phoneNumber: String,
        countryCode: String,
        countries: List<Country>
    ): ValidationResult {
        return when {
            phoneNumber.isBlank() ->
                ValidationResult.Invalid(ValidationUtils.ErrorMessages.EMPTY_PHONE_NUMBER)

            else -> {
                val phoneValidation = ValidationUtils.validatePhoneNumberWithCountry(
                    phoneNumber, countryCode, countries
                )
                if (phoneValidation.isValid) {
                    ValidationResult.Valid
                } else {
                    ValidationResult.Invalid((phoneValidation as ValidationUtils.PhoneValidationResult.Invalid).message)
                }
            }
        }
    }

    /**
     * Validación completa del OTP (usuario lo digita)
     */
    private fun validateOtp(otp: String): ValidationResult {
        return when {
            otp.isBlank() ->
                ValidationResult.Invalid(ValidationUtils.ErrorMessages.EMPTY_OTP)

            otp.length < 6 ->
                ValidationResult.Invalid("El código debe tener al menos 6 dígitos")

            otp.length > 6 ->
                ValidationResult.Invalid("El código debe tener exactamente 6 dígitos")

            otp.any { !it.isDigit() } ->
                ValidationResult.Invalid(ValidationUtils.ErrorMessages.OTP_CONTAINS_LETTERS)

            !ValidationUtils.isValidOtp(otp) ->
                ValidationResult.Invalid(ValidationUtils.ErrorMessages.INVALID_OTP)

            else -> ValidationResult.Valid
        }
    }
}