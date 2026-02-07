package com.zanoapps.shared.data.repository

import com.zanoapps.shared.domain.model.Conversation
import com.zanoapps.shared.domain.model.Message
import com.zanoapps.shared.domain.model.MessageParticipant
import com.zanoapps.shared.domain.repository.MessageRepository
import com.zanoapps.shared.util.DataError
import com.zanoapps.shared.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MessageRepositoryImpl : MessageRepository {

    private val conversationsCache = MutableStateFlow<List<Conversation>>(getMockConversations())
    private val messagesCache = MutableStateFlow<Map<String, List<Message>>>(getMockMessages())

    override suspend fun getConversations(): Result<List<Conversation>, DataError> {
        return try {
            Result.Success(conversationsCache.value)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun getConversation(conversationId: String): Result<Conversation, DataError> {
        return try {
            val conversation = conversationsCache.value.find { it.id == conversationId }
            if (conversation != null) {
                Result.Success(conversation)
            } else {
                Result.Error(DataError.Network.NOT_FOUND)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun getMessages(conversationId: String): Result<List<Message>, DataError> {
        return try {
            val messages = messagesCache.value[conversationId] ?: emptyList()
            Result.Success(messages)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun sendMessage(
        conversationId: String,
        content: String
    ): Result<Message, DataError> {
        return try {
            val newMessage = Message(
                id = "msg_${System.currentTimeMillis()}",
                conversationId = conversationId,
                senderId = "current_user",
                content = content,
                timestamp = "2024-01-15T10:30:00Z",
                isRead = true,
                attachments = emptyList()
            )

            val currentMessages = messagesCache.value.toMutableMap()
            val conversationMessages = (currentMessages[conversationId] ?: emptyList()).toMutableList()
            conversationMessages.add(newMessage)
            currentMessages[conversationId] = conversationMessages
            messagesCache.value = currentMessages

            Result.Success(newMessage)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun markAsRead(conversationId: String): Result<Unit, DataError> {
        return try {
            val updatedConversations = conversationsCache.value.map { conversation ->
                if (conversation.id == conversationId) {
                    conversation.copy(unreadCount = 0)
                } else {
                    conversation
                }
            }
            conversationsCache.value = updatedConversations
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun deleteConversation(conversationId: String): Result<Unit, DataError> {
        return try {
            conversationsCache.value = conversationsCache.value.filter { it.id != conversationId }
            val currentMessages = messagesCache.value.toMutableMap()
            currentMessages.remove(conversationId)
            messagesCache.value = currentMessages
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override fun observeConversations(): Flow<List<Conversation>> {
        return conversationsCache
    }

    override fun observeMessages(conversationId: String): Flow<List<Message>> {
        return messagesCache.map { it[conversationId] ?: emptyList() }
    }

    override fun observeUnreadCount(): Flow<Int> {
        return conversationsCache.map { conversations ->
            conversations.sumOf { it.unreadCount }
        }
    }

    override suspend fun createConversation(
        participantId: String,
        propertyId: String?,
        initialMessage: String
    ): Result<Conversation, DataError> {
        return try {
            val newConversation = Conversation(
                id = "conv_${System.currentTimeMillis()}",
                participants = listOf(
                    MessageParticipant(
                        id = "current_user",
                        name = "You",
                        avatarUrl = null,
                        isAgent = false
                    ),
                    MessageParticipant(
                        id = participantId,
                        name = "Agent",
                        avatarUrl = null,
                        isAgent = true
                    )
                ),
                lastMessage = initialMessage,
                lastMessageTimestamp = "2024-01-15T10:30:00Z",
                unreadCount = 0,
                propertyId = propertyId,
                propertyTitle = "Property Inquiry"
            )

            conversationsCache.value = conversationsCache.value + newConversation

            Result.Success(newConversation)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    private fun getMockConversations(): List<Conversation> {
        return listOf(
            Conversation(
                id = "conv_1",
                participants = listOf(
                    MessageParticipant(
                        id = "current_user",
                        name = "You",
                        avatarUrl = null,
                        isAgent = false
                    ),
                    MessageParticipant(
                        id = "agent_1",
                        name = "John Doe",
                        avatarUrl = null,
                        isAgent = true
                    )
                ),
                lastMessage = "Hello! I'm interested in the apartment in Tirana.",
                lastMessageTimestamp = "2024-01-15T10:30:00Z",
                unreadCount = 2,
                propertyId = "1",
                propertyTitle = "Modern Apartment in Tirana Center"
            ),
            Conversation(
                id = "conv_2",
                participants = listOf(
                    MessageParticipant(
                        id = "current_user",
                        name = "You",
                        avatarUrl = null,
                        isAgent = false
                    ),
                    MessageParticipant(
                        id = "agent_2",
                        name = "Maria Smith",
                        avatarUrl = null,
                        isAgent = true
                    )
                ),
                lastMessage = "When would you like to schedule a viewing?",
                lastMessageTimestamp = "2024-01-14T15:45:00Z",
                unreadCount = 0,
                propertyId = "2",
                propertyTitle = "Luxury Villa with Sea View"
            )
        )
    }

    private fun getMockMessages(): Map<String, List<Message>> {
        return mapOf(
            "conv_1" to listOf(
                Message(
                    id = "msg_1",
                    conversationId = "conv_1",
                    senderId = "current_user",
                    content = "Hello! I'm interested in the apartment in Tirana.",
                    timestamp = "2024-01-15T10:30:00Z",
                    isRead = true,
                    attachments = emptyList()
                ),
                Message(
                    id = "msg_2",
                    conversationId = "conv_1",
                    senderId = "agent_1",
                    content = "Hi! Thank you for your interest. The apartment is still available.",
                    timestamp = "2024-01-15T10:35:00Z",
                    isRead = false,
                    attachments = emptyList()
                )
            ),
            "conv_2" to listOf(
                Message(
                    id = "msg_3",
                    conversationId = "conv_2",
                    senderId = "current_user",
                    content = "Is the villa still available for viewing?",
                    timestamp = "2024-01-14T15:30:00Z",
                    isRead = true,
                    attachments = emptyList()
                ),
                Message(
                    id = "msg_4",
                    conversationId = "conv_2",
                    senderId = "agent_2",
                    content = "When would you like to schedule a viewing?",
                    timestamp = "2024-01-14T15:45:00Z",
                    isRead = true,
                    attachments = emptyList()
                )
            )
        )
    }
}
