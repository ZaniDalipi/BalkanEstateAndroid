package com.zanoapps.profile.presentation.profile

import com.zanoapps.presentation.ui.UiText

sealed interface ProfileEvent {
    data class Error(val error: UiText) : ProfileEvent
    data object LogoutSuccess : ProfileEvent
}
