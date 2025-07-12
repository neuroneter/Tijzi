package com.parceros.tijzi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryDto(
    @SerialName("nameEs")
    val nameEs: String,

    @SerialName("nameEn")
    val nameEn: String,

    @SerialName("isoCode")
    val isoCode: String,

    @SerialName("phoneCode")
    val phoneCode: String,

    @SerialName("numberLength")
    val numberLength: String? = null,

    @SerialName("customRegexPattern")
    val customRegexPattern: String? = null
)