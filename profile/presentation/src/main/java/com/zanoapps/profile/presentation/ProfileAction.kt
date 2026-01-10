package com.zanoapps.profile.presentation

sealed interface ProfileAction {
    data object LoadProfile : ProfileAction
    data object OnEditProfile : ProfileAction
    data object OnDismissEditProfile : ProfileAction
    data class OnSaveProfile(val name: String, val phone: String) : ProfileAction
    data object OnChangePassword : ProfileAction
    data object OnToggleNotifications : ProfileAction
    data object OnToggleDarkMode : ProfileAction
    data object OnMyListings : ProfileAction
    data object OnMyFavourites : ProfileAction
    data object OnMySavedSearches : ProfileAction
    data object OnHelpCenter : ProfileAction
    data object OnPrivacyPolicy : ProfileAction
    data object OnTermsOfService : ProfileAction
    data object OnAboutUs : ProfileAction
    data object OnLogoutClick : ProfileAction
    data object OnConfirmLogout : ProfileAction
    data object OnDismissLogout : ProfileAction
    data object OnDeleteAccount : ProfileAction
}
