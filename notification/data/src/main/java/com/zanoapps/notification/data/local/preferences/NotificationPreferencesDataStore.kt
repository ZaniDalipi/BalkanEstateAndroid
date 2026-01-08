package com.zanoapps.notification.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.zanoapps.notification.domain.model.NotificationPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStore for notification preferences.
 */
private val Context.notificationPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "notification_preferences"
)

class NotificationPreferencesDataStore(
    private val context: Context
) {

    private object PreferenceKeys {
        val PUSH_NOTIFICATIONS_ENABLED = booleanPreferencesKey("push_notifications_enabled")
        val PRICE_DROPS_ENABLED = booleanPreferencesKey("price_drops_enabled")
        val NEW_LISTINGS_ENABLED = booleanPreferencesKey("new_listings_enabled")
        val MESSAGES_ENABLED = booleanPreferencesKey("messages_enabled")
        val VIEWING_REMINDERS_ENABLED = booleanPreferencesKey("viewing_reminders_enabled")
        val PROPERTY_STATUS_ENABLED = booleanPreferencesKey("property_status_enabled")
        val SAVED_SEARCH_ALERTS_ENABLED = booleanPreferencesKey("saved_search_alerts_enabled")
        val PROMOTIONAL_ENABLED = booleanPreferencesKey("promotional_enabled")
        val SYSTEM_ANNOUNCEMENTS_ENABLED = booleanPreferencesKey("system_announcements_enabled")
        val EMAIL_NOTIFICATIONS_ENABLED = booleanPreferencesKey("email_notifications_enabled")
        val NOTIFICATION_EMAIL = stringPreferencesKey("notification_email")
        val QUIET_HOURS_START = intPreferencesKey("quiet_hours_start")
        val QUIET_HOURS_END = intPreferencesKey("quiet_hours_end")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val SHOW_ON_LOCK_SCREEN = booleanPreferencesKey("show_on_lock_screen")
        val MIN_PRICE_DROP_PERCENTAGE = intPreferencesKey("min_price_drop_percentage")
    }

    val preferencesFlow: Flow<NotificationPreferences> = context.notificationPreferencesDataStore.data
        .map { preferences ->
            NotificationPreferences(
                pushNotificationsEnabled = preferences[PreferenceKeys.PUSH_NOTIFICATIONS_ENABLED] ?: true,
                priceDropsEnabled = preferences[PreferenceKeys.PRICE_DROPS_ENABLED] ?: true,
                newListingsEnabled = preferences[PreferenceKeys.NEW_LISTINGS_ENABLED] ?: true,
                messagesEnabled = preferences[PreferenceKeys.MESSAGES_ENABLED] ?: true,
                viewingRemindersEnabled = preferences[PreferenceKeys.VIEWING_REMINDERS_ENABLED] ?: true,
                propertyStatusEnabled = preferences[PreferenceKeys.PROPERTY_STATUS_ENABLED] ?: true,
                savedSearchAlertsEnabled = preferences[PreferenceKeys.SAVED_SEARCH_ALERTS_ENABLED] ?: true,
                promotionalEnabled = preferences[PreferenceKeys.PROMOTIONAL_ENABLED] ?: false,
                systemAnnouncementsEnabled = preferences[PreferenceKeys.SYSTEM_ANNOUNCEMENTS_ENABLED] ?: true,
                emailNotificationsEnabled = preferences[PreferenceKeys.EMAIL_NOTIFICATIONS_ENABLED] ?: false,
                notificationEmail = preferences[PreferenceKeys.NOTIFICATION_EMAIL],
                quietHoursStart = preferences[PreferenceKeys.QUIET_HOURS_START]?.takeIf { it >= 0 },
                quietHoursEnd = preferences[PreferenceKeys.QUIET_HOURS_END]?.takeIf { it >= 0 },
                soundEnabled = preferences[PreferenceKeys.SOUND_ENABLED] ?: true,
                vibrationEnabled = preferences[PreferenceKeys.VIBRATION_ENABLED] ?: true,
                showOnLockScreen = preferences[PreferenceKeys.SHOW_ON_LOCK_SCREEN] ?: true,
                minPriceDropPercentage = preferences[PreferenceKeys.MIN_PRICE_DROP_PERCENTAGE] ?: 0
            )
        }

    suspend fun updatePreferences(preferences: NotificationPreferences) {
        context.notificationPreferencesDataStore.edit { prefs ->
            prefs[PreferenceKeys.PUSH_NOTIFICATIONS_ENABLED] = preferences.pushNotificationsEnabled
            prefs[PreferenceKeys.PRICE_DROPS_ENABLED] = preferences.priceDropsEnabled
            prefs[PreferenceKeys.NEW_LISTINGS_ENABLED] = preferences.newListingsEnabled
            prefs[PreferenceKeys.MESSAGES_ENABLED] = preferences.messagesEnabled
            prefs[PreferenceKeys.VIEWING_REMINDERS_ENABLED] = preferences.viewingRemindersEnabled
            prefs[PreferenceKeys.PROPERTY_STATUS_ENABLED] = preferences.propertyStatusEnabled
            prefs[PreferenceKeys.SAVED_SEARCH_ALERTS_ENABLED] = preferences.savedSearchAlertsEnabled
            prefs[PreferenceKeys.PROMOTIONAL_ENABLED] = preferences.promotionalEnabled
            prefs[PreferenceKeys.SYSTEM_ANNOUNCEMENTS_ENABLED] = preferences.systemAnnouncementsEnabled
            prefs[PreferenceKeys.EMAIL_NOTIFICATIONS_ENABLED] = preferences.emailNotificationsEnabled
            prefs[PreferenceKeys.NOTIFICATION_EMAIL] = preferences.notificationEmail ?: ""
            prefs[PreferenceKeys.QUIET_HOURS_START] = preferences.quietHoursStart ?: -1
            prefs[PreferenceKeys.QUIET_HOURS_END] = preferences.quietHoursEnd ?: -1
            prefs[PreferenceKeys.SOUND_ENABLED] = preferences.soundEnabled
            prefs[PreferenceKeys.VIBRATION_ENABLED] = preferences.vibrationEnabled
            prefs[PreferenceKeys.SHOW_ON_LOCK_SCREEN] = preferences.showOnLockScreen
            prefs[PreferenceKeys.MIN_PRICE_DROP_PERCENTAGE] = preferences.minPriceDropPercentage
        }
    }

    suspend fun clearPreferences() {
        context.notificationPreferencesDataStore.edit { it.clear() }
    }
}
