package com.zanoapps.auth.domain.model

data class AuthInfo(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)
