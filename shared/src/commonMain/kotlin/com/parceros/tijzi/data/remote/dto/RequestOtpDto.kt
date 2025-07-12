package com.parceros.tijzi.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RequestOtpDto(
    val phoneNumber: String
    // otros campos si tu API los requiere para solicitar OTP
)