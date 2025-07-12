// shared/src/commonMain/kotlin/com/parceros/tijzi/data/remote/HttpClient.kt
package com.parceros.tijzi.data.remote

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun createHttpClient(): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                encodeDefaults = true
            })
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY // Cambiar a LogLevel.INFO para producción
        }

        // 🔥 MEJORADO: Configuración del engine CIO con timeouts más apropiados
        engine {
            maxConnectionsCount = 1000
            endpoint {
                maxConnectionsPerRoute = 100
                pipelineMaxSize = 20
                keepAliveTime = 5000
                connectTimeout = 15000      // 15s para conectar
                socketTimeout = 30000       // 🔥 AÑADIDO: 30s para recibir datos
                requestTimeout = 45000      // 🔥 AÑADIDO: 45s total por request
                connectAttempts = 3         // 🔥 MEJORADO: Reducido de 5 a 3
            }
        }
    }
}