package com.zanoapps.notification.presentation.notifications

sealed interface NotificationAction {
    data object OnLoadNotifications : NotificationAction
    data class OnNotificationClick(val notificationId: String) : NotificationAction
    data class OnDeleteNotification(val notificationId: String) : NotificationAction
    data object OnMarkAllAsRead : NotificationAction
}
