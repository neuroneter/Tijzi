package com.parceros.tijzi.data.repository.impl

import com.parceros.tijzi.data.datasources.SecureKeyValueStorage
import com.parceros.tijzi.data.repository.RateLimitRepository
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class OtpRequestData(
    val requests: List<Long> = emptyList() // Timestamps de las solicitudes
)

class RateLimitRepositoryImpl(
    private val secureStorage: SecureKeyValueStorage,
    private val json: Json
) : RateLimitRepository {

    companion object {
        private const val KEY_PREFIX = "otp_rate_limit_"
        private const val MAX_REQUESTS_PER_HOUR = 3
        private const val MIN_INTERVAL_SECONDS = 60L
        private const val HOUR_IN_MILLIS = 3600000L // 1 hora en milisegundos
    }

    override suspend fun canRequestOtp(identifier: String): Boolean {
        val key = KEY_PREFIX + identifier
        val dataJson = secureStorage.getString(key)

        val currentTime = Clock.System.now().toEpochMilliseconds()

        if (dataJson == null) {
            return true // Primera solicitud
        }

        return try {
            val data = json.decodeFromString<OtpRequestData>(dataJson)
            val validRequests = data.requests.filter { timestamp ->
                currentTime - timestamp < HOUR_IN_MILLIS
            }

            // Verificar límite por hora
            if (validRequests.size >= MAX_REQUESTS_PER_HOUR) {
                return false
            }

            // Verificar intervalo mínimo
            val lastRequest = validRequests.maxOrNull()
            if (lastRequest != null && (currentTime - lastRequest) < (MIN_INTERVAL_SECONDS * 1000)) {
                return false
            }

            true
        } catch (e: Exception) {
            true // En caso de error, permitir la solicitud
        }
    }

    override suspend fun recordOtpRequest(identifier: String) {
        val key = KEY_PREFIX + identifier
        val dataJson = secureStorage.getString(key)
        val currentTime = Clock.System.now().toEpochMilliseconds()

        val data = if (dataJson != null) {
            try {
                json.decodeFromString<OtpRequestData>(dataJson)
            } catch (e: Exception) {
                OtpRequestData()
            }
        } else {
            OtpRequestData()
        }

        // Filtrar solicitudes antiguas y añadir la nueva
        val validRequests = data.requests.filter { timestamp ->
            currentTime - timestamp < HOUR_IN_MILLIS
        }

        val updatedData = data.copy(requests = validRequests + currentTime)
        val updatedJson = json.encodeToString(updatedData)

        secureStorage.saveString(key, updatedJson)
    }
}