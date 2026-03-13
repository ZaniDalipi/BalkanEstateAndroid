package com.zanoapps.profile.data.repository

import com.zanoapps.core.database.dao.UserDao
import com.zanoapps.core.database.entity.UserEntity
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.profile.domain.model.UserProfile
import com.zanoapps.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepositoryImpl(
    private val userDao: UserDao
) : ProfileRepository {

    override fun getProfile(): Flow<UserProfile> {
        return userDao.getCurrentUser().map { entity ->
            entity?.toDomainProfile() ?: UserProfile()
        }
    }

    override suspend fun updateProfile(profile: UserProfile): EmptyResult<DataError.Network> {
        return try {
            val existing = userDao.getUser()
            val entity = UserEntity(
                id = profile.id.ifBlank { existing?.id ?: "current_user" },
                email = profile.email,
                firstName = profile.firstName,
                lastName = profile.lastName,
                phone = profile.phone,
                avatarUrl = profile.avatarUrl,
                bio = profile.bio,
                location = profile.location,
                memberSince = profile.memberSince,
                isAgent = profile.isAgent,
                isPremium = profile.isPremium,
                listingsCount = profile.listingsCount,
                savedCount = profile.savedCount,
                reviewsCount = profile.reviewsCount,
                token = existing?.token ?: ""
            )
            userDao.insert(entity)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): EmptyResult<DataError.Network> {
        // In a real app this would call an API
        return Result.Success(Unit)
    }

    override suspend fun deleteAccount(): EmptyResult<DataError.Network> {
        return try {
            userDao.deleteAll()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}

private fun UserEntity.toDomainProfile(): UserProfile {
    return UserProfile(
        id = id, firstName = firstName, lastName = lastName,
        email = email, phone = phone, avatarUrl = avatarUrl,
        bio = bio, location = location, memberSince = memberSince,
        isAgent = isAgent, isPremium = isPremium,
        listingsCount = listingsCount, savedCount = savedCount,
        reviewsCount = reviewsCount
    )
}
