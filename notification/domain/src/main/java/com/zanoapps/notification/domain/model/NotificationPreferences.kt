package com.zanoapps.notification.domain.model

/**
 * User preferences for notification settings.
 */
data class NotificationPreferences(
    /** Master switch for all push notifications */
    val pushNotificationsEnabled: Boolean = true,

    /** Enable notifications for price drops on saved properties */
    val priceDropsEnabled: Boolean = true,

    /** Enable notifications for new listings matching saved searches */
    val newListingsEnabled: Boolean = true,

    /** Enable notifications for messages from agents */
    val messagesEnabled: Boolean = true,

    /** Enable notifications for viewing reminders */
    val viewingRemindersEnabled: Boolean = true,

    /** Enable notifications for property status changes */
    val propertyStatusEnabled: Boolean = true,

    /** Enable notifications for saved search alerts */
    val savedSearchAlertsEnabled: Boolean = true,

    /** Enable promotional/marketing notifications */
    val promotionalEnabled: Boolean = false,

    /** Enable system announcements */
    val systemAnnouncementsEnabled: Boolean = true,

    /** Enable email notifications */
    val emailNotificationsEnabled: Boolean = false,

    /** Email address for notifications */
    val notificationEmail: String? = null,

    /** Quiet hours start time (hour in 24h format, null = disabled) */
    val quietHoursStart: Int? = null,

    /** Quiet hours end time (hour in 24h format, null = disabled) */
    val quietHoursEnd: Int? = null,

    /** Notification sound enabled */
    val soundEnabled: Boolean = true,

    /** Vibration enabled for notifications */
    val vibrationEnabled: Boolean = true,

    /** Show notification previews on lock screen */
    val showOnLockScreen: Boolean = true,

    /** Minimum price drop percentage to notify (e.g., 5 means notify only for 5%+ drops) */
    val minPriceDropPercentage: Int = 0
) {
    /**
     * Checks if a notification type is enabled based on preferences.
     */
    fun isNotificationTypeEnabled(type: NotificationType): Boolean {
        if (!pushNotificationsEnabled) return false

        return when (type) {
            NotificationType.PRICE_DROP -> priceDropsEnabled
            NotificationType.NEW_LISTING_MATCH -> newListingsEnabled
            NotificationType.SAVED_SEARCH_ALERT -> savedSearchAlertsEnabled
            NotificationType.MESSAGE, NotificationType.AGENT_RESPONSE -> messagesEnabled
            NotificationType.VIEWING_SCHEDULED, NotificationType.VIEWING_REMINDER -> viewingRemindersEnabled
            NotificationType.PROPERTY_STATUS_CHANGE -> propertyStatusEnabled
            NotificationType.PROPERTY_SAVED, NotificationType.FAVORITE_UPDATE -> true
            NotificationType.PROMOTIONAL -> promotionalEnabled
            NotificationType.SYSTEM -> systemAnnouncementsEnabled
        }
    }

    /**
     * Checks if current time is within quiet hours.
     */
    fun isQuietHoursActive(currentHour: Int): Boolean {
        val start = quietHoursStart ?: return false
        val end = quietHoursEnd ?: return false

        return if (start <= end) {
            currentHour in start until end
        } else {
            // Handles overnight quiet hours (e.g., 22:00 to 07:00)
            currentHour >= start || currentHour < end
        }
    }
}
