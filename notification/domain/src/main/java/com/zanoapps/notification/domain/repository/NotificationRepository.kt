package com.zanoapps.notification.domain.repository

import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.notification.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotifications(): Flow<List<Notification>>
    suspend fun markAsRead(notificationId: String): EmptyResult<DataError.Network>
    suspend fun markAllAsRead(): EmptyResult<DataError.Network>
    suspend fun deleteNotification(notificationId: String): EmptyResult<DataError.Network>
    suspend fun clearAllNotifications(): EmptyResult<DataError.Network>
    fun getUnreadCount(): Flow<Int>
}
