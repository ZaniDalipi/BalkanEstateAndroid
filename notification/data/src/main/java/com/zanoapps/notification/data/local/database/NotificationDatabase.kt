package com.zanoapps.notification.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zanoapps.notification.data.local.dao.NotificationDao
import com.zanoapps.notification.data.local.entity.NotificationEntity

/**
 * Room database for notifications.
 */
@Database(
    entities = [NotificationEntity::class],
    version = 1,
    exportSchema = true
)
abstract class NotificationDatabase : RoomDatabase() {
    abstract val notificationDao: NotificationDao

    companion object {
        const val DATABASE_NAME = "notification_database"
    }
}
