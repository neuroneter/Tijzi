// shared/src/commonMain/kotlin/com/parceros/tijzi/presentation/viewmodel/AuthViewModelFactory.kt
package com.parceros.tijzi.presentation.viewmodel

import com.parceros.tijzi.data.datasources.SecureKeyValueStorage
import com.parceros.tijzi.di.AppModule
import kotlinx.coroutines.CoroutineScope

/**
 * Factory para crear AuthViewModel con todas las dependencias
 * Cada plataforma pasará su CoroutineScope específico
 */
object AuthViewModelFactory {

    fun create(
        secureKeyValueStorage: SecureKeyValueStorage,
        scope: CoroutineScope
    ): com.parceros.tijzi.presentation.viewmodel.AuthViewModel {

        // Crear repositorios que necesitan SecureKeyValueStorage
        val sessionRepository = AppModule.createSessionRepository(secureKeyValueStorage)
        val rateLimitRepository = AppModule.createRateLimitRepository(secureKeyValueStorage)

        // Crear use cases
        val requestOtpUseCase = AppModule.createRequestOtpUseCase(rateLimitRepository)
        val verifyOtpUseCase = AppModule.createVerifyOtpUseCase(sessionRepository)

        return AuthViewModel(
            requestOtpUseCase = requestOtpUseCase,
            verifyOtpUseCase = verifyOtpUseCase,
            countryRepository = AppModule.countryRepository,
            sessionRepository = sessionRepository,
            scope = scope
        )
    }
}