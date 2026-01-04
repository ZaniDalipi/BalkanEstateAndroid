package com.zanoapps.profile.data.repository

import com.zanoapps.core.data.mappers.toEntity
import com.zanoapps.core.data.mappers.toUserProfile
import com.zanoapps.core.data.remote.ProfileApiService
import com.zanoapps.core.data.remote.dto.UpdateProfileRequest
import com.zanoapps.core.database.dao.UserDao
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.profile.domain.model.UserProfile
import com.zanoapps.profile.domain.repository.ProfileRepository

/**
 * Single Source of Truth Implementation for Profile.
 *
 * Room database is the single source of truth.
 * Data is synced with MongoDB API.
 */
class ProfileRepositoryImpl(
    private val userDao: UserDao,
    private val profileApiService: ProfileApiService
) : ProfileRepository {

    // TODO: Get from auth repository/session
    private val currentUserId = "current_user"

    override suspend fun getProfile(): Result<UserProfile, DataError.Network> {
        // First try to get from local database
        val localUser = userDao.getUserByIdOnce(currentUserId)
        if (localUser != null) {
            // Return local data immediately
            return Result.Success(localUser.toUserProfile())
        }

        // If no local data, fetch from API
        return when (val result = profileApiService.getProfile(currentUserId)) {
            is Result.Success -> {
                val entity = result.data.data.toEntity()
                userDao.insertUser(entity)
                Result.Success(entity.toUserProfile())
            }
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun updateProfile(
        firstName: String,
        lastName: String,
        phoneNumber: String?,
        bio: String?
    ): EmptyResult<DataError.Network> {
        // Update locally first (optimistic update)
        val localUser = userDao.getUserByIdOnce(currentUserId)
        if (localUser != null) {
            userDao.updateUser(
                localUser.copy(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber,
                    bio = bio,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }

        // Sync with API
        return when (val result = profileApiService.updateProfile(
            userId = currentUserId,
            request = UpdateProfileRequest(
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                bio = bio
            )
        )) {
            is Result.Success -> {
                // Update local with server response
                userDao.insertUser(result.data.data.toEntity())
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun updateProfileImage(imageUri: String): EmptyResult<DataError.Network> {
        // Update locally first
        val localUser = userDao.getUserByIdOnce(currentUserId)
        if (localUser != null) {
            userDao.updateUser(
                localUser.copy(
                    profileImageUrl = imageUri,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }

        // Sync with API
        return when (val result = profileApiService.updateProfileImage(currentUserId, imageUri)) {
            is Result.Success -> {
                userDao.insertUser(result.data.data.toEntity())
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun deleteAccount(): EmptyResult<DataError.Network> {
        // Delete from API first
        return when (val result = profileApiService.deleteAccount(currentUserId)) {
            is Result.Success -> {
                // Clear local data
                userDao.deleteUser(currentUserId)
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }

    /**
     * Refresh profile from remote API.
     */
    suspend fun refreshProfile(): EmptyResult<DataError.Network> {
        return when (val result = profileApiService.getProfile(currentUserId)) {
            is Result.Success -> {
                userDao.insertUser(result.data.data.toEntity())
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }
}
