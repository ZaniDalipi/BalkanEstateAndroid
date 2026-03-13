package com.zanoapps.auth.data.session

import com.zanoapps.auth.domain.model.AuthUser
import com.zanoapps.auth.domain.repository.SessionStorage

class InMemorySessionStorage : SessionStorage {
    private var currentUser: AuthUser? = null

    override suspend fun setUser(user: AuthUser) {
        currentUser = user
    }

    override suspend fun getUser(): AuthUser? {
        return currentUser
    }

    override suspend fun clearSession() {
        currentUser = null
    }
}
