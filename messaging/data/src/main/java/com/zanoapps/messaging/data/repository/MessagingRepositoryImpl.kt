package com.zanoapps.messaging.data.repository

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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class MessagingRepositoryImpl(
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao
) : MessagingRepository {

    override fun getConversations(): Flow<List<Conversation>> {
        return conversationDao.getConversations().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getMessages(conversationId: String): Flow<List<Message>> {
        return messageDao.getMessages(conversationId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun sendMessage(conversationId: String, content: String): EmptyResult<DataError.Network> {
        return try {
            val timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            val message = MessageEntity(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId,
                content = content,
                timestamp = timestamp,
                isFromUser = true,
                isRead = true
            )
            messageDao.insert(message)
            conversationDao.updateLastMessage(conversationId, content, timestamp)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun deleteConversation(conversationId: String): EmptyResult<DataError.Local> {
        return try {
            conversationDao.delete(conversationId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun archiveConversation(conversationId: String): EmptyResult<DataError.Local> {
        return try {
            conversationDao.archive(conversationId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun markAsRead(conversationId: String): EmptyResult<DataError.Local> {
        return try {
            conversationDao.markAsRead(conversationId)
            messageDao.markAllAsRead(conversationId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }
}

private fun ConversationEntity.toDomain(): Conversation {
    return Conversation(
        id = id, agentName = agentName, agentAvatar = agentAvatar,
        lastMessage = lastMessage, lastMessageTime = lastMessageTime,
        unreadCount = unreadCount, propertyTitle = propertyTitle,
        propertyImageUrl = propertyImageUrl, isOnline = isOnline
    )
}

private fun MessageEntity.toDomain(): Message {
    return Message(
        id = id, conversationId = conversationId, content = content,
        timestamp = timestamp, isFromUser = isFromUser, isRead = isRead
    )
}
