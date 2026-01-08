package com.zanoapps.notification.data.mapper

import com.zanoapps.notification.data.remote.dto.NotificationPreferencesDto
import com.zanoapps.notification.domain.model.NotificationPreferences

/**
 * Mapper functions for notification preferences.
 */

fun NotificationPreferences.toDto(): NotificationPreferencesDto {
    return NotificationPreferencesDto(
        pushNotificationsEnabled = pushNotificationsEnabled,
        priceDropsEnabled = priceDropsEnabled,
        newListingsEnabled = newListingsEnabled,
        messagesEnabled = messagesEnabled,
        viewingRemindersEnabled = viewingRemindersEnabled,
        propertyStatusEnabled = propertyStatusEnabled,
        savedSearchAlertsEnabled = savedSearchAlertsEnabled,
        promotionalEnabled = promotionalEnabled,
        systemAnnouncementsEnabled = systemAnnouncementsEnabled,
        emailNotificationsEnabled = emailNotificationsEnabled,
        notificationEmail = notificationEmail,
        quietHoursStart = quietHoursStart,
        quietHoursEnd = quietHoursEnd,
        soundEnabled = soundEnabled,
        vibrationEnabled = vibrationEnabled,
        showOnLockScreen = showOnLockScreen,
        minPriceDropPercentage = minPriceDropPercentage
    )
}

fun NotificationPreferencesDto.toDomain(): NotificationPreferences {
    return NotificationPreferences(
        pushNotificationsEnabled = pushNotificationsEnabled,
        priceDropsEnabled = priceDropsEnabled,
        newListingsEnabled = newListingsEnabled,
        messagesEnabled = messagesEnabled,
        viewingRemindersEnabled = viewingRemindersEnabled,
        propertyStatusEnabled = propertyStatusEnabled,
        savedSearchAlertsEnabled = savedSearchAlertsEnabled,
        promotionalEnabled = promotionalEnabled,
        systemAnnouncementsEnabled = systemAnnouncementsEnabled,
        emailNotificationsEnabled = emailNotificationsEnabled,
        notificationEmail = notificationEmail,
        quietHoursStart = quietHoursStart,
        quietHoursEnd = quietHoursEnd,
        soundEnabled = soundEnabled,
        vibrationEnabled = vibrationEnabled,
        showOnLockScreen = showOnLockScreen,
        minPriceDropPercentage = minPriceDropPercentage
    )
}
