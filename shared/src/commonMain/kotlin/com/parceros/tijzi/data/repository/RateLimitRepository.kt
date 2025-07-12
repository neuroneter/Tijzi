package com.parceros.tijzi.data.repository

interface RateLimitRepository {
    /**
     * Verifica si el usuario puede solicitar un nuevo OTP para un identificador dado.
     * El identificador podría ser una combinación de código de país y número de teléfono.
     * Esta función contendría la lógica de "no más de 1 cada 60s" y "no más de 3 por hora".
     * @param identifier Un String único para el intento de login (ej. "+57-3001234567").
     * @return true si se permite la solicitud, false en caso contrario.
     */
    suspend fun canRequestOtp(identifier: String): Boolean

    /**
     * Registra que se ha realizado una solicitud de OTP para un identificador.
     * Esto es para que `canRequestOtp` pueda hacer sus cálculos.
     * @param identifier Un String único para el intento de login.
     */
    suspend fun recordOtpRequest(identifier: String)

    // Podrías añadir más métodos si necesitas más granularidad, por ejemplo:
    // suspend fun getOtpRequestCountLastHour(identifier: String): Int
    // suspend fun getLastOtpRequestTimestamp(identifier: String): Long?
}