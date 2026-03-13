package com.zanoapps.messaging.domain.model

data class Conversation(
    val id: String,
    val agentName: String,
    val agentAvatar: String = "",
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0,
    val propertyTitle: String = "",
    val propertyImageUrl: String = "",
    val isOnline: Boolean = false
)

data class Message(
    val id: String,
    val conversationId: String,
    val content: String,
    val timestamp: String,
    val isFromUser: Boolean,
    val isRead: Boolean = false
)
