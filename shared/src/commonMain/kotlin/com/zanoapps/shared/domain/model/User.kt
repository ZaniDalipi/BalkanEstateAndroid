package com.zanoapps.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val displayName: String,
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val avatarUrl: String? = null,
    val location: String = "",
    val country: String = "",
    val isVerified: Boolean = false,
    val isAgent: Boolean = false,
    val agentProfile: AgentProfile? = null,
    val createdAt: Long = 0,
    val lastLoginAt: Long = 0
)

@Serializable
data class AgentProfile(
    val agencyId: String? = null,
    val agencyName: String = "",
    val title: String = "",
    val bio: String = "",
    val rating: Float = 0f,
    val reviewsCount: Int = 0,
    val propertiesSold: Int = 0,
    val yearsExperience: Int = 0,
    val specializations: List<String> = emptyList(),
    val languages: List<String> = emptyList(),
    val certifications: List<String> = emptyList()
)

@Serializable
data class Agency(
    val id: String,
    val name: String,
    val description: String = "",
    val logoUrl: String? = null,
    val website: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val city: String = "",
    val country: String = "",
    val rating: Float = 0f,
    val reviewsCount: Int = 0,
    val agentsCount: Int = 0,
    val activeListings: Int = 0,
    val yearEstablished: Int = 0,
    val isVerified: Boolean = false,
    val specializations: List<String> = emptyList()
)

@Serializable
data class AuthCredentials(
    val email: String,
    val password: String
)

@Serializable
data class AuthResult(
    val user: User,
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Long
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phone: String = ""
)
