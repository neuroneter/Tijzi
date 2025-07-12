package com.parceros.tijzi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryDto(
    @SerialName("nameES")
    val nameEs: String,

    @SerialName("nameEN")
    val nameEn: String,

    @SerialName("phoneCode")
    val phoneCode: String,

    @SerialName("numberLength")
    val numberLength: String
)