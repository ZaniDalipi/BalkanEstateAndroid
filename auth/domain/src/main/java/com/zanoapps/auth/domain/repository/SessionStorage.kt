package com.zanoapps.auth.domain.repository

import com.zanoapps.auth.domain.model.AuthUser

interface SessionStorage {
    suspend fun setUser(user: AuthUser)
    suspend fun getUser(): AuthUser?
    suspend fun clearSession()
}
