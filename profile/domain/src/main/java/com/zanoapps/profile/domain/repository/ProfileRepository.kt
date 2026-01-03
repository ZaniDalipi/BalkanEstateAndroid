package com.zanoapps.profile.domain.repository

import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.profile.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getProfile(): Result<UserProfile, DataError.Network>
    suspend fun updateProfile(
        firstName: String,
        lastName: String,
        phoneNumber: String?,
        bio: String?
    ): EmptyResult<DataError.Network>
    suspend fun updateProfileImage(imageUri: String): EmptyResult<DataError.Network>
    suspend fun deleteAccount(): EmptyResult<DataError.Network>
}
