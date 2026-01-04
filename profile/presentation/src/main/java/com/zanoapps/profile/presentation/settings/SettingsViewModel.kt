package com.zanoapps.profile.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.profile.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    var state by mutableStateOf(SettingsState())
        private set

    init {
        observeSettings()
    }

    private fun observeSettings() {
        settingsRepository.notificationsEnabled
            .onEach { enabled ->
                state = state.copy(notificationsEnabled = enabled)
            }
            .launchIn(viewModelScope)

        settingsRepository.darkModeEnabled
            .onEach { enabled ->
                state = state.copy(darkModeEnabled = enabled)
            }
            .launchIn(viewModelScope)

        settingsRepository.selectedLanguage
            .onEach { language ->
                state = state.copy(selectedLanguage = language)
            }
            .launchIn(viewModelScope)
    }

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
                viewModelScope.launch {
                    settingsRepository.setNotificationsEnabled(action.enabled)
                }
            }
            is SettingsAction.OnToggleDarkMode -> {
                viewModelScope.launch {
                    settingsRepository.setDarkModeEnabled(action.enabled)
                }
            }
        }
    }
}
