package com.zanoapps.search.data.repository

import com.zanoapps.core.database.dao.AIChatMessageDao
import com.zanoapps.search.data.mappers.toDomainList
import com.zanoapps.search.data.mappers.toEntity
import com.zanoapps.search.domain.model.AIChatMessage
import com.zanoapps.search.domain.repository.AIChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Single Source of Truth Implementation for AI Chat Messages.
 *
 * Room database is the single source of truth.
 */
class AIChatRepositoryImpl(
    private val aiChatMessageDao: AIChatMessageDao
) : AIChatRepository {

    override fun getMessagesBySession(sessionId: String): Flow<List<AIChatMessage>> {
        return aiChatMessageDao.getMessagesBySession(sessionId).map { it.toDomainList() }
    }

    override fun getLatestSession(userId: String): Flow<List<AIChatMessage>> {
        return aiChatMessageDao.getLatestSession(userId).map { it.toDomainList() }
    }

    override fun getAllMessagesForUser(userId: String): Flow<List<AIChatMessage>> {
        return aiChatMessageDao.getAllMessagesForUser(userId).map { it.toDomainList() }
    }

    override suspend fun getRecentSessionIds(userId: String, limit: Int): List<String> {
        return aiChatMessageDao.getRecentSessionIds(userId, limit)
    }

    override suspend fun sendMessage(message: AIChatMessage) {
        aiChatMessageDao.insertMessage(message.toEntity())
    }

    override suspend fun sendMessages(messages: List<AIChatMessage>) {
        aiChatMessageDao.insertMessages(messages.map { it.toEntity() })
    }

    override suspend fun deleteSession(sessionId: String) {
        aiChatMessageDao.deleteSession(sessionId)
    }

    override suspend fun deleteAllMessagesForUser(userId: String) {
        aiChatMessageDao.deleteAllMessagesForUser(userId)
    }

    override suspend fun deleteOldMessages(olderThanDays: Int) {
        val threshold = System.currentTimeMillis() - (olderThanDays * 24 * 60 * 60 * 1000L)
        aiChatMessageDao.deleteOldMessages(threshold)
    }
}
