package com.parceros.tijzi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpResponseDto(
    @SerialName("session_token") // o como se llame en tu JSON
    val sessionToken: String,

    @SerialName("user_id") // o como se llame, si existe
    val userId: String? = null,

    // Si tu API devuelve otros campos útiles para UserSession, añádelos aquí
    // Por ejemplo, si devuelve el número de teléfono verificado:
    // @SerialName("verified_phone_number")
    // val verifiedPhoneNumber: String? = null
)