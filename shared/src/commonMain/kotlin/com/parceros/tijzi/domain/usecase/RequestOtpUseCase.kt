package com.parceros.tijzi.domain.usecase

import com.parceros.tijzi.data.repository.AuthRepository
import com.parceros.tijzi.data.repository.RateLimitRepository
import com.parceros.tijzi.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RequestOtpUseCase(
    private val authRepository: AuthRepository,
    private val rateLimitRepository: RateLimitRepository
) {
    operator fun invoke(countryCode: String, phoneNumber: String): Flow<Result<Unit>> = flow {
        val identifier = "$countryCode-$phoneNumber"

        // Verificar rate limiting
        if (!rateLimitRepository.canRequestOtp(identifier)) {
            emit(Result.Failure(Exception("Has alcanzado el límite de códigos por WhatsApp. Intenta de nuevo en una hora.")))
            return@flow
        }

        // Proceder con la solicitud
        authRepository.requestOtp(countryCode, phoneNumber).collect { result ->
            if (result is Result.Success) {
                // Registrar la solicitud exitosa
                rateLimitRepository.recordOtpRequest(identifier)
            }
            emit(result)
        }
    }
}