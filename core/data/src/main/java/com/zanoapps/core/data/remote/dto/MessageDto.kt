package com.zanoapps.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    @SerialName("_id")
    val id: String,
    val conversationId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val messageType: String = "TEXT"
)

@Serializable
data class MessagesResponse(
    val success: Boolean,
    val data: List<MessageDto>
)

@Serializable
data class MessageResponse(
    val success: Boolean,
    val data: MessageDto
)

@Serializable
data class SendMessageRequest(
    val conversationId: String,
    val content: String,
    val messageType: String = "TEXT"
)
