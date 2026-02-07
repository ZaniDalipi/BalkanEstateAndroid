package com.zanoapps.shared.domain.repository

import com.zanoapps.shared.domain.model.AuthCredentials
import com.zanoapps.shared.domain.model.AuthResult
import com.zanoapps.shared.domain.model.RegisterRequest
import com.zanoapps.shared.domain.model.User
import com.zanoapps.shared.util.AuthError
import com.zanoapps.shared.util.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    /**
     * Login with email and password
     */
    suspend fun login(credentials: AuthCredentials): Result<AuthResult, AuthError>

    /**
     * Register a new user
     */
    suspend fun register(request: RegisterRequest): Result<AuthResult, AuthError>

    /**
     * Login with Google
     */
    suspend fun loginWithGoogle(idToken: String): Result<AuthResult, AuthError>

    /**
     * Logout the current user
     */
    suspend fun logout(): Result<Unit, AuthError>

    /**
     * Get the current user
     */
    fun getCurrentUser(): Flow<User?>

    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Flow<Boolean>

    /**
     * Refresh the access token
     */
    suspend fun refreshToken(): Result<String, AuthError>

    /**
     * Send password reset email
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit, AuthError>

    /**
     * Update user profile
     */
    suspend fun updateProfile(user: User): Result<User, AuthError>
}
