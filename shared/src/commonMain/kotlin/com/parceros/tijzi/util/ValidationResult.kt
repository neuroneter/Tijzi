// shared/src/commonMain/kotlin/com/parceros/tijzi/util/ValidationResult.kt
package com.parceros.tijzi.util

/**
 * Resultado de validación que puede contener múltiples errores
 */
sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val errors: List<String>) : ValidationResult() {
        constructor(error: String) : this(listOf(error))
    }

    val isValid: Boolean get() = this is Valid
    val isInvalid: Boolean get() = this is Invalid

    // MOVIDO: firstError ahora está en la clase base
    val firstError: String get() = when (this) {
        is Invalid -> errors.first()
        is Valid -> throw IllegalStateException("No hay errores en un resultado válido")
    }

    // Helper function más segura
    fun getFirstErrorOrNull(): String? = when (this) {
        is Invalid -> errors.firstOrNull()
        is Valid -> null
    }
}

/**
 * Extensión para combinar múltiples validaciones
 */
fun List<ValidationResult>.combine(): ValidationResult {
    val errors = mutableListOf<String>()

    forEach { result ->
        if (result is ValidationResult.Invalid) {
            errors.addAll(result.errors)
        }
    }

    return if (errors.isEmpty()) {
        ValidationResult.Valid
    } else {
        ValidationResult.Invalid(errors)
    }
}