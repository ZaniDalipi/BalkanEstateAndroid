package com.zanoapps.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val id: String,
    val participants: List<ConversationParticipant>,
    val propertyId: String? = null,
    val propertyTitle: String? = null,
    val propertyImageUrl: String? = null,
    val lastMessage: Message? = null,
    val unreadCount: Int = 0,
    val createdAt: Long = 0,
    val updatedAt: Long = 0
)

@Serializable
data class ConversationParticipant(
    val userId: String,
    val displayName: String,
    val avatarUrl: String? = null,
    val isAgent: Boolean = false
)

@Serializable
data class Message(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val messageType: MessageType = MessageType.TEXT,
    val attachmentUrl: String? = null,
    val isRead: Boolean = false,
    val createdAt: Long = 0
)

@Serializable
enum class MessageType {
    TEXT,
    IMAGE,
    DOCUMENT,
    PROPERTY_SHARE,
    CONTACT_REQUEST,
    SCHEDULE_VIEWING
}

@Serializable
data class SendMessageRequest(
    val conversationId: String,
    val content: String,
    val messageType: MessageType = MessageType.TEXT,
    val attachmentUrl: String? = null
)

@Serializable
data class CreateConversationRequest(
    val recipientId: String,
    val propertyId: String? = null,
    val initialMessage: String
)
