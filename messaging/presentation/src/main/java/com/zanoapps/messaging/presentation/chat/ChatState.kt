package com.zanoapps.messaging.presentation.chat

import com.zanoapps.messaging.domain.model.Conversation
import com.zanoapps.messaging.domain.model.Message

data class ChatState(
    val conversation: Conversation? = null,
    val messages: List<Message> = emptyList(),
    val currentMessage: String = "",
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val currentUserId: String = "current_user"
)
