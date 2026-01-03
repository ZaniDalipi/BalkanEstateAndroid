package com.zanoapps.messaging.presentation.conversations

sealed interface ConversationsAction {
    data object OnBackClick : ConversationsAction
    data class OnConversationClick(val conversationId: String) : ConversationsAction
    data class OnDeleteConversation(val conversationId: String) : ConversationsAction
    data object OnRefresh : ConversationsAction
}
