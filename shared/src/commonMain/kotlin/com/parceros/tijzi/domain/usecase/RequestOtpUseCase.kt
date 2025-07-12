// En shared/src/commonMain/kotlin/com/parceros/tijzi/domain/usecases/RequestOtpUseCase.kt
package com.parceros.tijzi.domain.usecases

import com.parceros.tijzi.data.repository.AuthRepository
import com.parceros.tijzi.util.Result // <-- IMPORTA TU RESULT PERSONALIZADO
import kotlinx.coroutines.flow.Flow

class RequestOtpUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(countryCode: String, phoneNumber: String): Flow<Result<Unit>> {
        return authRepository.requestOtp(countryCode, phoneNumber)
    }
}