package com.zanoapps.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConversationDto(
    @SerialName("_id")
    val id: String,
    val participantId: String,
    val participantName: String,
    val participantImageUrl: String? = null,
    val propertyId: String? = null,
    val propertyTitle: String? = null,
    val lastMessage: String? = null,
    val lastMessageTime: Long? = null,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long
)

@Serializable
data class ConversationsResponse(
    val success: Boolean,
    val data: List<ConversationDto>
)

@Serializable
data class ConversationResponse(
    val success: Boolean,
    val data: ConversationDto
)

@Serializable
data class CreateConversationRequest(
    val participantId: String,
    val propertyId: String? = null
)
