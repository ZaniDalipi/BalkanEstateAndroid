package com.zanoapps.core.data.repository

import com.zanoapps.core.data.mappers.toDomain
import com.zanoapps.core.data.mappers.toEntities
import com.zanoapps.core.data.remote.NotificationApiService
import com.zanoapps.core.database.dao.NotificationDao
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.notification.domain.model.Notification
import com.zanoapps.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Single Source of Truth Implementation for Notifications.
 *
 * Room database is the single source of truth.
 * Data is synced with MongoDB API.
 * UI observes Room database through Flow.
 */
class NotificationRepositorySSOTImpl(
    private val notificationDao: NotificationDao,
    private val notificationApiService: NotificationApiService
) : NotificationRepository {

    /**
     * Get all notifications from Room (single source of truth).
     */
    override fun getNotifications(): Flow<List<Notification>> {
        return notificationDao.getAllNotifications().map { entities ->
            entities.map { entity -> entity.toDomain() }
        }
    }

    /**
     * Mark a notification as read - update locally and sync.
     */
    override suspend fun markAsRead(notificationId: String): EmptyResult<DataError.Network> {
        // Update locally first
        notificationDao.markAsRead(notificationId)

        // Sync with server
        return when (val result = notificationApiService.markAsRead(notificationId)) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(result.error)
        }
    }

    /**
     * Mark all notifications as read.
     */
    override suspend fun markAllAsRead(): EmptyResult<DataError.Network> {
        // Update locally first
        notificationDao.markAllAsRead()

        // Sync with server
        return when (val result = notificationApiService.markAllAsRead()) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(result.error)
        }
    }

    /**
     * Delete a notification.
     */
    override suspend fun deleteNotification(notificationId: String): EmptyResult<DataError.Network> {
        // Delete locally first
        notificationDao.deleteNotification(notificationId)

        // Sync with server
        return when (val result = notificationApiService.deleteNotification(notificationId)) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(result.error)
        }
    }

    /**
     * Clear all notifications.
     */
    override suspend fun clearAllNotifications(): EmptyResult<DataError.Network> {
        // Delete locally first
        notificationDao.deleteAllNotifications()

        // Sync with server
        return when (val result = notificationApiService.clearAllNotifications()) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(result.error)
        }
    }

    /**
     * Get unread notification count from Room.
     */
    override fun getUnreadCount(): Flow<Int> {
        return notificationDao.getUnreadCount()
    }

    /**
     * Refresh notifications from remote API.
     */
    suspend fun refreshNotifications(): EmptyResult<DataError.Network> {
        return when (val result = notificationApiService.getNotifications()) {
            is Result.Success -> {
                notificationDao.insertNotifications(result.data.data.toEntities())
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }
}
