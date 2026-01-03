package com.zanoapps.profile.presentation.settings

sealed interface SettingsAction {
    data object OnBackClick : SettingsAction
    data object OnNotificationsClick : SettingsAction
    data object OnPrivacyClick : SettingsAction
    data object OnLanguageClick : SettingsAction
    data object OnThemeClick : SettingsAction
    data object OnHelpClick : SettingsAction
    data object OnAboutClick : SettingsAction
    data object OnRateAppClick : SettingsAction
    data class OnToggleNotifications(val enabled: Boolean) : SettingsAction
    data class OnToggleDarkMode(val enabled: Boolean) : SettingsAction
}
