package com.zanoapps.auth.domain.repository

import com.zanoapps.auth.domain.model.AuthInfo

interface SessionStorage {
    suspend fun get(): AuthInfo?
    suspend fun set(info: AuthInfo?)
    suspend fun clear()
}
