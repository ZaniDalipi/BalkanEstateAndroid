package com.zanoapps.auth.domain.repository

import com.zanoapps.auth.domain.model.AccountType
import com.zanoapps.auth.domain.model.AuthUser
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthUser, DataError.Network>
    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        password: String,
        accountType: AccountType
    ): Result<AuthUser, DataError.Network>
    suspend fun loginWithGoogle(): Result<AuthUser, DataError.Network>
    suspend fun loginWithFacebook(): Result<AuthUser, DataError.Network>
    suspend fun logout(): EmptyResult<DataError.Network>
    suspend fun getCurrentUser(): AuthUser?
    suspend fun isLoggedIn(): Boolean
}
