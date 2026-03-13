package com.zanoapps.auth.data.repository

import com.zanoapps.auth.domain.model.AccountType
import com.zanoapps.auth.domain.model.AuthUser
import com.zanoapps.auth.domain.repository.AuthRepository
import com.zanoapps.auth.domain.repository.SessionStorage
import com.zanoapps.core.database.dao.UserDao
import com.zanoapps.core.database.entity.UserEntity
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import java.util.UUID

class AuthRepositoryImpl(
    private val userDao: UserDao,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthUser, DataError.Network> {
        // Simulate login - in production this would call an API
        val user = AuthUser(
            id = UUID.randomUUID().toString(),
            email = email,
            firstName = email.substringBefore("@").replaceFirstChar { it.uppercase() },
            lastName = "User",
            token = "mock_token_${System.currentTimeMillis()}"
        )
        val entity = UserEntity(
            id = user.id, email = user.email,
            firstName = user.firstName, lastName = user.lastName,
            token = user.token, memberSince = "March 2026"
        )
        userDao.insert(entity)
        sessionStorage.setUser(user)
        return Result.Success(user)
    }

    override suspend fun register(
        firstName: String, lastName: String, email: String,
        phone: String, password: String, accountType: AccountType
    ): Result<AuthUser, DataError.Network> {
        val user = AuthUser(
            id = UUID.randomUUID().toString(),
            email = email, firstName = firstName, lastName = lastName,
            phone = phone, accountType = accountType,
            token = "mock_token_${System.currentTimeMillis()}"
        )
        val entity = UserEntity(
            id = user.id, email = user.email,
            firstName = user.firstName, lastName = user.lastName,
            phone = user.phone, accountType = accountType.name,
            token = user.token, memberSince = "March 2026"
        )
        userDao.insert(entity)
        sessionStorage.setUser(user)
        return Result.Success(user)
    }

    override suspend fun logout(): EmptyResult<DataError.Network> {
        userDao.deleteAll()
        sessionStorage.clearSession()
        return Result.Success(Unit)
    }

    override suspend fun getCurrentUser(): AuthUser? {
        return sessionStorage.getUser()
    }

    override suspend fun isLoggedIn(): Boolean {
        return sessionStorage.getUser() != null
    }
}
