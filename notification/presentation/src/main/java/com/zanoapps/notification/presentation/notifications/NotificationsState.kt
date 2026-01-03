package com.zanoapps.notification.presentation.notifications

import com.zanoapps.notification.domain.model.Notification

data class NotificationsState(
    val notifications: List<Notification> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val unreadCount: Int = 0
)
