package com.zanoapps.messaging.presentation.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.util.Result
import com.zanoapps.messaging.domain.repository.MessagingRepository
import com.zanoapps.messaging.presentation.R
import com.zanoapps.presentation.ui.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val messagingRepository: MessagingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val conversationId: String = checkNotNull(savedStateHandle["conversationId"])

    var state by mutableStateOf(ChatState())
        private set

    private val eventChannel = Channel<ChatEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadConversation()
        loadMessages()
        markAsRead()
    }

    private fun loadConversation() {
        viewModelScope.launch {
            val conversations = messagingRepository.getConversations().first()
            val conversation = conversations.find { it.id == conversationId }
            state = state.copy(conversation = conversation)
        }
    }

    private fun loadMessages() {
        state = state.copy(isLoading = true)
        messagingRepository.getMessages(conversationId)
            .onEach { messages ->
                state = state.copy(
                    messages = messages,
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }

    private fun markAsRead() {
        viewModelScope.launch {
            messagingRepository.markAsRead(conversationId)
        }
    }

    fun onAction(action: ChatAction) {
        when (action) {
            ChatAction.OnBackClick -> Unit // Handled by navigation
            is ChatAction.OnMessageChanged -> {
                state = state.copy(currentMessage = action.message)
            }
            ChatAction.OnSendMessage -> sendMessage()
            ChatAction.OnViewProperty -> {
                state.conversation?.propertyId?.let { propertyId ->
                    viewModelScope.launch {
                        eventChannel.send(ChatEvent.NavigateToProperty(propertyId))
                    }
                }
            }
        }
    }

    private fun sendMessage() {
        val message = state.currentMessage.trim()
        if (message.isEmpty()) return

        viewModelScope.launch {
            state = state.copy(isSending = true, currentMessage = "")

            when (messagingRepository.sendMessage(conversationId, message)) {
                is Result.Success -> {
                    eventChannel.send(ChatEvent.MessageSent)
                }
                is Result.Error -> {
                    state = state.copy(currentMessage = message)
                    eventChannel.send(
                        ChatEvent.Error(UiText.StringResource(R.string.error_send_message))
                    )
                }
            }

            state = state.copy(isSending = false)
        }
    }
}
