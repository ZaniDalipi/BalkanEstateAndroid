package com.zanoapps.agent.presentation.listings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.agent.domain.model.Agent
import com.zanoapps.agent.domain.repository.AgentRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AgentListingsViewModel(
    private val agentRepository: AgentRepository
) : ViewModel() {

    var state by mutableStateOf(AgentListingsState())
        private set

    private val eventChannel = Channel<AgentListingsEvent>()
    val events = eventChannel.receiveAsFlow()

    private var allAgents: List<Agent> = emptyList()

    init {
        observeAgents()
        refreshAgents()
    }

    private fun observeAgents() {
        agentRepository.getAgents()
            .onEach { agents ->
                allAgents = agents
                state = state.copy(
                    agents = filterAgents(agents, state.searchQuery),
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }

    private fun refreshAgents() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = allAgents.isEmpty(),
                isRefreshing = allAgents.isNotEmpty()
            )
            agentRepository.refreshAgents()
            state = state.copy(isLoading = false, isRefreshing = false)
        }
    }

    fun onAction(action: AgentListingsAction) {
        when (action) {
            AgentListingsAction.OnBackClick -> Unit // Handled by navigation
            AgentListingsAction.OnRefresh -> refreshAgents()
            is AgentListingsAction.OnAgentClick -> {
                viewModelScope.launch {
                    eventChannel.send(AgentListingsEvent.NavigateToAgent(action.agent))
                }
            }
            is AgentListingsAction.OnSearchQueryChange -> {
                state = state.copy(
                    searchQuery = action.query,
                    agents = filterAgents(allAgents, action.query)
                )
            }
        }
    }

    private fun filterAgents(agents: List<Agent>, query: String): List<Agent> {
        if (query.isBlank()) return agents
        val lowerQuery = query.lowercase()
        return agents.filter { agent ->
            agent.name.lowercase().contains(lowerQuery) ||
                agent.agency?.lowercase()?.contains(lowerQuery) == true ||
                agent.specializations.any { it.lowercase().contains(lowerQuery) }
        }
    }
}

sealed interface AgentListingsEvent {
    data class NavigateToAgent(val agent: Agent) : AgentListingsEvent
}
