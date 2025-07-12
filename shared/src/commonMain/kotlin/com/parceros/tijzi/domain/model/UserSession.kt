package com.parceros.tijzi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val sessionToken: String,
    val userId: String? = null,
    val phoneNumber: String? = null
)