package com.zanoapps.core.data.repository

import com.zanoapps.core.data.mappers.toDomain
import com.zanoapps.core.data.mappers.toDomainList
import com.zanoapps.core.data.mappers.toEntities
import com.zanoapps.core.data.mappers.toEntity
import com.zanoapps.core.data.remote.MessagingApiService
import com.zanoapps.core.data.remote.dto.CreateConversationRequest
import com.zanoapps.core.data.remote.dto.SendMessageRequest
import com.zanoapps.core.database.dao.ConversationDao
import com.zanoapps.core.database.dao.MessageDao
import com.zanoapps.core.database.entity.ConversationEntity
import com.zanoapps.core.database.entity.MessageEntity
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.messaging.domain.model.Conversation
import com.zanoapps.messaging.domain.model.Message
import com.zanoapps.messaging.domain.repository.MessagingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.ZoneId
import java.util.UUID

/**
 * Single Source of Truth Implementation for Messaging.
 *
 * Room database is the single source of truth.
 * Data is synced with MongoDB API.
 * UI observes Room database through Flow.
 */
class MessagingRepositorySSOTImpl(
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao,
    private val messagingApiService: MessagingApiService
) : MessagingRepository {

    /**
     * Get all conversations from Room (single source of truth).
     */
    override fun getConversations(): Flow<List<Conversation>> {
        return conversationDao.getAllConversations().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * Get messages for a conversation from Room.
     */
    override fun getMessages(conversationId: String): Flow<List<Message>> {
        return messageDao.getMessagesByConversation(conversationId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * Send a message - optimistically add to local DB, then sync with server.
     */
    override suspend fun sendMessage(
        conversationId: String,
        content: String
    ): EmptyResult<DataError.Network> {
        // Create message locally first (optimistic update)
        val localMessage = MessageEntity(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            senderId = "current_user", // TODO: Get from auth
            content = content,
            timestamp = System.currentTimeMillis(),
            isRead = true,
            messageType = "TEXT"
        )
        messageDao.insertMessage(localMessage)

        // Update conversation's last message
        conversationDao.getConversationByIdOnce(conversationId)?.let { conversation ->
            conversationDao.updateConversation(
                conversation.copy(
                    lastMessage = content,
                    lastMessageTime = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            )
        }

        // Sync with server
        return when (val result = messagingApiService.sendMessage(
            SendMessageRequest(conversationId = conversationId, content = content)
        )) {
            is Result.Success -> {
                // Update local message with server ID
                messageDao.deleteMessage(localMessage.id)
                messageDao.insertMessage(result.data.data.toEntity())
                Result.Success(Unit)
            }
            is Result.Error -> {
                // Keep local message for retry
                Result.Error(result.error)
            }
        }
    }

    /**
     * Mark conversation as read - update locally and sync.
     */
    override suspend fun markAsRead(conversationId: String): EmptyResult<DataError.Network> {
        // Update locally first
        conversationDao.markAsRead(conversationId)
        messageDao.markMessagesAsRead(conversationId)

        // Sync with server
        return when (val result = messagingApiService.markConversationAsRead(conversationId)) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(result.error)
        }
    }

    /**
     * Start a new conversation.
     */
    override suspend fun startConversation(
        participantId: String,
        propertyId: String?
    ): Result<Conversation, DataError.Network> {
        return when (val result = messagingApiService.createConversation(
            CreateConversationRequest(participantId = participantId, propertyId = propertyId)
        )) {
            is Result.Success -> {
                val entity = result.data.data.toEntity()
                conversationDao.insertConversation(entity)
                Result.Success(entity.toDomain())
            }
            is Result.Error -> Result.Error(result.error)
        }
    }

    /**
     * Delete a conversation.
     */
    override suspend fun deleteConversation(conversationId: String): EmptyResult<DataError.Network> {
        // Delete locally first
        conversationDao.deleteConversation(conversationId)

        // Sync with server
        return when (val result = messagingApiService.deleteConversation(conversationId)) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(result.error)
        }
    }

    /**
     * Refresh conversations from remote API.
     */
    suspend fun refreshConversations(): EmptyResult<DataError.Network> {
        return when (val result = messagingApiService.getConversations()) {
            is Result.Success -> {
                conversationDao.insertConversations(result.data.data.toEntities())
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }

    /**
     * Refresh messages for a conversation from remote API.
     */
    suspend fun refreshMessages(conversationId: String): EmptyResult<DataError.Network> {
        return when (val result = messagingApiService.getMessages(conversationId)) {
            is Result.Success -> {
                messageDao.insertMessages(result.data.data.toEntities())
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }
}
