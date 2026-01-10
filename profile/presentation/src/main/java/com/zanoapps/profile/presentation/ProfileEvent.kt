package com.zanoapps.profile.presentation

sealed interface ProfileEvent {
    data object NavigateToMyListings : ProfileEvent
    data object NavigateToFavourites : ProfileEvent
    data object NavigateToSavedSearches : ProfileEvent
    data object NavigateToLogin : ProfileEvent
    data object ProfileUpdated : ProfileEvent
    data class Error(val message: String) : ProfileEvent
    data object LoggedOut : ProfileEvent
}
