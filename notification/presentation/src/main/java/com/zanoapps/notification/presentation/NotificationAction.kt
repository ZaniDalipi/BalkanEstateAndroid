package com.zanoapps.notification.presentation

import com.zanoapps.notification.domain.model.Notification
import com.zanoapps.notification.domain.model.NotificationFilter
import com.zanoapps.notification.domain.model.NotificationPreferences
import com.zanoapps.notification.domain.model.NotificationSortOption

/**
 * User actions for notification screens.
 */
sealed interface NotificationAction {

    // ============ Navigation Actions ============

    /** Navigate to notification details */
    data class OnNotificationClick(val notification: Notification) : NotificationAction

    /** Navigate to related property */
    data class OnPropertyClick(val propertyId: String) : NotificationAction

    /** Navigate to related saved search */
    data class OnSavedSearchClick(val savedSearchId: String) : NotificationAction

    /** Navigate to message conversation */
    data class OnMessageClick(val notificationId: String) : NotificationAction

    /** Navigate back */
    data object OnBackClick : NotificationAction

    /** Open notification settings */
    data object OnOpenSettings : NotificationAction

    /** Close notification settings */
    data object OnCloseSettings : NotificationAction

    // ============ List Actions ============

    /** Refresh notifications */
    data object OnRefresh : NotificationAction

    /** Load more notifications (pagination) */
    data object OnLoadMore : NotificationAction

    /** Pull to refresh gesture */
    data object OnPullToRefresh : NotificationAction

    // ============ Selection Actions ============

    /** Toggle selection mode */
    data object OnToggleSelectionMode : NotificationAction

    /** Select/deselect a notification */
    data class OnToggleSelection(val notificationId: String) : NotificationAction

    /** Select all notifications */
    data object OnSelectAll : NotificationAction

    /** Deselect all notifications */
    data object OnDeselectAll : NotificationAction

    // ============ Read/Unread Actions ============

    /** Mark a notification as read */
    data class OnMarkAsRead(val notificationId: String) : NotificationAction

    /** Mark a notification as unread */
    data class OnMarkAsUnread(val notificationId: String) : NotificationAction

    /** Mark selected notifications as read */
    data object OnMarkSelectedAsRead : NotificationAction

    /** Mark all notifications as read */
    data object OnMarkAllAsRead : NotificationAction

    /** Confirm mark all as read */
    data object OnConfirmMarkAllAsRead : NotificationAction

    /** Cancel mark all as read confirmation */
    data object OnCancelMarkAllAsRead : NotificationAction

    // ============ Delete Actions ============

    /** Delete a notification */
    data class OnDeleteNotification(val notificationId: String) : NotificationAction

    /** Delete selected notifications */
    data object OnDeleteSelected : NotificationAction

    /** Delete all notifications */
    data object OnDeleteAll : NotificationAction

    /** Delete all read notifications */
    data object OnDeleteReadNotifications : NotificationAction

    /** Confirm delete action */
    data object OnConfirmDelete : NotificationAction

    /** Cancel delete confirmation */
    data object OnCancelDelete : NotificationAction

    // ============ Filter/Sort Actions ============

    /** Change filter tab */
    data class OnFilterTabChange(val tab: NotificationFilterTab) : NotificationAction

    /** Apply advanced filter */
    data class OnApplyFilter(val filter: NotificationFilter) : NotificationAction

    /** Clear filters */
    data object OnClearFilters : NotificationAction

    /** Change sort option */
    data class OnSortChange(val sortOption: NotificationSortOption) : NotificationAction

    /** Show filter sheet */
    data object OnShowFilterSheet : NotificationAction

    /** Hide filter sheet */
    data object OnHideFilterSheet : NotificationAction

    /** Show sort sheet */
    data object OnShowSortSheet : NotificationAction

    /** Hide sort sheet */
    data object OnHideSortSheet : NotificationAction

    // ============ Preference Actions ============

    /** Toggle push notifications */
    data class OnTogglePushNotifications(val enabled: Boolean) : NotificationAction

    /** Toggle price drop notifications */
    data class OnTogglePriceDrops(val enabled: Boolean) : NotificationAction

    /** Toggle new listings notifications */
    data class OnToggleNewListings(val enabled: Boolean) : NotificationAction

    /** Toggle message notifications */
    data class OnToggleMessages(val enabled: Boolean) : NotificationAction

    /** Toggle viewing reminders */
    data class OnToggleViewingReminders(val enabled: Boolean) : NotificationAction

    /** Toggle property status notifications */
    data class OnTogglePropertyStatus(val enabled: Boolean) : NotificationAction

    /** Toggle saved search alerts */
    data class OnToggleSavedSearchAlerts(val enabled: Boolean) : NotificationAction

    /** Toggle promotional notifications */
    data class OnTogglePromotional(val enabled: Boolean) : NotificationAction

    /** Toggle system announcements */
    data class OnToggleSystemAnnouncements(val enabled: Boolean) : NotificationAction

    /** Toggle email notifications */
    data class OnToggleEmailNotifications(val enabled: Boolean) : NotificationAction

    /** Update notification email */
    data class OnUpdateEmail(val email: String) : NotificationAction

    /** Toggle sound */
    data class OnToggleSound(val enabled: Boolean) : NotificationAction

    /** Toggle vibration */
    data class OnToggleVibration(val enabled: Boolean) : NotificationAction

    /** Toggle show on lock screen */
    data class OnToggleLockScreen(val enabled: Boolean) : NotificationAction

    /** Set quiet hours */
    data class OnSetQuietHours(val start: Int?, val end: Int?) : NotificationAction

    /** Set minimum price drop percentage */
    data class OnSetMinPriceDropPercentage(val percentage: Int) : NotificationAction

    /** Update full preferences */
    data class OnUpdatePreferences(val preferences: NotificationPreferences) : NotificationAction

    /** Save preferences */
    data object OnSavePreferences : NotificationAction

    /** Reset preferences to defaults */
    data object OnResetPreferences : NotificationAction

    // ============ Swipe Actions ============

    /** Swipe to delete */
    data class OnSwipeToDelete(val notificationId: String) : NotificationAction

    /** Swipe to mark as read/unread */
    data class OnSwipeToToggleRead(val notificationId: String) : NotificationAction

    // ============ Error Handling ============

    /** Dismiss error */
    data object OnDismissError : NotificationAction

    /** Retry failed operation */
    data object OnRetry : NotificationAction
}
