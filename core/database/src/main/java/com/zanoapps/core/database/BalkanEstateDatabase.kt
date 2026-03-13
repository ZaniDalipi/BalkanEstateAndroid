package com.zanoapps.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zanoapps.core.database.dao.AgencyDao
import com.zanoapps.core.database.dao.AgentDao
import com.zanoapps.core.database.dao.ConversationDao
import com.zanoapps.core.database.dao.FavoriteDao
import com.zanoapps.core.database.dao.MessageDao
import com.zanoapps.core.database.dao.PropertyDao
import com.zanoapps.core.database.dao.SavedSearchDao
import com.zanoapps.core.database.dao.UserDao
import com.zanoapps.core.database.entity.AgencyEntity
import com.zanoapps.core.database.entity.AgentEntity
import com.zanoapps.core.database.entity.ConversationEntity
import com.zanoapps.core.database.entity.FavoritePropertyEntity
import com.zanoapps.core.database.entity.MessageEntity
import com.zanoapps.core.database.entity.PropertyEntity
import com.zanoapps.core.database.entity.SavedSearchEntity
import com.zanoapps.core.database.entity.UserEntity

@Database(
    entities = [
        PropertyEntity::class,
        FavoritePropertyEntity::class,
        SavedSearchEntity::class,
        ConversationEntity::class,
        MessageEntity::class,
        UserEntity::class,
        AgentEntity::class,
        AgencyEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class BalkanEstateDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun savedSearchDao(): SavedSearchDao
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
    abstract fun userDao(): UserDao
    abstract fun agentDao(): AgentDao
    abstract fun agencyDao(): AgencyDao
}
