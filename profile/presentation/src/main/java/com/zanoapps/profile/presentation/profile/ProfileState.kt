package com.zanoapps.profile.presentation.profile

import com.zanoapps.profile.domain.model.UserProfile

data class ProfileState(
    val userProfile: UserProfile = UserProfile(
        id = "1",
        firstName = "John",
        lastName = "Doe",
        email = "john.doe@example.com",
        phone = "+355 69 123 4567",
        location = "Tirana, Albania",
        memberSince = "January 2024",
        isAgent = false,
        isPremium = false,
        listingsCount = 3,
        savedCount = 12,
        reviewsCount = 5
    ),
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val editFirstName: String = "",
    val editLastName: String = "",
    val editEmail: String = "",
    val editPhone: String = "",
    val editBio: String = "",
    val editLocation: String = "",
    val isSaving: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val showDeleteAccountDialog: Boolean = false,
    val errorMessage: String? = null
)
