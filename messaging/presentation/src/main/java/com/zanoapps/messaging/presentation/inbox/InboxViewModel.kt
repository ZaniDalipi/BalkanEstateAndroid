package com.zanoapps.messaging.presentation.inbox

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.messaging.domain.model.Conversation
import com.zanoapps.messaging.domain.model.Message
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
            InboxAction.OnLoadConversations -> loadConversations()
            is InboxAction.OnConversationClick -> {
                state = state.copy(
                    selectedConversation = action.conversation,
                    isLoadingMessages = true
                )
                loadMessages(action.conversation.id)
            }
            InboxAction.OnBackFromConversation -> {
                state = state.copy(selectedConversation = null, messages = emptyList())
            }
            is InboxAction.OnSearchQueryChanged -> {
                state = state.copy(searchQuery = action.query)
                filterConversations()
            }
            is InboxAction.OnTabChanged -> {
                state = state.copy(selectedTab = action.tab)
                filterConversations()
            }
            is InboxAction.OnNewMessageChanged -> {
                state = state.copy(newMessage = action.message)
            }
            InboxAction.OnSendMessage -> sendMessage()
            is InboxAction.OnDeleteConversation -> {
                val updated = state.conversations.filter { it.id != action.conversationId }
                state = state.copy(conversations = updated, filteredConversations = updated)
            }
            is InboxAction.OnArchiveConversation -> { /* Archive logic */ }
            is InboxAction.OnMarkAsRead -> {
                val updated = state.conversations.map {
                    if (it.id == action.conversationId) it.copy(unreadCount = 0) else it
                }
                state = state.copy(conversations = updated, filteredConversations = updated)
            }
        }
    }

    private fun loadConversations() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val conversations = getMockConversations()
            state = state.copy(
                conversations = conversations,
                filteredConversations = conversations,
                isLoading = false
            )
        }
    }

    private fun loadMessages(conversationId: String) {
        viewModelScope.launch {
            delay(300)
            val messages = getMockMessages(conversationId)
            state = state.copy(messages = messages, isLoadingMessages = false)
        }
    }

    private fun sendMessage() {
        if (state.newMessage.isBlank()) return
        viewModelScope.launch {
            state = state.copy(isSendingMessage = true)
            delay(500)
            val newMsg = Message(
                id = "msg_${System.currentTimeMillis()}",
                conversationId = state.selectedConversation?.id ?: "",
                content = state.newMessage,
                timestamp = "Just now",
                isFromUser = true,
                isRead = true
            )
            state = state.copy(
                messages = state.messages + newMsg,
                newMessage = "",
                isSendingMessage = false
            )
            eventChannel.send(InboxEvent.MessageSent)
        }
    }

    private fun filterConversations() {
        val query = state.searchQuery.lowercase()
        val filtered = state.conversations.filter { conv ->
            val matchesQuery = query.isEmpty() ||
                    conv.agentName.lowercase().contains(query) ||
                    conv.lastMessage.lowercase().contains(query) ||
                    conv.propertyTitle.lowercase().contains(query)
            val matchesTab = when (state.selectedTab) {
                InboxTab.ALL -> true
                InboxTab.UNREAD -> conv.unreadCount > 0
                InboxTab.ARCHIVED -> false
            }
            matchesQuery && matchesTab
        }
        state = state.copy(filteredConversations = filtered)
    }

    private fun getMockConversations(): List<Conversation> = listOf(
        Conversation(
            id = "conv1",
            agentName = "Besmir Kola",
            lastMessage = "The property is still available. Would you like to schedule a viewing?",
            lastMessageTime = "2 min ago",
            unreadCount = 2,
            propertyTitle = "Beautiful 3BR Villa in Tirana",
            propertyImageUrl = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914",
            isOnline = true
        ),
        Conversation(
            id = "conv2",
            agentName = "Eglantina Dervishi",
            lastMessage = "I've sent you the documents for review.",
            lastMessageTime = "1 hour ago",
            unreadCount = 0,
            propertyTitle = "Modern 2BR Apartment in Blloku",
            propertyImageUrl = "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267",
            isOnline = false
        ),
        Conversation(
            id = "conv3",
            agentName = "Arben Dedja",
            lastMessage = "The price is negotiable. Let me know your offer.",
            lastMessageTime = "Yesterday",
            unreadCount = 1,
            propertyTitle = "Luxury Penthouse with City Views",
            propertyImageUrl = "https://images.unsplash.com/photo-1512917774080-9991f1c4c750",
            isOnline = true
        ),
        Conversation(
            id = "conv4",
            agentName = "Mirela Hoxha",
            lastMessage = "Thank you for your interest! I'll get back to you shortly.",
            lastMessageTime = "3 days ago",
            unreadCount = 0,
            propertyTitle = "Cozy Studio near University",
            isOnline = false
        )
    )

    private fun getMockMessages(conversationId: String): List<Message> = listOf(
        Message("m1", conversationId, "Hi, I'm interested in this property.", "10:00 AM", true),
        Message("m2", conversationId, "Hello! Thank you for your interest. The property is currently available for viewing.", "10:05 AM", false),
        Message("m3", conversationId, "Great! What's the earliest available time?", "10:10 AM", true),
        Message("m4", conversationId, "We can arrange a viewing this Saturday at 2 PM. Does that work for you?", "10:15 AM", false),
        Message("m5", conversationId, "That works perfectly. I'll be there.", "10:20 AM", true),
        Message("m6", conversationId, "Excellent! I'll send you the exact address and directions. See you Saturday!", "10:25 AM", false)
    )
}
