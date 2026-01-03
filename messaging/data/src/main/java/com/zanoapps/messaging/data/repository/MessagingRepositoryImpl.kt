package com.zanoapps.messaging.data.repository

import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.messaging.domain.model.Conversation
import com.zanoapps.messaging.domain.model.Message
import com.zanoapps.messaging.domain.model.MessageType
import com.zanoapps.messaging.domain.repository.MessagingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.util.UUID

class MessagingRepositoryImpl : MessagingRepository {

    private val conversationsFlow = MutableStateFlow(getMockConversations())
    private val messagesFlow = MutableStateFlow(getMockMessages())

    override fun getConversations(): Flow<List<Conversation>> {
        return conversationsFlow
    }

    override fun getMessages(conversationId: String): Flow<List<Message>> {
        return messagesFlow.map { messages ->
            messages.filter { it.conversationId == conversationId }
                .sortedBy { it.timestamp }
        }
    }

    override suspend fun sendMessage(
        conversationId: String,
        content: String
    ): EmptyResult<DataError.Network> {
        delay(300) // Simulate network delay

        val newMessage = Message(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            senderId = "current_user",
            content = content,
            timestamp = LocalDateTime.now(),
            isRead = true,
            type = MessageType.TEXT
        )

        val currentMessages = messagesFlow.value.toMutableList()
        currentMessages.add(newMessage)
        messagesFlow.value = currentMessages

        // Update conversation's last message
        val currentConversations = conversationsFlow.value.toMutableList()
        val conversationIndex = currentConversations.indexOfFirst { it.id == conversationId }
        if (conversationIndex >= 0) {
            currentConversations[conversationIndex] = currentConversations[conversationIndex].copy(
                lastMessage = content,
                lastMessageTime = LocalDateTime.now()
            )
            conversationsFlow.value = currentConversations
        }

        return Result.Success(Unit)
    }

    override suspend fun markAsRead(conversationId: String): EmptyResult<DataError.Network> {
        delay(100)

        val currentConversations = conversationsFlow.value.toMutableList()
        val conversationIndex = currentConversations.indexOfFirst { it.id == conversationId }
        if (conversationIndex >= 0) {
            currentConversations[conversationIndex] = currentConversations[conversationIndex].copy(
                unreadCount = 0
            )
            conversationsFlow.value = currentConversations
        }

        val currentMessages = messagesFlow.value.map { message ->
            if (message.conversationId == conversationId && !message.isRead) {
                message.copy(isRead = true)
            } else {
                message
            }
        }
        messagesFlow.value = currentMessages

        return Result.Success(Unit)
    }

    override suspend fun startConversation(
        participantId: String,
        propertyId: String?
    ): Result<Conversation, DataError.Network> {
        delay(500)

        val newConversation = Conversation(
            id = UUID.randomUUID().toString(),
            participantId = participantId,
            participantName = "Agent Name",
            participantImageUrl = null,
            propertyId = propertyId,
            propertyTitle = propertyId?.let { "Property Title" },
            lastMessage = null,
            lastMessageTime = null,
            unreadCount = 0,
            isOnline = true
        )

        val currentConversations = conversationsFlow.value.toMutableList()
        currentConversations.add(0, newConversation)
        conversationsFlow.value = currentConversations

        return Result.Success(newConversation)
    }

    override suspend fun deleteConversation(conversationId: String): EmptyResult<DataError.Network> {
        delay(300)

        val currentConversations = conversationsFlow.value.toMutableList()
        currentConversations.removeIf { it.id == conversationId }
        conversationsFlow.value = currentConversations

        val currentMessages = messagesFlow.value.toMutableList()
        currentMessages.removeIf { it.conversationId == conversationId }
        messagesFlow.value = currentMessages

        return Result.Success(Unit)
    }

    private fun getMockConversations(): List<Conversation> = listOf(
        Conversation(
            id = "conv1",
            participantId = "agent1",
            participantName = "Sarah Johnson",
            participantImageUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100",
            propertyId = "prop1",
            propertyTitle = "Modern Apartment in City Center",
            lastMessage = "I can schedule a viewing for tomorrow at 2 PM. Does that work for you?",
            lastMessageTime = LocalDateTime.now().minusMinutes(30),
            unreadCount = 2,
            isOnline = true
        ),
        Conversation(
            id = "conv2",
            participantId = "agent2",
            participantName = "Michael Chen",
            participantImageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100",
            propertyId = "prop2",
            propertyTitle = "Luxury Villa with Pool",
            lastMessage = "The property is still available. Would you like more details?",
            lastMessageTime = LocalDateTime.now().minusHours(2),
            unreadCount = 0,
            isOnline = false
        ),
        Conversation(
            id = "conv3",
            participantId = "agent3",
            participantName = "Emma Wilson",
            participantImageUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=100",
            propertyId = "prop3",
            propertyTitle = "Cozy Studio Near Beach",
            lastMessage = "Thank you for your interest! I'll send you the floor plans shortly.",
            lastMessageTime = LocalDateTime.now().minusDays(1),
            unreadCount = 0,
            isOnline = true
        ),
        Conversation(
            id = "conv4",
            participantId = "agent4",
            participantName = "David Brown",
            participantImageUrl = null,
            propertyId = null,
            propertyTitle = null,
            lastMessage = "Hello! How can I help you today?",
            lastMessageTime = LocalDateTime.now().minusDays(3),
            unreadCount = 0,
            isOnline = false
        )
    )

    private fun getMockMessages(): List<Message> = listOf(
        // Conversation 1 messages
        Message(
            id = "msg1",
            conversationId = "conv1",
            senderId = "current_user",
            content = "Hi, I'm interested in the Modern Apartment in City Center. Is it still available?",
            timestamp = LocalDateTime.now().minusDays(1),
            isRead = true
        ),
        Message(
            id = "msg2",
            conversationId = "conv1",
            senderId = "agent1",
            content = "Hello! Yes, the apartment is still available. It's a beautiful 2-bedroom unit with modern amenities.",
            timestamp = LocalDateTime.now().minusDays(1).plusMinutes(10),
            isRead = true
        ),
        Message(
            id = "msg3",
            conversationId = "conv1",
            senderId = "current_user",
            content = "Great! Can I schedule a viewing?",
            timestamp = LocalDateTime.now().minusHours(2),
            isRead = true
        ),
        Message(
            id = "msg4",
            conversationId = "conv1",
            senderId = "agent1",
            content = "I can schedule a viewing for tomorrow at 2 PM. Does that work for you?",
            timestamp = LocalDateTime.now().minusMinutes(30),
            isRead = false
        ),
        // Conversation 2 messages
        Message(
            id = "msg5",
            conversationId = "conv2",
            senderId = "current_user",
            content = "Is the Luxury Villa with Pool still on the market?",
            timestamp = LocalDateTime.now().minusHours(3),
            isRead = true
        ),
        Message(
            id = "msg6",
            conversationId = "conv2",
            senderId = "agent2",
            content = "The property is still available. Would you like more details?",
            timestamp = LocalDateTime.now().minusHours(2),
            isRead = true
        ),
        // Conversation 3 messages
        Message(
            id = "msg7",
            conversationId = "conv3",
            senderId = "current_user",
            content = "Could you send me the floor plans for the studio?",
            timestamp = LocalDateTime.now().minusDays(1).plusHours(5),
            isRead = true
        ),
        Message(
            id = "msg8",
            conversationId = "conv3",
            senderId = "agent3",
            content = "Thank you for your interest! I'll send you the floor plans shortly.",
            timestamp = LocalDateTime.now().minusDays(1),
            isRead = true
        )
    )
}
