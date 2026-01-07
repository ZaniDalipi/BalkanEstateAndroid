package com.zanoapps.search.domain.repository

import com.zanoapps.search.domain.model.AIChatMessage
import kotlinx.coroutines.flow.Flow

interface AIChatRepository {
    fun getMessagesBySession(sessionId: String): Flow<List<AIChatMessage>>
    fun getLatestSession(userId: String): Flow<List<AIChatMessage>>
    fun getAllMessagesForUser(userId: String): Flow<List<AIChatMessage>>

    suspend fun getRecentSessionIds(userId: String, limit: Int = 10): List<String>
    suspend fun sendMessage(message: AIChatMessage)
    suspend fun sendMessages(messages: List<AIChatMessage>)
    suspend fun deleteSession(sessionId: String)
    suspend fun deleteAllMessagesForUser(userId: String)
    suspend fun deleteOldMessages(olderThanDays: Int = 30)
}
