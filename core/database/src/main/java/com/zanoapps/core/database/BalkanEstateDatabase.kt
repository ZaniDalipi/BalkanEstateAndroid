package com.zanoapps.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zanoapps.core.database.dao.AgentDao
import com.zanoapps.core.database.dao.AIChatMessageDao
import com.zanoapps.core.database.dao.ConversationDao
import com.zanoapps.core.database.dao.FavoritePropertyDao
import com.zanoapps.core.database.dao.MessageDao
import com.zanoapps.core.database.dao.NotificationDao
import com.zanoapps.core.database.dao.OnboardingPreferencesDao
import com.zanoapps.core.database.dao.PropertyDao
import com.zanoapps.core.database.dao.SavedSearchDao
import com.zanoapps.core.database.dao.SearchHistoryDao
import com.zanoapps.core.database.dao.UserDao
import com.zanoapps.core.database.entity.AgentEntity
import com.zanoapps.core.database.entity.AIChatMessageEntity
import com.zanoapps.core.database.entity.ConversationEntity
import com.zanoapps.core.database.entity.FavoritePropertyEntity
import com.zanoapps.core.database.entity.MessageEntity
import com.zanoapps.core.database.entity.NotificationEntity
import com.zanoapps.core.database.entity.OnboardingPreferencesEntity
import com.zanoapps.core.database.entity.PropertyEntity
import com.zanoapps.core.database.entity.SavedSearchEntity
import com.zanoapps.core.database.entity.SearchHistoryEntity
import com.zanoapps.core.database.entity.UserEntity

@Database(
    entities = [
        PropertyEntity::class,
        UserEntity::class,
        FavoritePropertyEntity::class,
        ConversationEntity::class,
        MessageEntity::class,
        NotificationEntity::class,
        OnboardingPreferencesEntity::class,
        AgentEntity::class,
        SavedSearchEntity::class,
        SearchHistoryEntity::class,
        AIChatMessageEntity::class
    ],
    version = 3,
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
    abstract fun agentDao(): AgentDao
    abstract fun savedSearchDao(): SavedSearchDao
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun aiChatMessageDao(): AIChatMessageDao

    companion object {
        const val DATABASE_NAME = "balkan_estate_db"
    }
}
