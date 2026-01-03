package com.zanoapps.auth.domain.repository

import com.zanoapps.auth.domain.model.AuthInfo
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthInfo, DataError.Network>
    suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): EmptyResult<DataError.Network>
    suspend fun logout(): EmptyResult<DataError.Network>
    suspend fun isLoggedIn(): Boolean
}
