// shared/src/commonMain/kotlin/com/parceros/tijzi/di/AppModule.kt
package com.parceros.tijzi.di

import com.parceros.tijzi.data.datasources.SecureKeyValueStorage
import com.parceros.tijzi.data.remote.createHttpClient
import com.parceros.tijzi.data.repository.impl.AuthRepositoryImpl
import com.parceros.tijzi.data.repository.impl.CountryRepositoryImpl
import com.parceros.tijzi.data.repository.impl.SessionRepositoryImpl
import com.parceros.tijzi.data.repository.impl.RateLimitRepositoryImpl
import com.parceros.tijzi.data.repository.AuthRepository
import com.parceros.tijzi.data.repository.CountryRepository
import com.parceros.tijzi.data.repository.SessionRepository
import com.parceros.tijzi.data.repository.RateLimitRepository
import com.parceros.tijzi.domain.usecase.RequestOtpUseCase
import com.parceros.tijzi.domain.usecase.VerifyOtpUseCase
import io.ktor.client.*
import kotlinx.serialization.json.Json

object AppModule {

    // JSON Serializer
    val json: Json by lazy {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }

    // HTTP Client
    val httpClient: HttpClient by lazy {
        createHttpClient()
    }

    // Repositorios
    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(httpClient)
    }

    val countryRepository: CountryRepository by lazy {
        CountryRepositoryImpl(httpClient)
    }

    // Repositorios que requieren SecureKeyValueStorage
    fun createSessionRepository(secureKeyValueStorage: SecureKeyValueStorage): SessionRepository {
        return SessionRepositoryImpl(secureKeyValueStorage, json)
    }

    fun createRateLimitRepository(secureKeyValueStorage: SecureKeyValueStorage): RateLimitRepository {
        return RateLimitRepositoryImpl(secureKeyValueStorage, json)
    }

    // Use Cases
    fun createRequestOtpUseCase(rateLimitRepository: RateLimitRepository): RequestOtpUseCase {
        return RequestOtpUseCase(authRepository, rateLimitRepository, countryRepository)
    }

    fun createVerifyOtpUseCase(sessionRepository: SessionRepository): VerifyOtpUseCase {
        return VerifyOtpUseCase(authRepository, sessionRepository, countryRepository)
    }
}