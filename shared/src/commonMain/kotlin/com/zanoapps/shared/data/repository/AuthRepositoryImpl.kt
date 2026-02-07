package com.zanoapps.shared.data.repository

import com.zanoapps.shared.domain.model.User
import com.zanoapps.shared.domain.repository.AuthRepository
import com.zanoapps.shared.util.DataError
import com.zanoapps.shared.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class AuthRepositoryImpl : AuthRepository {

    private val currentUserFlow = MutableStateFlow<User?>(null)
    private val isLoggedInFlow = MutableStateFlow(false)

    override suspend fun login(email: String, password: String): Result<User, DataError> {
        return try {
            // Simulate login - replace with actual API call
            if (email.isNotBlank() && password.length >= 6) {
                val user = User(
                    id = "user_${System.currentTimeMillis()}",
                    email = email,
                    displayName = email.substringBefore("@"),
                    firstName = "User",
                    lastName = "Demo",
                    photoUrl = null,
                    phoneNumber = null,
                    isEmailVerified = true,
                    isAgent = false,
                    createdAt = "2024-01-01",
                    lastLoginAt = "2024-01-15"
                )
                currentUserFlow.value = user
                isLoggedInFlow.value = true
                Result.Success(user)
            } else {
                Result.Error(DataError.Network.UNAUTHORIZED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<User, DataError> {
        return try {
            if (email.isNotBlank() && password.length >= 6) {
                val user = User(
                    id = "user_${System.currentTimeMillis()}",
                    email = email,
                    displayName = "$firstName $lastName",
                    firstName = firstName,
                    lastName = lastName,
                    photoUrl = null,
                    phoneNumber = null,
                    isEmailVerified = false,
                    isAgent = false,
                    createdAt = "2024-01-15",
                    lastLoginAt = "2024-01-15"
                )
                currentUserFlow.value = user
                isLoggedInFlow.value = true
                Result.Success(user)
            } else {
                Result.Error(DataError.Network.BAD_REQUEST)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<User, DataError> {
        return try {
            val user = User(
                id = "google_user_${System.currentTimeMillis()}",
                email = "google.user@gmail.com",
                displayName = "Google User",
                firstName = "Google",
                lastName = "User",
                photoUrl = "https://example.com/photo.jpg",
                phoneNumber = null,
                isEmailVerified = true,
                isAgent = false,
                createdAt = "2024-01-15",
                lastLoginAt = "2024-01-15"
            )
            currentUserFlow.value = user
            isLoggedInFlow.value = true
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun logout(): Result<Unit, DataError> {
        return try {
            currentUserFlow.value = null
            isLoggedInFlow.value = false
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun getCurrentUser(): Result<User?, DataError> {
        return Result.Success(currentUserFlow.value)
    }

    override fun observeCurrentUser(): Flow<User?> {
        return currentUserFlow
    }

    override fun observeIsLoggedIn(): Flow<Boolean> {
        return isLoggedInFlow
    }

    override suspend fun updateProfile(user: User): Result<User, DataError> {
        return try {
            currentUserFlow.value = user
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit, DataError> {
        return try {
            if (email.isNotBlank() && email.contains("@")) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Network.BAD_REQUEST)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun deleteAccount(): Result<Unit, DataError> {
        return try {
            currentUserFlow.value = null
            isLoggedInFlow.value = false
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}
