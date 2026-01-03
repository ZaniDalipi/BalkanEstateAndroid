package com.zanoapps.auth.data.repository

import com.zanoapps.auth.data.dto.LoginRequest
import com.zanoapps.auth.data.dto.LoginResponse
import com.zanoapps.auth.data.dto.RegisterRequest
import com.zanoapps.auth.data.mapper.toAuthInfo
import com.zanoapps.auth.domain.model.AuthInfo
import com.zanoapps.auth.domain.repository.AuthRepository
import com.zanoapps.auth.domain.repository.SessionStorage
import com.zanoapps.core.data.networking.post
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthInfo, DataError.Network> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = "/auth/login",
            body = LoginRequest(email = email, password = password)
        )

        return when (result) {
            is Result.Success -> {
                val authInfo = result.data.toAuthInfo()
                sessionStorage.set(authInfo)
                Result.Success(authInfo)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): EmptyResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/auth/register",
            body = RegisterRequest(
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName
            )
        ).asEmptyDataResult()
    }

    override suspend fun logout(): EmptyResult<DataError.Network> {
        sessionStorage.clear()
        return Result.Success(Unit)
    }

    override suspend fun isLoggedIn(): Boolean {
        return sessionStorage.get() != null
    }
}
