package com.zanoapps.messaging.presentation.inbox

import com.zanoapps.messaging.domain.model.Conversation
import com.zanoapps.messaging.domain.model.Message

data class InboxState(
    val conversations: List<Conversation> = emptyList(),
    val filteredConversations: List<Conversation> = emptyList(),
    val selectedConversation: Conversation? = null,
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMessages: Boolean = false,
    val searchQuery: String = "",
    val newMessage: String = "",
    val isSendingMessage: Boolean = false,
    val selectedTab: InboxTab = InboxTab.ALL,
    val errorMessage: String? = null
)

enum class InboxTab(val displayName: String) {
    ALL("All"),
    UNREAD("Unread"),
    ARCHIVED("Archived")
}
