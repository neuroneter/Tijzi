package com.parceros.tijzi.data.repository.impl

import com.parceros.tijzi.data.remote.dto.RequestOtpBodyDto
import com.parceros.tijzi.data.remote.dto.VerifyOtpBodyDto
import com.parceros.tijzi.data.remote.dto.VerifyOtpResponseDto
import com.parceros.tijzi.data.repository.AuthRepository
import com.parceros.tijzi.domain.model.UserSession
import com.parceros.tijzi.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(private val httpClient: HttpClient) : AuthRepository {

    private val baseUrl = "https://tijzibackend-production.up.railway.app"
    private val requestOtpEndpoint = "$baseUrl/auth/send-code"
    private val verifyOtpEndpoint = "$baseUrl/auth/verify-code"

    override fun requestOtp(countryCode: String, phoneNumber: String): Flow<Result<Unit>> = flow {
        try {
            val requestBody = RequestOtpBodyDto(
                countryCode = countryCode,
                phoneNumber = phoneNumber
            )

            val response: HttpResponse = httpClient.post(requestOtpEndpoint) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            if (response.status.isSuccess()) {
                emit(Result.Success(Unit))
            } else {
                val errorMsg = try {
                    response.body<String>()
                } catch (e: Exception) {
                    "Error del servidor: ${response.status.value}"
                }
                emit(Result.Failure(Exception(createUserFriendlyError(response.status.value, errorMsg))))
            }
        } catch (e: Exception) {
            emit(Result.Failure(Exception(createNetworkError(e))))
        }
    }

    override fun verifyOtp(countryCode: String, phoneNumber: String, otp: String): Flow<Result<UserSession>> = flow {
        try {
            val requestBody = VerifyOtpBodyDto(
                countryCode = countryCode,
                phoneNumber = phoneNumber,
                otp = otp
            )

            val response: HttpResponse = httpClient.post(verifyOtpEndpoint) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            if (response.status.isSuccess()) {
                val dto = response.body<VerifyOtpResponseDto>()

                // 🔥 VALIDACIÓN DE RESPONSE
                val validationResult = validateVerifyOtpResponse(dto)
                if (validationResult.isInvalid) {
                    val errorException = validationResult.getExceptionOrNull()
                        ?: Exception("Error de validación de respuesta")
                    emit(Result.Failure(errorException))
                    return@flow
                }

                // 🔥 CREAR UserSession SOLO SI LA VALIDACIÓN PASA
                val userSession = UserSession(
                    sessionToken = dto.sessionToken.trim(),
                    userId = dto.userId?.takeIf { it.isNotBlank() },
                    phoneNumber = "$countryCode$phoneNumber" // Guardar número completo
                )

                emit(Result.Success(userSession))
            } else {
                val errorMsg = try {
                    response.body<String>()
                } catch (e: Exception) {
                    "Error del servidor: ${response.status.value}"
                }
                emit(Result.Failure(Exception(createUserFriendlyError(response.status.value, errorMsg))))
            }
        } catch (e: Exception) {
            emit(Result.Failure(Exception(createNetworkError(e))))
        }
    }

    /**
     * Valida que la respuesta del backend sea válida
     */
    private fun validateVerifyOtpResponse(dto: VerifyOtpResponseDto): ResponseValidationResult {
        return when {
            dto.sessionToken.isBlank() -> {
                ResponseValidationResult.Invalid(Exception("Error del servidor: no se recibió un token de sesión válido"))
            }

            dto.sessionToken.length < 10 -> {
                ResponseValidationResult.Invalid(Exception("Error del servidor: token de sesión inválido"))
            }

            // Validaciones adicionales si las necesitas
            dto.sessionToken.contains(" ") -> {
                ResponseValidationResult.Invalid(Exception("Error del servidor: formato de token inválido"))
            }

            else -> ResponseValidationResult.Valid
        }
    }

    /**
     * Crea mensajes de error amigables basados en el código de estado HTTP
     */
    private fun createUserFriendlyError(statusCode: Int, originalMessage: String): String {
        return when (statusCode) {
            400 -> "Los datos enviados no son válidos. Verifica el número de teléfono y código."
            401 -> "El código de verificación es incorrecto o ha expirado."
            404 -> "Servicio no disponible. Intenta más tarde."
            429 -> "Demasiadas solicitudes. Espera un momento antes de intentar nuevamente."
            500, 502, 503 -> "Error del servidor. Intenta más tarde."
            else -> "Error de conexión: $originalMessage"
        }
    }

    /**
     * Crea mensajes de error amigables para errores de red
     */
    private fun createNetworkError(exception: Exception): String {
        return when {
            exception.message?.contains("timeout", ignoreCase = true) == true ->
                "La conexión tardó demasiado. Verifica tu internet e intenta nuevamente."

            exception.message?.contains("network", ignoreCase = true) == true ->
                "Error de conexión. Verifica tu internet e intenta nuevamente."

            exception.message?.contains("host", ignoreCase = true) == true ->
                "No se pudo conectar al servidor. Verifica tu internet."

            else -> "Error de conexión: ${exception.message}"
        }
    }

    /**
     * Resultado interno para validación de responses
     */
    private sealed class ResponseValidationResult {
        object Valid : ResponseValidationResult()
        data class Invalid(val exception: Exception) : ResponseValidationResult()

        val isValid: Boolean get() = this is Valid
        val isInvalid: Boolean get() = this is Invalid

        // Helper function para obtener la excepción de forma segura
        fun getExceptionOrNull(): Exception? = when (this) {
            is Invalid -> exception
            is Valid -> null
        }
    }
}