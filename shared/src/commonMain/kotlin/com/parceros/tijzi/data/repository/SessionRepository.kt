
package com.parceros.tijzi.data.repository

import com.parceros.tijzi.domain.model.UserSession
import com.parceros.tijzi.util.Result
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun saveSession(userSession: UserSession): Flow<Result<Unit>>
    fun getSession(): Flow<Result<UserSession?>>
    fun clearSession(): Flow<Result<Unit>>
    fun isLoggedIn(): Flow<Boolean>
    fun getToken(): Flow<String?>
}