package com.zanoapps.profile.presentation.profile

import com.zanoapps.profile.domain.model.UserProfile

data class ProfileState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val showLogoutConfirmation: Boolean = false
)
