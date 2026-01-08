package com.zanoapps.notification.domain.model

/**
 * Filter options for querying notifications.
 */
data class NotificationFilter(
    /** Filter by notification types */
    val types: Set<NotificationType>? = null,

    /** Filter by read status (null = all, true = read only, false = unread only) */
    val isRead: Boolean? = null,

    /** Filter by property ID */
    val propertyId: String? = null,

    /** Filter by saved search ID */
    val savedSearchId: String? = null,

    /** Filter notifications created after this timestamp */
    val createdAfter: Long? = null,

    /** Filter notifications created before this timestamp */
    val createdBefore: Long? = null,

    /** Filter by priority levels */
    val priorities: Set<NotificationPriority>? = null,

    /** Search query for title/message */
    val searchQuery: String? = null,

    /** Exclude expired notifications */
    val excludeExpired: Boolean = true,

    /** Maximum number of results */
    val limit: Int? = null,

    /** Offset for pagination */
    val offset: Int = 0
) {
    companion object {
        /** Filter for unread notifications only */
        val UNREAD_ONLY = NotificationFilter(isRead = false)

        /** Filter for all notifications without limit */
        val ALL = NotificationFilter()

        /** Filter for message-type notifications */
        val MESSAGES_ONLY = NotificationFilter(
            types = setOf(NotificationType.MESSAGE, NotificationType.AGENT_RESPONSE)
        )

        /** Filter for property-related notifications */
        val PROPERTY_UPDATES = NotificationFilter(
            types = setOf(
                NotificationType.PRICE_DROP,
                NotificationType.PROPERTY_STATUS_CHANGE,
                NotificationType.FAVORITE_UPDATE,
                NotificationType.NEW_LISTING_MATCH
            )
        )

        /** Filter for high priority notifications */
        val HIGH_PRIORITY = NotificationFilter(
            priorities = setOf(NotificationPriority.HIGH, NotificationPriority.URGENT)
        )
    }
}

/**
 * Sort options for notifications.
 */
enum class NotificationSortOption {
    /** Sort by creation date, newest first */
    NEWEST_FIRST,

    /** Sort by creation date, oldest first */
    OLDEST_FIRST,

    /** Sort by priority (urgent first), then by date */
    PRIORITY,

    /** Sort by read status (unread first), then by date */
    UNREAD_FIRST,

    /** Sort by type, then by date */
    TYPE
}
