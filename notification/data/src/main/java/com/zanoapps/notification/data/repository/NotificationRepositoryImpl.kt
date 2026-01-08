package com.zanoapps.notification.data.repository

import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.notification.data.local.dao.NotificationDao
import com.zanoapps.notification.data.local.preferences.NotificationPreferencesDataStore
import com.zanoapps.notification.data.mapper.toDomain
import com.zanoapps.notification.data.mapper.toEntity
import com.zanoapps.notification.domain.model.Notification
import com.zanoapps.notification.domain.model.NotificationError
import com.zanoapps.notification.domain.model.NotificationFilter
import com.zanoapps.notification.domain.model.NotificationPreferences
import com.zanoapps.notification.domain.model.NotificationPriority
import com.zanoapps.notification.domain.model.NotificationSortOption
import com.zanoapps.notification.domain.model.NotificationSummary
import com.zanoapps.notification.domain.model.NotificationType
import com.zanoapps.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Implementation of NotificationRepository using Room for local storage
 * and DataStore for preferences.
 */
class NotificationRepositoryImpl(
    private val notificationDao: NotificationDao,
    private val preferencesDataStore: NotificationPreferencesDataStore
) : NotificationRepository {

    // ============ Notification CRUD Operations ============

    override fun getNotificationsFlow(
        filter: NotificationFilter,
        sortOption: NotificationSortOption
    ): Flow<List<Notification>> {
        return when {
            filter.isRead == false -> notificationDao.getUnreadNotificationsFlow()
            else -> notificationDao.getAllNotificationsFlow()
        }.map { entities ->
            var notifications = entities.toDomain()

            // Apply additional filters
            notifications = applyFilters(notifications, filter)

            // Apply sorting
            notifications = applySorting(notifications, sortOption)

            notifications
        }
    }

    override suspend fun getNotifications(
        filter: NotificationFilter,
        sortOption: NotificationSortOption
    ): Result<List<Notification>, NotificationError> {
        return try {
            val entities = notificationDao.getFilteredNotifications(
                isRead = filter.isRead,
                type = filter.types?.firstOrNull()?.name,
                propertyId = filter.propertyId,
                createdAfter = filter.createdAfter,
                createdBefore = filter.createdBefore,
                excludeExpired = filter.excludeExpired,
                currentTime = System.currentTimeMillis(),
                limit = filter.limit ?: Int.MAX_VALUE,
                offset = filter.offset
            )

            var notifications = entities.toDomain()
            notifications = applyFilters(notifications, filter)
            notifications = applySorting(notifications, sortOption)

            Result.Success(notifications)
        } catch (e: Exception) {
            Result.Error(NotificationError.Local.DATABASE_ERROR)
        }
    }

    override suspend fun getNotificationById(id: String): Result<Notification, NotificationError> {
        return try {
            val entity = notificationDao.getNotificationById(id)
            if (entity != null) {
                Result.Success(entity.toDomain())
            } else {
                Result.Error(NotificationError.Local.NOT_FOUND)
            }
        } catch (e: Exception) {
            Result.Error(NotificationError.Local.DATABASE_ERROR)
        }
    }

    override suspend fun refreshNotifications(): EmptyResult<NotificationError> {
        // In a real implementation, this would fetch from remote API
        // For now, we just return success
        return Result.Success(Unit)
    }

    override suspend fun deleteNotification(id: String): EmptyResult<NotificationError> {
        return try {
            notificationDao.deleteNotification(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NotificationError.Local.DATABASE_ERROR)
        }
    }

    override suspend fun deleteNotifications(ids: List<String>): EmptyResult<NotificationError> {
        return try {
            notificationDao.deleteNotifications(ids)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NotificationError.Local.DATABASE_ERROR)
        }
    }

    override suspend fun deleteAllNotifications(): EmptyResult<NotificationError> {
        return try {
            notificationDao.deleteAllNotifications()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NotificationError.Local.DATABASE_ERROR)
        }
    }

    override suspend fun deleteReadNotifications(): EmptyResult<NotificationError> {
        return try {
            notificationDao.deleteReadNotifications()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NotificationError.Local.DATABASE_ERROR)
        }
    }

    // ============ Read Status Operations ============

    override suspend fun markAsRead(id: String): EmptyResult<NotificationError> {
        return try {
            notificationDao.markAsRead(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NotificationError.Local.DATABASE_ERROR)
        }
    }

    override suspend fun markAsRead(ids: List<String>): EmptyResult<NotificationError> {
        return try {
            notificationDao.markAsRead(ids)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NotificationError.Local.DATABASE_ERROR)
        }
    }

    override suspend fun markAllAsRead(): EmptyResult<NotificationError> {
        return try {
            notificationDao.markAllAsRead()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NotificationError.Local.DATABASE_ERROR)
        }
    }

    override suspend fun markAsUnread(id: String): EmptyResult<NotificationError> {
        return try {
            notificationDao.markAsUnread(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NotificationError.Local.DATABASE_ERROR)
        }
    }

    // ============ Summary & Counts ============

    override suspend fun getNotificationSummary(): Result<NotificationSummary, NotificationError> {
        return try {
            val totalCount = notificationDao.getTotalCount()
            val unreadCount = notificationDao.getUnreadCount()
            val latestTimestamp = notificationDao.getLatestTimestamp()

            val countByType = mutableMapOf<NotificationType, Int>()
            val unreadCountByType = mutableMapOf<NotificationType, Int>()

            NotificationType.entries.forEach { type ->
                countByType[type] = notificationDao.getCountByType(type.name)
                unreadCountByType[type] = notificationDao.getUnreadCountByType(type.name)
            }

            val highPriorityUnreadCount = notificationDao.getHighPriorityUnreadCount(
                listOf(NotificationPriority.HIGH.name, NotificationPriority.URGENT.name)
            )

            Result.Success(
                NotificationSummary(
                    totalCount = totalCount,
                    unreadCount = unreadCount,
                    countByType = countByType,
                    unreadCountByType = unreadCountByType,
                    highPriorityUnreadCount = highPriorityUnreadCount,
                    latestNotificationTimestamp = latestTimestamp
                )
            )
        } catch (e: Exception) {
            Result.Error(NotificationError.Local.DATABASE_ERROR)
        }
    }

    override fun getUnreadCountFlow(): Flow<Int> {
        return notificationDao.getUnreadCountFlow()
    }

    override suspend fun getUnreadCount(): Result<Int, NotificationError> {
        return try {
            Result.Success(notificationDao.getUnreadCount())
        } catch (e: Exception) {
            Result.Error(NotificationError.Local.DATABASE_ERROR)
        }
    }

    // ============ Preferences ============

    override suspend fun getPreferences(): Result<NotificationPreferences, NotificationError> {
        return try {
            val preferences = preferencesDataStore.preferencesFlow.first()
            Result.Success(preferences)
        } catch (e: Exception) {
            Result.Error(NotificationError.Local.DATABASE_ERROR)
        }
    }

    override fun getPreferencesFlow(): Flow<NotificationPreferences> {
        return preferencesDataStore.preferencesFlow
    }

    override suspend fun updatePreferences(preferences: NotificationPreferences): EmptyResult<NotificationError> {
        return try {
            preferencesDataStore.updatePreferences(preferences)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NotificationError.Local.DATABASE_ERROR)
        }
    }

    // ============ Push Notification Token ============

    override suspend fun registerPushToken(token: String): EmptyResult<NotificationError> {
        // In a real implementation, this would send the token to the backend
        // For now, we just return success
        return Result.Success(Unit)
    }

    override suspend fun unregisterPushToken(): EmptyResult<NotificationError> {
        // In a real implementation, this would unregister the token from the backend
        return Result.Success(Unit)
    }

    // ============ Local Operations ============

    override suspend fun saveNotificationLocally(notification: Notification): EmptyResult<NotificationError> {
        return try {
            notificationDao.insertNotification(notification.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NotificationError.Local.DATABASE_ERROR)
        }
    }

    override suspend fun clearExpiredNotifications(): EmptyResult<NotificationError> {
        return try {
            notificationDao.deleteExpiredNotifications(System.currentTimeMillis())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NotificationError.Local.DATABASE_ERROR)
        }
    }

    // ============ Private Helper Methods ============

    private fun applyFilters(
        notifications: List<Notification>,
        filter: NotificationFilter
    ): List<Notification> {
        var result = notifications

        // Filter by types
        filter.types?.let { types ->
            result = result.filter { it.type in types }
        }

        // Filter by priorities
        filter.priorities?.let { priorities ->
            result = result.filter { it.priority in priorities }
        }

        // Filter by search query
        filter.searchQuery?.let { query ->
            if (query.isNotBlank()) {
                val lowerQuery = query.lowercase()
                result = result.filter {
                    it.title.lowercase().contains(lowerQuery) ||
                            it.message.lowercase().contains(lowerQuery)
                }
            }
        }

        // Filter by saved search ID
        filter.savedSearchId?.let { savedSearchId ->
            result = result.filter { it.savedSearchId == savedSearchId }
        }

        return result
    }

    private fun applySorting(
        notifications: List<Notification>,
        sortOption: NotificationSortOption
    ): List<Notification> {
        return when (sortOption) {
            NotificationSortOption.NEWEST_FIRST ->
                notifications.sortedByDescending { it.createdAt }

            NotificationSortOption.OLDEST_FIRST ->
                notifications.sortedBy { it.createdAt }

            NotificationSortOption.PRIORITY ->
                notifications.sortedWith(
                    compareByDescending<Notification> { it.priority.ordinal }
                        .thenByDescending { it.createdAt }
                )

            NotificationSortOption.UNREAD_FIRST ->
                notifications.sortedWith(
                    compareBy<Notification> { it.isRead }
                        .thenByDescending { it.createdAt }
                )

            NotificationSortOption.TYPE ->
                notifications.sortedWith(
                    compareBy<Notification> { it.type.ordinal }
                        .thenByDescending { it.createdAt }
                )
        }
    }
}
