package com.zanoapps.notification.presentation

import com.zanoapps.notification.domain.model.Notification
import com.zanoapps.notification.domain.model.NotificationFilter
import com.zanoapps.notification.domain.model.NotificationPreferences
import com.zanoapps.notification.domain.model.NotificationSortOption
import com.zanoapps.notification.domain.model.NotificationSummary
import com.zanoapps.notification.domain.model.NotificationType

/**
 * UI state for the notification screens.
 */
data class NotificationState(
    // Notifications list
    val notifications: List<Notification> = emptyList(),

    // Loading states
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,

    // Error state
    val errorMessage: String? = null,

    // Selected notification for details
    val selectedNotification: Notification? = null,

    // Filter and sort
    val currentFilter: NotificationFilter = NotificationFilter.ALL,
    val currentSortOption: NotificationSortOption = NotificationSortOption.NEWEST_FIRST,
    val selectedFilterTab: NotificationFilterTab = NotificationFilterTab.ALL,

    // Summary/counts
    val summary: NotificationSummary? = null,
    val unreadCount: Int = 0,

    // Preferences
    val preferences: NotificationPreferences = NotificationPreferences(),
    val isLoadingPreferences: Boolean = false,
    val isSavingPreferences: Boolean = false,

    // UI state
    val isSelectionMode: Boolean = false,
    val selectedNotificationIds: Set<String> = emptySet(),
    val showDeleteConfirmation: Boolean = false,
    val showMarkAllReadConfirmation: Boolean = false,
    val showFilterSheet: Boolean = false,
    val showSortSheet: Boolean = false,
    val showSettingsSheet: Boolean = false,

    // Pagination
    val hasMoreNotifications: Boolean = true,
    val currentPage: Int = 0
) {
    val hasNotifications: Boolean get() = notifications.isNotEmpty()
    val hasUnread: Boolean get() = unreadCount > 0
    val selectedCount: Int get() = selectedNotificationIds.size
    val isAllSelected: Boolean get() = selectedNotificationIds.size == notifications.size && notifications.isNotEmpty()

    fun getFilteredNotifications(): List<Notification> {
        return when (selectedFilterTab) {
            NotificationFilterTab.ALL -> notifications
            NotificationFilterTab.UNREAD -> notifications.filter { !it.isRead }
            NotificationFilterTab.MESSAGES -> notifications.filter { it.isMessage() }
            NotificationFilterTab.PROPERTY_UPDATES -> notifications.filter {
                it.type in setOf(
                    NotificationType.PRICE_DROP,
                    NotificationType.PROPERTY_STATUS_CHANGE,
                    NotificationType.NEW_LISTING_MATCH,
                    NotificationType.FAVORITE_UPDATE
                )
            }
        }
    }
}

/**
 * Tab options for filtering notifications.
 */
enum class NotificationFilterTab(val title: String) {
    ALL("All"),
    UNREAD("Unread"),
    MESSAGES("Messages"),
    PROPERTY_UPDATES("Property Updates")
}
