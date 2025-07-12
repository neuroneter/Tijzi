package com.parceros.tijzi.data.remote // Asegúrate de que el paquete sea este

import io.ktor.client.*
import io.ktor.client.engine.cio.*      // O el engine que hayas decidido usar (OkHttp, Darwin, etc.)
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

// 👇 ESTA ES LA FUNCIÓN QUE TU CountryRepositoryImpl ESTÁ BUSCANDO 👇
fun createHttpClient(): HttpClient {
    return HttpClient(CIO) { // Usa el engine CIO o el que hayas configurado
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true // Muy importante para APIs que pueden añadir campos
                // encodeDefaults = true // Descomenta si necesitas enviar valores por defecto aunque sean null o el valor por defecto del tipo
            })
        }

        install(Logging) {
            logger = Logger.DEFAULT // Puedes usar un logger personalizado si lo deseas
            level = LogLevel.ALL    // LogLevel.BODY es bueno para producción, LogLevel.ALL para desarrollo
        }

        // Aquí puedes añadir configuraciones por defecto para todas las peticiones si lo necesitas
        // por ejemplo, cabeceras comunes, timeouts, etc.
        // defaultRequest {
        //    header("X-Api-Key", "tu_api_key_si_la_necesitas")
        // }
    }
}