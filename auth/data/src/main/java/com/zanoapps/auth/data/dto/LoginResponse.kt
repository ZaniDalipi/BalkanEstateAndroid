package com.zanoapps.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)
