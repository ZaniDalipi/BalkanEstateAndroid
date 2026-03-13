package com.zanoapps.profile.domain.model

data class UserProfile(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val avatarUrl: String = "",
    val bio: String = "",
    val location: String = "",
    val memberSince: String = "",
    val isAgent: Boolean = false,
    val isPremium: Boolean = false,
    val listingsCount: Int = 0,
    val savedCount: Int = 0,
    val reviewsCount: Int = 0
)
