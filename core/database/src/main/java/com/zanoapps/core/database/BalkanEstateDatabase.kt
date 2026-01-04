package com.zanoapps.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zanoapps.core.database.dao.ConversationDao
import com.zanoapps.core.database.dao.FavoritePropertyDao
import com.zanoapps.core.database.dao.MessageDao
import com.zanoapps.core.database.dao.NotificationDao
import com.zanoapps.core.database.dao.OnboardingPreferencesDao
import com.zanoapps.core.database.dao.PropertyDao
import com.zanoapps.core.database.dao.UserDao
import com.zanoapps.core.database.entity.ConversationEntity
import com.zanoapps.core.database.entity.FavoritePropertyEntity
import com.zanoapps.core.database.entity.MessageEntity
import com.zanoapps.core.database.entity.NotificationEntity
import com.zanoapps.core.database.entity.OnboardingPreferencesEntity
import com.zanoapps.core.database.entity.PropertyEntity
import com.zanoapps.core.database.entity.UserEntity

@Database(
    entities = [
        PropertyEntity::class,
        UserEntity::class,
        FavoritePropertyEntity::class,
        ConversationEntity::class,
        MessageEntity::class,
        NotificationEntity::class,
        OnboardingPreferencesEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class BalkanEstateDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDao
    abstract fun userDao(): UserDao
    abstract fun favoritePropertyDao(): FavoritePropertyDao
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
    abstract fun notificationDao(): NotificationDao
    abstract fun onboardingPreferencesDao(): OnboardingPreferencesDao

    companion object {
        const val DATABASE_NAME = "balkan_estate_db"
    }
}
