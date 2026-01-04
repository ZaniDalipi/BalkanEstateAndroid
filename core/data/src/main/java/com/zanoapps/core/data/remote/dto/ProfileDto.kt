package com.zanoapps.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    @SerialName("_id")
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String? = null,
    val bio: String? = null,
    val profileImageUrl: String? = null,
    val role: String = "BUYER",
    val isAgent: Boolean = false,
    val savedSearchesCount: Int = 0,
    val favouritesCount: Int = 0,
    val listingsCount: Int = 0,
    val memberSince: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Serializable
data class ProfileResponse(
    val success: Boolean,
    val data: ProfileDto
)

@Serializable
data class UpdateProfileRequest(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String? = null,
    val bio: String? = null
)
