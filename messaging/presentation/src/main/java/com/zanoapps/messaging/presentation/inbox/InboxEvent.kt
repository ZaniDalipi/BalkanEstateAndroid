package com.zanoapps.messaging.presentation.inbox

import com.zanoapps.presentation.ui.UiText

sealed interface InboxEvent {
    data class Error(val error: UiText) : InboxEvent
    data object MessageSent : InboxEvent
    data object ConversationDeleted : InboxEvent
}
