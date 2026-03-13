package com.zanoapps.profile.presentation.profile

sealed interface ProfileAction {
    data object OnLoadProfile : ProfileAction
    data object OnEditProfileClick : ProfileAction
    data object OnCancelEdit : ProfileAction
    data object OnSaveProfile : ProfileAction
    data class OnFirstNameChanged(val firstName: String) : ProfileAction
    data class OnLastNameChanged(val lastName: String) : ProfileAction
    data class OnEmailChanged(val email: String) : ProfileAction
    data class OnPhoneChanged(val phone: String) : ProfileAction
    data class OnBioChanged(val bio: String) : ProfileAction
    data class OnLocationChanged(val location: String) : ProfileAction
    data object OnMyListingsClick : ProfileAction
    data object OnSavedPropertiesClick : ProfileAction
    data object OnSavedSearchesClick : ProfileAction
    data object OnNotificationSettingsClick : ProfileAction
    data object OnSubscriptionClick : ProfileAction
    data object OnHelpClick : ProfileAction
    data object OnPrivacyPolicyClick : ProfileAction
    data object OnTermsClick : ProfileAction
    data object OnLogoutClick : ProfileAction
    data object OnConfirmLogout : ProfileAction
    data object OnDismissLogout : ProfileAction
    data object OnDeleteAccountClick : ProfileAction
    data object OnConfirmDeleteAccount : ProfileAction
    data object OnDismissDeleteAccount : ProfileAction
    data object OnChangePasswordClick : ProfileAction
}
