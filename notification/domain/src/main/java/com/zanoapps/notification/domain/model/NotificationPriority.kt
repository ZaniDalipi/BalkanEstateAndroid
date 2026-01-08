package com.zanoapps.notification.domain.model

/**
 * Priority levels for notifications to determine display behavior.
 */
enum class NotificationPriority {
    /** Low priority - informational only */
    LOW,

    /** Normal priority - standard notifications */
    NORMAL,

    /** High priority - important updates */
    HIGH,

    /** Urgent priority - requires immediate attention */
    URGENT
}
