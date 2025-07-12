package com.parceros.tijzi.data.repository.impl

import com.parceros.tijzi.data.remote.dto.RequestOtpBodyDto
import com.parceros.tijzi.data.remote.dto.VerifyOtpBodyDto
import com.parceros.tijzi.data.remote.dto.VerifyOtpResponseDto
import com.parceros.tijzi.data.repository.AuthRepository
import com.parceros.tijzi.domain.model.UserSession
// Importa tu clase Result personalizada si la tienes, o usa kotlin.Result
// import com.parceros.tijzi.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse // Para verificar el status sin consumir el body inmediatamente
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.parceros.tijzi.util.Result// Si usas el Result de Kotlin

class AuthRepositoryImpl(private val httpClient: HttpClient) : AuthRepository {

    private val baseUrl = "https://tijzibackend-production.up.railway.app"
    private val requestOtpEndpoint = "$baseUrl/auth/send-code"
    private val verifyOtpEndpoint = "$baseUrl/auth/verify-code"

    override fun requestOtp(countryCode: String, phoneNumber: String): Flow<Result<Unit>> = flow { // CAMBIADO
        try {
            val requestBody = RequestOtpBodyDto(
                countryCode = countryCode,
                phoneNumber = phoneNumber)

            // Realiza la petición
            val response: HttpResponse = httpClient.post(requestOtpEndpoint) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

        if (response.status.isSuccess()) {
                emit(Result.Success(Unit)) // <-- Nota: Result.Success (con S mayúscula)
            } else {
                val errorMsg = try { response.body<String>() } catch (e: Exception) { "Failed to parse error body: ${e.message}" }
                emit(Result.Failure(Exception("API Error ${response.status.value}: $errorMsg"))) // <-- Nota: Result.Failure (con F mayúscula)
            }
        } catch (e: Exception) {
            emit(Result.Failure(Exception("Network or unexpected error: ${e.message}", e))) // <-- Nota: Result.Failure (con F mayúscula)
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
                val userSession = UserSession(
                    sessionToken = dto.sessionToken,
                    userId = dto.userId
                )
                // CORREGIDO: Usa tu clase Result.Success
                emit(Result.Success(userSession))
            } else {
                val errorMsg = try { response.body<String>() } catch (e: Exception) { "Failed to parse error body: ${e.message}" }
                // CORREGIDO: Usa tu clase Result.Failure
                emit(Result.Failure(Exception("API Error ${response.status.value}: $errorMsg")))
            }
        } catch (e: Exception) {
            // CORREGIDO: Usa tu clase Result.Failure
            emit(Result.Failure(Exception("Network or unexpected error: ${e.message}", e)))
        }
    }
}