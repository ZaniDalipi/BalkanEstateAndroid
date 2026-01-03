package com.zanoapps.messaging.domain.model

import java.time.LocalDateTime

data class Message(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val content: String,
    val timestamp: LocalDateTime,
    val isRead: Boolean = false,
    val type: MessageType = MessageType.TEXT
)

enum class MessageType {
    TEXT,
    IMAGE,
    PROPERTY_LINK
}
