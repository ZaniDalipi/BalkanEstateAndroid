package com.zanoapps.notification.domain.model

/**
 * Represents a group of notifications for display purposes.
 */
data class NotificationGroup(
    /** Unique identifier for the group */
    val groupKey: String,

    /** Display title for the group */
    val title: String,

    /** Notifications in this group */
    val notifications: List<Notification>,

    /** Type of notifications in this group */
    val type: NotificationType,

    /** Timestamp of the most recent notification in the group */
    val latestTimestamp: Long
) {
    /** Number of unread notifications in the group */
    val unreadCount: Int get() = notifications.count { !it.isRead }

    /** Total count of notifications in the group */
    val totalCount: Int get() = notifications.size

    /** Whether all notifications in the group are read */
    val isAllRead: Boolean get() = notifications.all { it.isRead }
}

/**
 * Represents notification statistics/summary.
 */
data class NotificationSummary(
    /** Total number of notifications */
    val totalCount: Int,

    /** Number of unread notifications */
    val unreadCount: Int,

    /** Count by notification type */
    val countByType: Map<NotificationType, Int>,

    /** Unread count by notification type */
    val unreadCountByType: Map<NotificationType, Int>,

    /** Count of high priority unread notifications */
    val highPriorityUnreadCount: Int,

    /** Timestamp of the most recent notification */
    val latestNotificationTimestamp: Long?
) {
    /** Whether there are any unread notifications */
    val hasUnread: Boolean get() = unreadCount > 0

    /** Whether there are any high priority unread notifications */
    val hasHighPriorityUnread: Boolean get() = highPriorityUnreadCount > 0
}
