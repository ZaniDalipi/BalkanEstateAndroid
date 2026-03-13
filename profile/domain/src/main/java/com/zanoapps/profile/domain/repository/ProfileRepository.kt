package com.zanoapps.profile.domain.repository

import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.profile.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<UserProfile>
    suspend fun updateProfile(profile: UserProfile): EmptyResult<DataError.Network>
    suspend fun changePassword(oldPassword: String, newPassword: String): EmptyResult<DataError.Network>
    suspend fun deleteAccount(): EmptyResult<DataError.Network>
}
