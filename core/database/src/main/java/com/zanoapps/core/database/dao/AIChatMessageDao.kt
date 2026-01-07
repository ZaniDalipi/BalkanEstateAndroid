package com.zanoapps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zanoapps.core.database.entity.AIChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AIChatMessageDao {

    @Query("SELECT * FROM ai_chat_messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getMessagesBySession(sessionId: String): Flow<List<AIChatMessageEntity>>

    @Query("SELECT * FROM ai_chat_messages WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAllMessagesForUser(userId: String): Flow<List<AIChatMessageEntity>>

    @Query("SELECT DISTINCT sessionId FROM ai_chat_messages WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentSessionIds(userId: String, limit: Int = 10): List<String>

    @Query("SELECT * FROM ai_chat_messages WHERE userId = :userId AND sessionId = (SELECT sessionId FROM ai_chat_messages WHERE userId = :userId ORDER BY timestamp DESC LIMIT 1) ORDER BY timestamp ASC")
    fun getLatestSession(userId: String): Flow<List<AIChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: AIChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<AIChatMessageEntity>)

    @Query("DELETE FROM ai_chat_messages WHERE sessionId = :sessionId")
    suspend fun deleteSession(sessionId: String)

    @Query("DELETE FROM ai_chat_messages WHERE userId = :userId")
    suspend fun deleteAllMessagesForUser(userId: String)

    @Query("DELETE FROM ai_chat_messages WHERE timestamp < :timestamp")
    suspend fun deleteOldMessages(timestamp: Long)

    @Query("SELECT COUNT(*) FROM ai_chat_messages WHERE sessionId = :sessionId")
    suspend fun getMessageCountInSession(sessionId: String): Int
}
