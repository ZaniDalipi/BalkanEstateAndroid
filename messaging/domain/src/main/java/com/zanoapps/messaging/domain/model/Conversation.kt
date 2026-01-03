package com.zanoapps.messaging.domain.model

import java.time.LocalDateTime

data class Conversation(
    val id: String,
    val participantId: String,
    val participantName: String,
    val participantImageUrl: String?,
    val propertyId: String?,
    val propertyTitle: String?,
    val lastMessage: String?,
    val lastMessageTime: LocalDateTime?,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false
)
