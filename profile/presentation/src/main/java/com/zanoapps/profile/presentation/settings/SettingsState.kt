package com.zanoapps.profile.presentation.settings

data class SettingsState(
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val selectedLanguage: String = "English",
    val appVersion: String = "1.0.0"
)
