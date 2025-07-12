package com.parceros.tijzi.domain.usecases

import com.parceros.tijzi.data.repository.AuthRepository
import com.parceros.tijzi.data.repository.SessionRepository
import com.parceros.tijzi.domain.model.UserSession
import com.parceros.tijzi.util.Result // Tu clase Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class VerifyOtpUseCase(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository
) {
    operator fun invoke(
        countryCode: String,
        phoneNumber: String,
        otp: String
    ): Flow<Result<UserSession>> { // Devuelve la UserSession en caso de éxito
        return authRepository.verifyOtp(countryCode, phoneNumber, otp)
            .flatMapConcat { verificationResult -> // Se usa para encadenar Flows basados en la emisión del anterior
                when (verificationResult) {
                    is Result.Success -> {
                        // OTP verificado con éxito, ahora guarda la sesión
                        val userSession = verificationResult.data
                        sessionRepository.saveSession(userSession)
                            .map { saveResult ->
                                // Transformamos el resultado de guardar (Result<Unit>)
                                // de nuevo a un resultado que contenga la UserSession
                                // si el guardado fue exitoso.
                                when (saveResult) {
                                    is Result.Success -> Result.Success(userSession)
                                    is Result.Failure -> Result.Failure(saveResult.exception) // Propaga el error de guardado
                                }
                            }
                    }
                    is Result.Failure -> {
                        // La verificación del OTP falló, emite el error directamente
                        flow { emit(Result.Failure(verificationResult.exception)) }
                    }
                }
            }
    }
}