package com.zanoapps.profile.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {

    var state by mutableStateOf(SettingsState())
        private set

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.OnBackClick -> Unit // Handled by navigation
            SettingsAction.OnNotificationsClick -> Unit // Handled by navigation
            SettingsAction.OnPrivacyClick -> Unit // Handled by navigation
            SettingsAction.OnLanguageClick -> Unit // Handled by navigation
            SettingsAction.OnThemeClick -> Unit // TODO: Show theme picker
            SettingsAction.OnHelpClick -> Unit // TODO: Open help center
            SettingsAction.OnAboutClick -> Unit // TODO: Show about dialog
            SettingsAction.OnRateAppClick -> Unit // TODO: Open play store
            is SettingsAction.OnToggleNotifications -> {
                state = state.copy(notificationsEnabled = action.enabled)
                // TODO: Save to preferences
            }
            is SettingsAction.OnToggleDarkMode -> {
                state = state.copy(darkModeEnabled = action.enabled)
                // TODO: Save to preferences and apply theme
            }
        }
    }
}
