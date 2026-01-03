package com.zanoapps.messaging.presentation.conversations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.util.Result
import com.zanoapps.messaging.domain.repository.MessagingRepository
import com.zanoapps.messaging.presentation.R
import com.zanoapps.presentation.ui.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ConversationsViewModel(
    private val messagingRepository: MessagingRepository
) : ViewModel() {

    var state by mutableStateOf(ConversationsState())
        private set

    private val eventChannel = Channel<ConversationsEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadConversations()
    }

    private fun loadConversations() {
        state = state.copy(isLoading = true)
        messagingRepository.getConversations()
            .onEach { conversations ->
                state = state.copy(
                    conversations = conversations,
                    isLoading = false,
                    isRefreshing = false
                )
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ConversationsAction) {
        when (action) {
            ConversationsAction.OnBackClick -> Unit // Handled by navigation
            is ConversationsAction.OnConversationClick -> {
                viewModelScope.launch {
                    eventChannel.send(ConversationsEvent.NavigateToChat(action.conversationId))
                }
            }
            is ConversationsAction.OnDeleteConversation -> deleteConversation(action.conversationId)
            ConversationsAction.OnRefresh -> refresh()
        }
    }

    private fun refresh() {
        state = state.copy(isRefreshing = true)
        // Flow will automatically update
    }

    private fun deleteConversation(conversationId: String) {
        viewModelScope.launch {
            when (messagingRepository.deleteConversation(conversationId)) {
                is Result.Success -> {
                    // Conversation will be removed from the flow automatically
                }
                is Result.Error -> {
                    eventChannel.send(
                        ConversationsEvent.Error(UiText.StringResource(R.string.error_delete_conversation))
                    )
                }
            }
        }
    }
}
