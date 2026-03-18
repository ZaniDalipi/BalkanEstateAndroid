package com.zanoapps.agent.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.agent.domain.repository.AgentRepository
import com.zanoapps.core.domain.util.Result
import com.zanoapps.presentation.ui.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AgentDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val agentRepository: AgentRepository
) : ViewModel() {

    var state by mutableStateOf(AgentDetailState())
        private set

    private val eventChannel = Channel<AgentDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    private val agentId: String = savedStateHandle.get<String>("agentId") ?: ""

    init {
        loadAgent()
    }

    fun onAction(action: AgentDetailAction) {
        when (action) {
            AgentDetailAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(AgentDetailEvent.NavigateBack)
                }
            }
            AgentDetailAction.OnCallClick -> {
                state.agent?.let { agent ->
                    viewModelScope.launch {
                        eventChannel.send(AgentDetailEvent.Call(agent.phone))
                    }
                }
            }
            AgentDetailAction.OnEmailClick -> {
                state.agent?.let { agent ->
                    viewModelScope.launch {
                        eventChannel.send(AgentDetailEvent.Email(agent.email))
                    }
                }
            }
            AgentDetailAction.OnMessageClick -> {
                state.agent?.let { agent ->
                    viewModelScope.launch {
                        eventChannel.send(AgentDetailEvent.Message(agent.id))
                    }
                }
            }
            AgentDetailAction.OnViewListingsClick -> {
                state.agent?.let { agent ->
                    viewModelScope.launch {
                        eventChannel.send(AgentDetailEvent.NavigateToListings(agent.id))
                    }
                }
            }
            AgentDetailAction.OnRetry -> loadAgent()
        }
    }

    private fun loadAgent() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null)
            when (val result = agentRepository.getAgentById(agentId)) {
                is Result.Success -> {
                    state = state.copy(
                        agent = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        isLoading = false,
                        errorMessage = "Failed to load agent details"
                    )
                    eventChannel.send(
                        AgentDetailEvent.Error(
                            UiText.DynamicString("Failed to load agent details")
                        )
                    )
                }
            }
        }
    }
}
