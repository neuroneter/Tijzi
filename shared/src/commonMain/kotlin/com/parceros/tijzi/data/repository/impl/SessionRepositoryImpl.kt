package com.parceros.tijzi.data.repository.impl

import com.parceros.tijzi.data.datasources.SecureKeyValueStorage
import com.parceros.tijzi.data.repository.SessionRepository
import com.parceros.tijzi.domain.model.UserSession
import com.parceros.tijzi.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SessionRepositoryImpl(
    private val secureStorage: SecureKeyValueStorage,
    private val json: Json
) : SessionRepository {

    companion object {
        private const val KEY_USER_SESSION = "user_session_key"
    }

    override fun saveSession(userSession: UserSession): Flow<Result<Unit>> = flow {
        try {
            val sessionJson = json.encodeToString(userSession)
            secureStorage.saveString(KEY_USER_SESSION, sessionJson)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    override fun getSession(): Flow<Result<UserSession?>> = flow {
        try {
            val sessionJson = secureStorage.getString(KEY_USER_SESSION)
            if (sessionJson != null) {
                val userSession = json.decodeFromString<UserSession>(sessionJson)
                emit(Result.Success(userSession))
            } else {
                emit(Result.Success(null))
            }
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    override fun clearSession(): Flow<Result<Unit>> = flow {
        try {
            secureStorage.removeString(KEY_USER_SESSION)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return getSession().map { result ->
            result is Result.Success && result.data?.sessionToken?.isNotBlank() == true
        }.catch {
            emit(false)
        }
    }

    override fun getToken(): Flow<String?> = flow {
        try {
            val sessionJson = secureStorage.getString(KEY_USER_SESSION)
            if (sessionJson != null) {
                val userSession = json.decodeFromString<UserSession>(sessionJson)
                emit(userSession.sessionToken)
            } else {
                emit(null)
            }
        } catch (e: Exception) {
            emit(null)
        }
    }
}