package com.parceros.tijzi.di

// --- DataSources ---
import com.parceros.tijzi.data.datasources.SecureKeyValueStorage // Correcto

// --- Implementaciones de Repositorio ---
// Ajusta estas rutas según tu estructura:
import com.parceros.tijzi.data.repository.impl.AuthRepositoryImpl    // Estaba en data.repository.impl
import com.parceros.tijzi.data.repository.impl.CountryRepositoryImpl // Estaba en data.repository
import com.parceros.tijzi.data.repository.impl.SessionRepositoryImpl  // Estaba en data.repository.impl

// --- Interfaces de Repositorio ---
// Ajusta estas rutas según tu estructura (parecen estar en data.repository):
import com.parceros.tijzi.data.repository.AuthRepository
import com.parceros.tijzi.data.repository.CountryRepository
import com.parceros.tijzi.data.repository.SessionRepository
// Si tienes RateLimitRepository y lo usas en AppModule, añádelo también:
// import com.parceros.tijzi.data.repository.RateLimitRepository

// --- Ktor y Serialización ---
import io.ktor.client.*
// Si tienes tu HttpClient.kt en data.remote, y ese es el que quieres usar,
// podrías importarlo directamente, pero el AppModule está creando su propia instancia
// configurada, lo cual está bien. Si quieres usar el de data.remote.HttpClient.kt,
// la lógica de creación de httpClient aquí cambiaría. Por ahora, asumimos que creamos uno nuevo.
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object AppModule {

    // 1. JSON Serializer
    val json: Json by lazy {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }

    // 2. HTTP Client
    val httpClient: HttpClient by lazy {
        // Aquí estás creando una nueva instancia de HttpClient.
        // Si tu archivo data/remote/HttpClient.kt define una función o un objeto
        // que ya crea y configura un cliente, podrías llamar a eso en su lugar.
        // Por ejemplo, si HttpClient.kt tiene:
        //   fun createPlatformHttpClient(): HttpClient { ... }
        // podrías hacer:
        //   HttpClient { createPlatformHttpClient() } o simplemente val httpClient = createPlatformHttpClient()
        // Por ahora, lo dejamos creando uno nuevo aquí:
        io.ktor.client.HttpClient { // Usando el nombre completo para evitar ambigüedad si tienes otro HttpClient importado
            install(ContentNegotiation) {
                json(json)
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL // Ajusta para producción
            }
            // Aquí puedes añadir más configuración como DefaultRequest si es necesario
        }
    }

    // --- Repositorios ---
    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(httpClient)
    }

    val countryRepository: CountryRepository by lazy {
        CountryRepositoryImpl(httpClient)
    }

    // Si también necesitas RateLimitRepository como una dependencia común:
    // val rateLimitRepository: RateLimitRepository by lazy {
    //     RateLimitRepositoryImpl(httpClient) // Asumiendo que existe RateLimitRepositoryImpl
    // }

    // SessionRepository
    fun createSessionRepository(secureKeyValueStorage: SecureKeyValueStorage): SessionRepository {
        return SessionRepositoryImpl(secureKeyValueStorage, json)
    }
}