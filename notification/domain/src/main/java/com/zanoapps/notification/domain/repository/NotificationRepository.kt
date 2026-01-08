package com.zanoapps.notification.domain.repository

import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.notification.domain.model.Notification
import com.zanoapps.notification.domain.model.NotificationError
import com.zanoapps.notification.domain.model.NotificationFilter
import com.zanoapps.notification.domain.model.NotificationPreferences
import com.zanoapps.notification.domain.model.NotificationSortOption
import com.zanoapps.notification.domain.model.NotificationSummary
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for notification operations.
 */
interface NotificationRepository {

    // ============ Notification CRUD Operations ============

    /**
     * Get all notifications as a Flow for real-time updates.
     */
    fun getNotificationsFlow(
        filter: NotificationFilter = NotificationFilter.ALL,
        sortOption: NotificationSortOption = NotificationSortOption.NEWEST_FIRST
    ): Flow<List<Notification>>

    /**
     * Get notifications with filter and sort options.
     */
    suspend fun getNotifications(
        filter: NotificationFilter = NotificationFilter.ALL,
        sortOption: NotificationSortOption = NotificationSortOption.NEWEST_FIRST
    ): Result<List<Notification>, NotificationError>

    /**
     * Get a single notification by ID.
     */
    suspend fun getNotificationById(id: String): Result<Notification, NotificationError>

    /**
     * Refresh notifications from remote source.
     */
    suspend fun refreshNotifications(): EmptyResult<NotificationError>

    /**
     * Delete a notification by ID.
     */
    suspend fun deleteNotification(id: String): EmptyResult<NotificationError>

    /**
     * Delete multiple notifications.
     */
    suspend fun deleteNotifications(ids: List<String>): EmptyResult<NotificationError>

    /**
     * Delete all notifications.
     */
    suspend fun deleteAllNotifications(): EmptyResult<NotificationError>

    /**
     * Delete all read notifications.
     */
    suspend fun deleteReadNotifications(): EmptyResult<NotificationError>

    // ============ Read Status Operations ============

    /**
     * Mark a notification as read.
     */
    suspend fun markAsRead(id: String): EmptyResult<NotificationError>

    /**
     * Mark multiple notifications as read.
     */
    suspend fun markAsRead(ids: List<String>): EmptyResult<NotificationError>

    /**
     * Mark all notifications as read.
     */
    suspend fun markAllAsRead(): EmptyResult<NotificationError>

    /**
     * Mark a notification as unread.
     */
    suspend fun markAsUnread(id: String): EmptyResult<NotificationError>

    // ============ Summary & Counts ============

    /**
     * Get notification summary/statistics.
     */
    suspend fun getNotificationSummary(): Result<NotificationSummary, NotificationError>

    /**
     * Get unread notification count as a Flow for real-time badge updates.
     */
    fun getUnreadCountFlow(): Flow<Int>

    /**
     * Get unread notification count.
     */
    suspend fun getUnreadCount(): Result<Int, NotificationError>

    // ============ Preferences ============

    /**
     * Get notification preferences.
     */
    suspend fun getPreferences(): Result<NotificationPreferences, NotificationError>

    /**
     * Get notification preferences as Flow.
     */
    fun getPreferencesFlow(): Flow<NotificationPreferences>

    /**
     * Update notification preferences.
     */
    suspend fun updatePreferences(preferences: NotificationPreferences): EmptyResult<NotificationError>

    // ============ Push Notification Token ============

    /**
     * Register FCM token for push notifications.
     */
    suspend fun registerPushToken(token: String): EmptyResult<NotificationError>

    /**
     * Unregister FCM token (e.g., on logout).
     */
    suspend fun unregisterPushToken(): EmptyResult<NotificationError>

    // ============ Local Operations ============

    /**
     * Save a notification locally (e.g., from push).
     */
    suspend fun saveNotificationLocally(notification: Notification): EmptyResult<NotificationError>

    /**
     * Clear expired notifications.
     */
    suspend fun clearExpiredNotifications(): EmptyResult<NotificationError>
}
