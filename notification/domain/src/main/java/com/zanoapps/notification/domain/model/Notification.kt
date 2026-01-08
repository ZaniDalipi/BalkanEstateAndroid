package com.zanoapps.notification.domain.model

/**
 * Main domain model representing a notification in the BalkanEstate app.
 */
data class Notification(
    /** Unique identifier for the notification */
    val id: String,

    /** Title of the notification */
    val title: String,

    /** Main body/message of the notification */
    val message: String,

    /** Type of notification for categorization and display */
    val type: NotificationType,

    /** Priority level for the notification */
    val priority: NotificationPriority = NotificationPriority.NORMAL,

    /** Timestamp when the notification was created (epoch millis) */
    val createdAt: Long,

    /** Whether the notification has been read by the user */
    val isRead: Boolean = false,

    /** Optional property ID if notification is related to a property */
    val propertyId: String? = null,

    /** Optional property title for display purposes */
    val propertyTitle: String? = null,

    /** Optional property image URL for rich display */
    val propertyImageUrl: String? = null,

    /** Optional price value (e.g., for price drop notifications) */
    val price: Double? = null,

    /** Optional old price value (e.g., for price drop comparison) */
    val oldPrice: Double? = null,

    /** Optional currency code */
    val currency: String? = null,

    /** Optional deep link for navigation */
    val deepLink: String? = null,

    /** Optional sender name (e.g., agent name for messages) */
    val senderName: String? = null,

    /** Optional sender avatar URL */
    val senderAvatarUrl: String? = null,

    /** Optional action button text */
    val actionText: String? = null,

    /** Optional saved search ID if related to a saved search */
    val savedSearchId: String? = null,

    /** Optional saved search name */
    val savedSearchName: String? = null,

    /** Group key for notification grouping */
    val groupKey: String? = null,

    /** Whether the notification can be dismissed */
    val isDismissible: Boolean = true,

    /** Expiration timestamp (epoch millis), null means no expiration */
    val expiresAt: Long? = null
) {
    /**
     * Returns the percentage price drop if applicable.
     */
    fun getPriceDropPercentage(): Int? {
        if (oldPrice == null || price == null || oldPrice <= 0) return null
        return ((oldPrice - price) / oldPrice * 100).toInt()
    }

    /**
     * Returns true if the notification has expired.
     */
    fun isExpired(): Boolean {
        return expiresAt?.let { it < System.currentTimeMillis() } ?: false
    }

    /**
     * Returns true if this notification is related to a property.
     */
    fun hasPropertyContext(): Boolean = propertyId != null

    /**
     * Returns true if this notification is a message type.
     */
    fun isMessage(): Boolean = type == NotificationType.MESSAGE || type == NotificationType.AGENT_RESPONSE
}
