package com.zanoapps.notification.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object for notifications from the API.
 */
@Serializable
data class NotificationDto(
    @SerialName("id")
    val id: String,

    @SerialName("title")
    val title: String,

    @SerialName("message")
    val message: String,

    @SerialName("type")
    val type: String,

    @SerialName("priority")
    val priority: String? = null,

    @SerialName("created_at")
    val createdAt: Long,

    @SerialName("is_read")
    val isRead: Boolean? = null,

    @SerialName("property_id")
    val propertyId: String? = null,

    @SerialName("property_title")
    val propertyTitle: String? = null,

    @SerialName("property_image_url")
    val propertyImageUrl: String? = null,

    @SerialName("price")
    val price: Double? = null,

    @SerialName("old_price")
    val oldPrice: Double? = null,

    @SerialName("currency")
    val currency: String? = null,

    @SerialName("deep_link")
    val deepLink: String? = null,

    @SerialName("sender_name")
    val senderName: String? = null,

    @SerialName("sender_avatar_url")
    val senderAvatarUrl: String? = null,

    @SerialName("action_text")
    val actionText: String? = null,

    @SerialName("saved_search_id")
    val savedSearchId: String? = null,

    @SerialName("saved_search_name")
    val savedSearchName: String? = null,

    @SerialName("group_key")
    val groupKey: String? = null,

    @SerialName("is_dismissible")
    val isDismissible: Boolean? = null,

    @SerialName("expires_at")
    val expiresAt: Long? = null
)

/**
 * Response wrapper for notification list.
 */
@Serializable
data class NotificationsResponse(
    @SerialName("notifications")
    val notifications: List<NotificationDto>,

    @SerialName("total_count")
    val totalCount: Int,

    @SerialName("unread_count")
    val unreadCount: Int
)

/**
 * Request body for registering push token.
 */
@Serializable
data class RegisterPushTokenRequest(
    @SerialName("token")
    val token: String,

    @SerialName("platform")
    val platform: String = "android",

    @SerialName("device_id")
    val deviceId: String? = null
)

/**
 * Request body for updating notification preferences.
 */
@Serializable
data class NotificationPreferencesDto(
    @SerialName("push_notifications_enabled")
    val pushNotificationsEnabled: Boolean,

    @SerialName("price_drops_enabled")
    val priceDropsEnabled: Boolean,

    @SerialName("new_listings_enabled")
    val newListingsEnabled: Boolean,

    @SerialName("messages_enabled")
    val messagesEnabled: Boolean,

    @SerialName("viewing_reminders_enabled")
    val viewingRemindersEnabled: Boolean,

    @SerialName("property_status_enabled")
    val propertyStatusEnabled: Boolean,

    @SerialName("saved_search_alerts_enabled")
    val savedSearchAlertsEnabled: Boolean,

    @SerialName("promotional_enabled")
    val promotionalEnabled: Boolean,

    @SerialName("system_announcements_enabled")
    val systemAnnouncementsEnabled: Boolean,

    @SerialName("email_notifications_enabled")
    val emailNotificationsEnabled: Boolean,

    @SerialName("notification_email")
    val notificationEmail: String?,

    @SerialName("quiet_hours_start")
    val quietHoursStart: Int?,

    @SerialName("quiet_hours_end")
    val quietHoursEnd: Int?,

    @SerialName("sound_enabled")
    val soundEnabled: Boolean,

    @SerialName("vibration_enabled")
    val vibrationEnabled: Boolean,

    @SerialName("show_on_lock_screen")
    val showOnLockScreen: Boolean,

    @SerialName("min_price_drop_percentage")
    val minPriceDropPercentage: Int
)
