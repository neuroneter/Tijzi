package com.parceros.tijzi.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpBodyDto(
    // Opción A: Si tu API espera el número completo
    // val fullPhoneNumber: String,

    // Opción B: Si tu API espera los campos separados
    val countryCode: String,
    val phoneNumber: String,
    val otp: String // O como se llame el campo en tu API, ej "otpCode"
)