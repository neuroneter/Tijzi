// shared/src/commonMain/kotlin/com/parceros/tijzi/domain/usecase/RequestOtpUseCase.kt
package com.parceros.tijzi.domain.usecase

import com.parceros.tijzi.data.repository.AuthRepository
import com.parceros.tijzi.data.repository.CountryRepository
import com.parceros.tijzi.data.repository.RateLimitRepository
import com.parceros.tijzi.domain.model.Country
import com.parceros.tijzi.util.Result
import com.parceros.tijzi.util.ValidationUtils
import com.parceros.tijzi.util.ValidationResult
import com.parceros.tijzi.util.combine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RequestOtpUseCase(
    private val authRepository: AuthRepository,
    private val rateLimitRepository: RateLimitRepository,
    private val countryRepository: CountryRepository
) {
    operator fun invoke(countryCode: String, phoneNumber: String): Flow<Result<Unit>> = flow {
        // 🔥 PASO 1: Obtener lista de países para validación
        var countries: List<Country> = emptyList()
        countryRepository.getCountries().collect { countriesResult ->
            when (countriesResult) {
                is Result.Success -> {
                    countries = countriesResult.data
                }
                is Result.Failure -> {
                    // Si no podemos obtener países, usaremos validación genérica
                    countries = emptyList()
                }
            }
        }

        // 🔥 PASO 2: Validar entrada
        val validationResult = validateInput(countryCode, phoneNumber, countries)
        if (validationResult.isInvalid) {
            emit(Result.Failure(Exception(validationResult .firstError)))
            return@flow
        }

        // 🔥 PASO 3: Limpiar y preparar datos
        val cleanCountryCode = countryCode.trim()
        val cleanPhoneNumber = ValidationUtils.cleanPhoneNumber(phoneNumber)
        val identifier = "$cleanCountryCode-$cleanPhoneNumber"

        // 🔥 PASO 4: Verificar rate limiting
        if (!rateLimitRepository.canRequestOtp(identifier)) {
            emit(Result.Failure(Exception("Has alcanzado el límite de códigos por WhatsApp. Intenta de nuevo en una hora.")))
            return@flow
        }

        // 🔥 PASO 5: Proceder con la solicitud
        authRepository.requestOtp(cleanCountryCode, cleanPhoneNumber).collect { result ->
            if (result is Result.Success) {
                // Registrar la solicitud exitosa
                rateLimitRepository.recordOtpRequest(identifier)
            }
            emit(result)
        }
    }

    private fun validateInput(
        countryCode: String,
        phoneNumber: String,
        countries: List<Country>
    ): ValidationResult {
        val validations = listOf(
            validateCountryCode(countryCode, countries),
            validatePhoneNumber(phoneNumber, countryCode, countries)
        )

        return validations.combine()
    }

    private fun validateCountryCode(countryCode: String, countries: List<Country>): ValidationResult {
        return when {
            countryCode.isBlank() ->
                ValidationResult.Invalid(ValidationUtils.ErrorMessages.EMPTY_COUNTRY_CODE)

            !ValidationUtils.isValidCountryCode(countryCode) ->
                ValidationResult.Invalid(ValidationUtils.ErrorMessages.INVALID_COUNTRY_CODE)

            countries.isNotEmpty() && ValidationUtils.findCountryByCode(countryCode, countries) == null ->
                ValidationResult.Invalid("El código de país $countryCode no está soportado actualmente")

            else -> ValidationResult.Valid
        }
    }

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
}