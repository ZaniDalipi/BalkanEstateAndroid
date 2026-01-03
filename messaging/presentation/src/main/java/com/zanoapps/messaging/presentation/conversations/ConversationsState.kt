package com.zanoapps.messaging.presentation.conversations

import com.zanoapps.messaging.domain.model.Conversation

data class ConversationsState(
    val conversations: List<Conversation> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)
