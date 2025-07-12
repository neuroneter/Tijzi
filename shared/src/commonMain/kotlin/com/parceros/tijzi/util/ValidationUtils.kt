// shared/src/commonMain/kotlin/com/parceros/tijzi/util/ValidationUtils.kt
package com.parceros.tijzi.util

import com.parceros.tijzi.domain.model.Country

object ValidationUtils {

    /**
     * Valida que el código de país tenga el formato correcto para WhatsApp
     * Ejemplos válidos: "+57", "+1", "+34"
     */
    fun isValidCountryCode(countryCode: String): Boolean {
        return countryCode.matches(Regex("^\\+[1-9]\\d{0,4}$"))
    }

    /**
     * Valida número de teléfono usando las reglas específicas del país
     * Si no hay reglas específicas, usa validación genérica
     */
    fun isValidPhoneNumber(phoneNumber: String, country: Country?): Boolean {
        val cleanNumber = phoneNumber.replace("[^0-9]".toRegex(), "")

        return when {
            // Si tenemos país y patrón personalizado, usarlo
            country != null && !country.customRegexPattern.isNullOrBlank() -> {
                cleanNumber.matches(Regex(country.customRegexPattern))
            }

            // Si tenemos país y longitud específica, validar longitud exacta
            country != null && !country.numberLength.isNullOrBlank() -> {
                try {
                    val expectedLength = country.numberLength.toInt()
                    cleanNumber.length == expectedLength && cleanNumber.matches(Regex("^[0-9]+$"))
                } catch (e: NumberFormatException) {
                    // Si numberLength no es un número válido, usar validación genérica
                    isValidPhoneNumberGeneric(cleanNumber)
                }
            }

            // Si no hay país o reglas específicas, usar validación genérica
            else -> isValidPhoneNumberGeneric(cleanNumber)
        }
    }

    /**
     * Validación genérica para números de teléfono
     * - Solo dígitos
     * - Entre 7 y 15 dígitos (estándar internacional)
     */
    private fun isValidPhoneNumberGeneric(cleanNumber: String): Boolean {
        return cleanNumber.matches(Regex("^[0-9]{7,15}$"))
    }

    /**
     * Valida que el código OTP tenga exactamente 6 dígitos
     */
    fun isValidOtp(otp: String): Boolean {
        return otp.matches(Regex("^[0-9]{6}$"))
    }

    /**
     * Limpia el número de teléfono removiendo espacios, guiones, etc.
     */
    fun cleanPhoneNumber(phoneNumber: String): String {
        return phoneNumber.replace("[^0-9]".toRegex(), "")
    }

    /**
     * Busca el país por código de país en la lista
     */
    fun findCountryByCode(countryCode: String, countries: List<Country>): Country? {
        return countries.find { it.phoneCode == countryCode }
    }

    /**
     * Valida número de teléfono con información específica del país
     */
    fun validatePhoneNumberWithCountry(
        phoneNumber: String,
        countryCode: String,
        countries: List<Country>
    ): PhoneValidationResult {
        val cleanNumber = cleanPhoneNumber(phoneNumber)
        val country = findCountryByCode(countryCode, countries)

        return when {
            cleanNumber.isEmpty() ->
                PhoneValidationResult.Invalid(ErrorMessages.EMPTY_PHONE_NUMBER)

            country == null -> {
                // País no encontrado, usar validación genérica
                if (isValidPhoneNumberGeneric(cleanNumber)) {
                    PhoneValidationResult.Valid
                } else {
                    PhoneValidationResult.Invalid(ErrorMessages.INVALID_PHONE_NUMBER)
                }
            }

            !country.customRegexPattern.isNullOrBlank() -> {
                // Usar patrón personalizado del país
                if (cleanNumber.matches(Regex(country.customRegexPattern))) {
                    PhoneValidationResult.Valid
                } else {
                    PhoneValidationResult.Invalid(
                        "El número no tiene el formato válido para ${country.nameEs}. " +
                                "Ejemplo: número colombiano debe iniciar con 3 y tener 10 dígitos."
                    )
                }
            }

            !country.numberLength.isNullOrBlank() -> {
                // Usar longitud específica del país
                try {
                    val expectedLength = country.numberLength.toInt()
                    when {
                        cleanNumber.length < expectedLength ->
                            PhoneValidationResult.Invalid(
                                "El número para ${country.nameEs} debe tener $expectedLength dígitos. " +
                                        "Ingresaste ${cleanNumber.length} dígitos."
                            )
                        cleanNumber.length > expectedLength ->
                            PhoneValidationResult.Invalid(
                                "El número para ${country.nameEs} debe tener $expectedLength dígitos. " +
                                        "Ingresaste ${cleanNumber.length} dígitos."
                            )
                        else -> PhoneValidationResult.Valid
                    }
                } catch (e: NumberFormatException) {
                    // numberLength no es válido, usar validación genérica
                    if (isValidPhoneNumberGeneric(cleanNumber)) {
                        PhoneValidationResult.Valid
                    } else {
                        PhoneValidationResult.Invalid(ErrorMessages.INVALID_PHONE_NUMBER)
                    }
                }
            }

            else -> {
                // No hay reglas específicas, usar validación genérica
                if (isValidPhoneNumberGeneric(cleanNumber)) {
                    PhoneValidationResult.Valid
                } else {
                    PhoneValidationResult.Invalid(ErrorMessages.INVALID_PHONE_NUMBER)
                }
            }
        }
    }

    /**
     * Resultado específico para validación de números de teléfono
     */
    sealed class PhoneValidationResult {
        object Valid : PhoneValidationResult()
        data class Invalid(val message: String) : PhoneValidationResult()

        val isValid: Boolean get() = this is Valid
        val isInvalid: Boolean get() = this is Invalid
    }

    /**
     * Genera mensajes de error específicos y claros
     */
    object ErrorMessages {
        const val EMPTY_COUNTRY_CODE = "Selecciona un código de país válido"
        const val INVALID_COUNTRY_CODE = "El código de país debe tener formato +XX (ej: +57)"
        const val UNSUPPORTED_COUNTRY_CODE = "Este código de país no está soportado actualmente"

        const val EMPTY_PHONE_NUMBER = "Ingresa tu número de teléfono"
        const val INVALID_PHONE_NUMBER = "El número debe tener entre 7 y 15 dígitos"
        const val PHONE_NUMBER_TOO_SHORT = "El número de teléfono es muy corto"
        const val PHONE_NUMBER_TOO_LONG = "El número de teléfono es muy largo"

        const val EMPTY_OTP = "Ingresa el código de verificación"
        const val INVALID_OTP = "El código debe tener exactamente 6 dígitos"
        const val OTP_CONTAINS_LETTERS = "El código solo debe contener números"
    }
}