package com.zanoapps.messaging.presentation.inbox

sealed interface InboxAction {
    data object LoadConversations : InboxAction
    data object RefreshConversations : InboxAction
    data class OnConversationClick(val conversation: Conversation) : InboxAction
    data class OnDeleteConversation(val conversationId: String) : InboxAction
    data class OnMarkAsRead(val conversationId: String) : InboxAction
}
