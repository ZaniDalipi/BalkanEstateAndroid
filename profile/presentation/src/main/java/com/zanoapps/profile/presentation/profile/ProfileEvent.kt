package com.zanoapps.profile.presentation.profile

import com.zanoapps.presentation.ui.UiText

sealed interface ProfileEvent {
    data class Error(val error: UiText) : ProfileEvent
    data object ProfileSaved : ProfileEvent
    data object NavigateToLogin : ProfileEvent
    data object NavigateToMyListings : ProfileEvent
    data object NavigateToSavedProperties : ProfileEvent
    data object NavigateToSavedSearches : ProfileEvent
    data object NavigateToSubscription : ProfileEvent
    data object NavigateToNotificationSettings : ProfileEvent
}
