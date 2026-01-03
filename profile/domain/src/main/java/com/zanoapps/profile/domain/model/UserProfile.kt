package com.zanoapps.profile.domain.model

data class UserProfile(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String?,
    val profileImageUrl: String?,
    val bio: String?,
    val isAgent: Boolean = false,
    val savedSearchesCount: Int = 0,
    val favouritesCount: Int = 0,
    val listingsCount: Int = 0,
    val memberSince: Long = System.currentTimeMillis()
) {
    val fullName: String
        get() = "$firstName $lastName"

    val initials: String
        get() = "${firstName.firstOrNull()?.uppercaseChar() ?: ""}${lastName.firstOrNull()?.uppercaseChar() ?: ""}"
}
