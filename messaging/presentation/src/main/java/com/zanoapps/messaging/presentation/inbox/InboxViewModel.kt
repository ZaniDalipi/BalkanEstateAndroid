package com.zanoapps.messaging.presentation.inbox

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.messaging.domain.repository.MessagingRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class InboxViewModel(
    private val messagingRepository: MessagingRepository
) : ViewModel() {

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
                viewModelScope.launch {
                    messagingRepository.deleteConversation(action.conversationId)
                }
            }
            is InboxAction.OnArchiveConversation -> { /* Archive logic */ }
            is InboxAction.OnMarkAsRead -> {
                viewModelScope.launch {
                    messagingRepository.markAsRead(action.conversationId)
                }
            }
        }
    }

    private fun loadConversations() {
        state = state.copy(isLoading = true)
        messagingRepository.getConversations()
            .onEach { conversations ->
                state = state.copy(
                    conversations = conversations,
                    filteredConversations = conversations,
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }

    private fun loadMessages(conversationId: String) {
        messagingRepository.getMessages(conversationId)
            .onEach { messages ->
                state = state.copy(messages = messages, isLoadingMessages = false)
            }
            .launchIn(viewModelScope)
        viewModelScope.launch {
            messagingRepository.markAsRead(conversationId)
        }
    }

    private fun sendMessage() {
        if (state.newMessage.isBlank()) return
        val conversationId = state.selectedConversation?.id ?: return
        viewModelScope.launch {
            state = state.copy(isSendingMessage = true)
            messagingRepository.sendMessage(conversationId, state.newMessage)
            state = state.copy(newMessage = "", isSendingMessage = false)
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
}
