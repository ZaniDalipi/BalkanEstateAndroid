package com.zanoapps.messaging.presentation.inbox

import com.zanoapps.messaging.domain.model.Conversation

sealed interface InboxAction {
    data object OnLoadConversations : InboxAction
    data class OnConversationClick(val conversation: Conversation) : InboxAction
    data object OnBackFromConversation : InboxAction
    data class OnSearchQueryChanged(val query: String) : InboxAction
    data class OnTabChanged(val tab: InboxTab) : InboxAction
    data class OnNewMessageChanged(val message: String) : InboxAction
    data object OnSendMessage : InboxAction
    data class OnDeleteConversation(val conversationId: String) : InboxAction
    data class OnArchiveConversation(val conversationId: String) : InboxAction
    data class OnMarkAsRead(val conversationId: String) : InboxAction
}
