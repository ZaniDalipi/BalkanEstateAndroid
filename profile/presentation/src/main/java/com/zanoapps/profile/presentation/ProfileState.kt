package com.zanoapps.profile.presentation

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val avatarUrl: String?,
    val memberSince: String,
    val listingsCount: Int,
    val favouritesCount: Int,
    val savedSearchesCount: Int
)

data class ProfileState(
    val user: UserProfile? = null,
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val errorMessage: String? = null,
    val isEditProfileDialogVisible: Boolean = false,
    val isLogoutDialogVisible: Boolean = false
)
