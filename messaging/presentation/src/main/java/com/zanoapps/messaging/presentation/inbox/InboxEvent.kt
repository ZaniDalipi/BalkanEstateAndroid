package com.zanoapps.messaging.presentation.inbox

sealed interface InboxEvent {
    data class NavigateToChat(val conversationId: String) : InboxEvent
    data class Error(val message: String) : InboxEvent
}
