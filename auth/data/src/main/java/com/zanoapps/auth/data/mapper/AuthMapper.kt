package com.zanoapps.auth.data.mapper

import com.zanoapps.auth.data.dto.LoginResponse
import com.zanoapps.auth.domain.model.AuthInfo

fun LoginResponse.toAuthInfo(): AuthInfo {
    return AuthInfo(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId
    )
}
