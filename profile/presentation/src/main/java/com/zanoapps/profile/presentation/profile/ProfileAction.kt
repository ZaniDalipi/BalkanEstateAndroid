package com.zanoapps.profile.presentation.profile

sealed interface ProfileAction {
    data object OnBackClick : ProfileAction
    data object OnEditProfileClick : ProfileAction
    data object OnSavedSearchesClick : ProfileAction
    data object OnFavouritesClick : ProfileAction
    data object OnMyListingsClick : ProfileAction
    data object OnSettingsClick : ProfileAction
    data object OnHelpClick : ProfileAction
    data object OnLogoutClick : ProfileAction
    data object OnRefresh : ProfileAction
}
