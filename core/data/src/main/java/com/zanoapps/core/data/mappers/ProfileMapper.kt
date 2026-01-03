package com.zanoapps.core.data.mappers

import com.zanoapps.core.data.remote.dto.ProfileDto
import com.zanoapps.core.database.entity.UserEntity
import com.zanoapps.profile.domain.model.UserProfile

fun ProfileDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        bio = bio,
        profileImageUrl = profileImageUrl,
        role = role,
        isAgent = isAgent,
        savedSearchesCount = savedSearchesCount,
        favouritesCount = favouritesCount,
        listingsCount = listingsCount,
        memberSince = memberSince,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun UserEntity.toUserProfile(): UserProfile {
    return UserProfile(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        profileImageUrl = profileImageUrl,
        bio = bio,
        isAgent = isAgent,
        savedSearchesCount = savedSearchesCount,
        favouritesCount = favouritesCount,
        listingsCount = listingsCount,
        memberSince = memberSince
    )
}
