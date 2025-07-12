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

        // 🔥 NUEVO: Configuración del engine CIO más robusta
        engine {
            // Configuraciones específicas de CIO
            maxConnectionsCount = 1000
            endpoint {
                maxConnectionsPerRoute = 100
                pipelineMaxSize = 20
                keepAliveTime = 5000
                connectTimeout = 15000
                connectAttempts = 5
            }
        }
    }
}