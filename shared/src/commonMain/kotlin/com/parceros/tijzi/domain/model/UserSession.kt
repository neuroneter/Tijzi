package com.parceros.tijzi.domain.model

// Esta es la entidad que usará tu app internamente
// después de una autenticación exitosa.
data class UserSession(
    val sessionToken: String,
    val userId: String? = null, // Opcional, si tu API lo devuelve y lo necesitas
    val phoneNumber: String? = null, // Opcional, para tenerlo a mano
    // Otros datos del usuario que puedan venir con la sesión
)