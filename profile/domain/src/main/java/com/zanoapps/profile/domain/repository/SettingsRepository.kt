package com.zanoapps.profile.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing app settings/preferences.
 * Uses DataStore for persistence.
 */
interface SettingsRepository {
    val notificationsEnabled: Flow<Boolean>
    val darkModeEnabled: Flow<Boolean>
    val selectedLanguage: Flow<String>

    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun setDarkModeEnabled(enabled: Boolean)
    suspend fun setSelectedLanguage(language: String)
}
