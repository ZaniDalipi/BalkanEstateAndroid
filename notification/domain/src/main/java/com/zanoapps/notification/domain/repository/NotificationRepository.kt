package com.zanoapps.notification.domain.repository

import com.zanoapps.notification.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotifications(): Flow<List<Notification>>
    suspend fun markAsRead(notificationId: String)
    suspend fun markAllAsRead()
    suspend fun deleteNotification(notificationId: String)
    suspend fun getUnreadCount(): Int
}
