package com.zanoapps.messaging.presentation.chat

import com.zanoapps.presentation.ui.UiText

sealed interface ChatEvent {
    data class Error(val error: UiText) : ChatEvent
    data object MessageSent : ChatEvent
    data class NavigateToProperty(val propertyId: String) : ChatEvent
}
