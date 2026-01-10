package com.zanoapps.messaging.presentation.inbox

data class Conversation(
    val id: String,
    val participantName: String,
    val participantAvatarUrl: String?,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int,
    val propertyTitle: String?,
    val propertyImageUrl: String?,
    val isOnline: Boolean = false
)

data class InboxState(
    val conversations: List<Conversation> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isEmpty: Boolean = false,
    val errorMessage: String? = null,
    val selectedConversation: Conversation? = null
)
