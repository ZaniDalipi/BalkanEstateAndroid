package com.zanoapps.notification.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zanoapps.notification.data.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for notification database operations.
 */
@Dao
interface NotificationDao {

    // ============ Insert Operations ============

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    // ============ Query Operations ============

    @Query("SELECT * FROM notifications ORDER BY createdAt DESC")
    fun getAllNotificationsFlow(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications ORDER BY createdAt DESC")
    suspend fun getAllNotifications(): List<NotificationEntity>

    @Query("SELECT * FROM notifications WHERE id = :id")
    suspend fun getNotificationById(id: String): NotificationEntity?

    @Query("SELECT * FROM notifications WHERE isRead = 0 ORDER BY createdAt DESC")
    fun getUnreadNotificationsFlow(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE isRead = 0 ORDER BY createdAt DESC")
    suspend fun getUnreadNotifications(): List<NotificationEntity>

    @Query("SELECT * FROM notifications WHERE type = :type ORDER BY createdAt DESC")
    suspend fun getNotificationsByType(type: String): List<NotificationEntity>

    @Query("SELECT * FROM notifications WHERE propertyId = :propertyId ORDER BY createdAt DESC")
    suspend fun getNotificationsByPropertyId(propertyId: String): List<NotificationEntity>

    @Query("SELECT * FROM notifications WHERE savedSearchId = :savedSearchId ORDER BY createdAt DESC")
    suspend fun getNotificationsBySavedSearchId(savedSearchId: String): List<NotificationEntity>

    @Query("""
        SELECT * FROM notifications
        WHERE (:isRead IS NULL OR isRead = :isRead)
        AND (:type IS NULL OR type = :type)
        AND (:propertyId IS NULL OR propertyId = :propertyId)
        AND (:createdAfter IS NULL OR createdAt >= :createdAfter)
        AND (:createdBefore IS NULL OR createdAt <= :createdBefore)
        AND (:excludeExpired = 0 OR expiresAt IS NULL OR expiresAt > :currentTime)
        ORDER BY createdAt DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getFilteredNotifications(
        isRead: Boolean?,
        type: String?,
        propertyId: String?,
        createdAfter: Long?,
        createdBefore: Long?,
        excludeExpired: Boolean,
        currentTime: Long,
        limit: Int,
        offset: Int
    ): List<NotificationEntity>

    @Query("""
        SELECT * FROM notifications
        WHERE (title LIKE '%' || :query || '%' OR message LIKE '%' || :query || '%')
        ORDER BY createdAt DESC
        LIMIT :limit
    """)
    suspend fun searchNotifications(query: String, limit: Int = 50): List<NotificationEntity>

    // ============ Count Operations ============

    @Query("SELECT COUNT(*) FROM notifications")
    suspend fun getTotalCount(): Int

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    suspend fun getUnreadCount(): Int

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM notifications WHERE type = :type")
    suspend fun getCountByType(type: String): Int

    @Query("SELECT COUNT(*) FROM notifications WHERE type = :type AND isRead = 0")
    suspend fun getUnreadCountByType(type: String): Int

    @Query("SELECT COUNT(*) FROM notifications WHERE priority IN (:priorities) AND isRead = 0")
    suspend fun getHighPriorityUnreadCount(priorities: List<String>): Int

    @Query("SELECT MAX(createdAt) FROM notifications")
    suspend fun getLatestTimestamp(): Long?

    // ============ Update Operations ============

    @Update
    suspend fun updateNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query("UPDATE notifications SET isRead = 1 WHERE id IN (:ids)")
    suspend fun markAsRead(ids: List<String>)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Query("UPDATE notifications SET isRead = 0 WHERE id = :id")
    suspend fun markAsUnread(id: String)

    // ============ Delete Operations ============

    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteNotification(id: String)

    @Query("DELETE FROM notifications WHERE id IN (:ids)")
    suspend fun deleteNotifications(ids: List<String>)

    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()

    @Query("DELETE FROM notifications WHERE isRead = 1")
    suspend fun deleteReadNotifications()

    @Query("DELETE FROM notifications WHERE expiresAt IS NOT NULL AND expiresAt < :currentTime")
    suspend fun deleteExpiredNotifications(currentTime: Long)

    @Query("DELETE FROM notifications WHERE propertyId = :propertyId")
    suspend fun deleteNotificationsByPropertyId(propertyId: String)
}
