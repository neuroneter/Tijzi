package com.parceros.tijzi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val nameEs: String,
    val nameEn: String,
    val isoCode: String, // <--- AÑADIDO RECOMENDADO
    val phoneCode: String, // Este es tu prefijo, ej: "+57"
    val numberLength: String? = null, // Podría ser Int si siempre es un número
    var customRegexPattern: String? = null
)