package com.zanoapps.messaging.presentation.chat

sealed interface ChatAction {
    data object OnBackClick : ChatAction
    data class OnMessageChanged(val message: String) : ChatAction
    data object OnSendMessage : ChatAction
    data object OnViewProperty : ChatAction
}
