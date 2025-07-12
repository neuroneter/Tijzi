package com.parceros.tijzi.android.di

import android.content.Context
import com.parceros.tijzi.data.datasources.SecureKeyValueStorage
import com.parceros.tijzi.di.AppModule
import com.parceros.tijzi.data.repository.SessionRepository
import com.parceros.tijzi.domain.usecase.VerifyOtpUseCase

object AndroidModule {

    fun provideSecureKeyValueStorage(context: Context): SecureKeyValueStorage {
        return SecureKeyValueStorage(context) // Android necesita Context
    }

    fun provideSessionRepository(context: Context): SessionRepository {
        val secureStorage = provideSecureKeyValueStorage(context)
        return AppModule.createSessionRepository(secureStorage)
    }

    fun provideVerifyOtpUseCase(context: Context): VerifyOtpUseCase {
        val sessionRepository = provideSessionRepository(context)
        return AppModule.createVerifyOtpUseCase(sessionRepository)
    }
}