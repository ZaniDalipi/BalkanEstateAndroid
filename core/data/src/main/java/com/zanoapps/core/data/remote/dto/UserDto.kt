package com.zanoapps.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("_id")
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String? = null,
    val bio: String? = null,
    val profileImageUrl: String? = null,
    val role: String = "BUYER",
    val createdAt: Long,
    val updatedAt: Long
)

@Serializable
data class UserResponse(
    val success: Boolean,
    val data: UserDto
)

@Serializable
data class UpdateUserRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    val bio: String? = null,
    val profileImageUrl: String? = null
)
