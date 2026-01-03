package com.zanoapps.profile.presentation.edit_profile

import com.zanoapps.presentation.ui.UiText

sealed interface EditProfileEvent {
    data class Error(val error: UiText) : EditProfileEvent
    data object ProfileUpdated : EditProfileEvent
    data object AccountDeleted : EditProfileEvent
}
