package com.zanoapps.notification.presentation.notifications

sealed interface NotificationsAction {
    data object OnBackClick : NotificationsAction
    data class OnNotificationClick(val notificationId: String) : NotificationsAction
    data class OnDeleteNotification(val notificationId: String) : NotificationsAction
    data object OnMarkAllAsRead : NotificationsAction
    data object OnClearAll : NotificationsAction
    data object OnRefresh : NotificationsAction
}
