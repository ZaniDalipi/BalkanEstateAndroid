package com.zanoapps.messaging.presentation.inbox

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class InboxViewModel : ViewModel() {

    var state by mutableStateOf(InboxState())
        private set

    private val eventChannel = Channel<InboxEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadConversations()
    }

    fun onAction(action: InboxAction) {
        when (action) {
            InboxAction.LoadConversations -> loadConversations()
            InboxAction.RefreshConversations -> refreshConversations()
            is InboxAction.OnConversationClick -> {
                viewModelScope.launch {
                    eventChannel.send(InboxEvent.NavigateToChat(action.conversation.id))
                }
            }
            is InboxAction.OnDeleteConversation -> deleteConversation(action.conversationId)
            is InboxAction.OnMarkAsRead -> markAsRead(action.conversationId)
        }
    }

    private fun loadConversations() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val conversations = getMockConversations()
            state = state.copy(
                conversations = conversations,
                isLoading = false,
                isEmpty = conversations.isEmpty()
            )
        }
    }

    private fun refreshConversations() {
        viewModelScope.launch {
            state = state.copy(isRefreshing = true)
            val conversations = getMockConversations()
            state = state.copy(
                conversations = conversations,
                isRefreshing = false,
                isEmpty = conversations.isEmpty()
            )
        }
    }

    private fun deleteConversation(conversationId: String) {
        val updatedList = state.conversations.filter { it.id != conversationId }
        state = state.copy(
            conversations = updatedList,
            isEmpty = updatedList.isEmpty()
        )
    }

    private fun markAsRead(conversationId: String) {
        val updatedList = state.conversations.map { conversation ->
            if (conversation.id == conversationId) {
                conversation.copy(unreadCount = 0)
            } else {
                conversation
            }
        }
        state = state.copy(conversations = updatedList)
    }

    private fun getMockConversations(): List<Conversation> {
        return listOf(
            Conversation(
                id = "conv1",
                participantName = "Marina Lleshi",
                participantAvatarUrl = null,
                lastMessage = "The property is still available. Would you like to schedule a visit?",
                lastMessageTime = "2 min ago",
                unreadCount = 2,
                propertyTitle = "Luxury Penthouse with Sea View",
                propertyImageUrl = "https://images.unsplash.com/photo-1512917774080-9991f1c4c750",
                isOnline = true
            ),
            Conversation(
                id = "conv2",
                participantName = "Andi Basha",
                participantAvatarUrl = null,
                lastMessage = "Thank you for your interest! I'll send you the documents.",
                lastMessageTime = "1 hour ago",
                unreadCount = 0,
                propertyTitle = "Modern Studio in Business District",
                propertyImageUrl = "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688",
                isOnline = false
            ),
            Conversation(
                id = "conv3",
                participantName = "Elira Hoxha",
                participantAvatarUrl = null,
                lastMessage = "Yes, the garden is over 500m². Perfect for families!",
                lastMessageTime = "Yesterday",
                unreadCount = 1,
                propertyTitle = "Charming Villa with Garden",
                propertyImageUrl = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914",
                isOnline = true
            ),
            Conversation(
                id = "conv4",
                participantName = "Besmir Kola",
                participantAvatarUrl = null,
                lastMessage = "I can arrange a viewing this weekend if you're available.",
                lastMessageTime = "3 days ago",
                unreadCount = 0,
                propertyTitle = "City Center Apartment",
                propertyImageUrl = null,
                isOnline = false
            )
        )
    }
}
