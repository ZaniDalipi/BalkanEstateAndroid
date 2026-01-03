package com.zanoapps.messaging.presentation.conversations

import com.zanoapps.presentation.ui.UiText

sealed interface ConversationsEvent {
    data class Error(val error: UiText) : ConversationsEvent
    data class NavigateToChat(val conversationId: String) : ConversationsEvent
}
