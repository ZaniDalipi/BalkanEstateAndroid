package com.zanoapps.profile.data.repository

import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.profile.domain.model.UserProfile
import com.zanoapps.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl : ProfileRepository {

    // Mock user profile for now
    private var mockProfile = UserProfile(
        id = "1",
        email = "user@example.com",
        firstName = "John",
        lastName = "Doe",
        phoneNumber = "+1234567890",
        profileImageUrl = null,
        bio = "Looking for my dream home in the Balkans",
        isAgent = false,
        savedSearchesCount = 5,
        favouritesCount = 12,
        listingsCount = 0,
        memberSince = System.currentTimeMillis() - (365L * 24 * 60 * 60 * 1000) // 1 year ago
    )

    override suspend fun getProfile(): Result<UserProfile, DataError.Network> {
        return Result.Success(mockProfile)
    }

    override suspend fun updateProfile(
        firstName: String,
        lastName: String,
        phoneNumber: String?,
        bio: String?
    ): EmptyResult<DataError.Network> {
        mockProfile = mockProfile.copy(
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            bio = bio
        )
        return Result.Success(Unit)
    }

    override suspend fun updateProfileImage(imageUri: String): EmptyResult<DataError.Network> {
        mockProfile = mockProfile.copy(profileImageUrl = imageUri)
        return Result.Success(Unit)
    }

    override suspend fun deleteAccount(): EmptyResult<DataError.Network> {
        return Result.Success(Unit)
    }
}
