package com.zanoapps.profile.presentation.edit_profile

import androidx.compose.foundation.text.input.TextFieldState

data class EditProfileState(
    val firstName: TextFieldState = TextFieldState(),
    val lastName: TextFieldState = TextFieldState(),
    val email: String = "",
    val phoneNumber: TextFieldState = TextFieldState(),
    val bio: TextFieldState = TextFieldState(),
    val profileImageUrl: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val isFirstNameValid: Boolean = true,
    val isLastNameValid: Boolean = true,
    val canSave: Boolean = false
)
