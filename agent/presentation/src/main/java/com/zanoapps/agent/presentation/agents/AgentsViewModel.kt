package com.zanoapps.agent.presentation.agents

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.agent.domain.repository.AgentRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AgentsViewModel(
    private val agentRepository: AgentRepository
) : ViewModel() {

    var state by mutableStateOf(AgentsState())
        private set

    private val eventChannel = Channel<AgentsEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadAgents()
    }

    fun onAction(action: AgentsAction) {
        when (action) {
            AgentsAction.OnLoadAgents -> loadAgents()
            is AgentsAction.OnSearchQueryChanged -> {
                state = state.copy(searchQuery = action.query)
                filterAgents()
            }
            is AgentsAction.OnSortChanged -> {
                state = state.copy(sortOption = action.sortOption)
                sortAgents()
            }
            is AgentsAction.OnAgentClick -> {
                viewModelScope.launch {
                    eventChannel.send(AgentsEvent.NavigateToAgentDetail(action.agent.id))
                }
            }
            is AgentsAction.OnContactAgent -> {
                viewModelScope.launch {
                    eventChannel.send(AgentsEvent.NavigateToContactAgent(action.agent.id))
                }
            }
            is AgentsAction.OnSpecializationFilter -> {
                state = state.copy(selectedSpecialization = action.specialization)
                filterAgents()
            }
            is AgentsAction.OnLocationFilter -> {
                state = state.copy(selectedLocation = action.location)
                filterAgents()
            }
        }
    }

    private fun loadAgents() {
        state = state.copy(isLoading = true)
        agentRepository.getAgents()
            .onEach { agents ->
                state = state.copy(
                    agents = agents,
                    filteredAgents = agents,
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }

    private fun filterAgents() {
        val query = state.searchQuery.lowercase()
        val filtered = state.agents.filter { agent ->
            val matchesQuery = query.isEmpty() ||
                    agent.name.lowercase().contains(query) ||
                    agent.agency.lowercase().contains(query) ||
                    agent.location.lowercase().contains(query)
            val matchesSpec = state.selectedSpecialization == null ||
                    agent.specialization == state.selectedSpecialization
            val matchesLocation = state.selectedLocation == null ||
                    agent.location == state.selectedLocation
            matchesQuery && matchesSpec && matchesLocation
        }
        state = state.copy(filteredAgents = filtered)
    }

    private fun sortAgents() {
        val sorted = when (state.sortOption) {
            AgentSortOption.TOP_RATED -> state.filteredAgents.sortedByDescending { it.rating }
            AgentSortOption.MOST_LISTINGS -> state.filteredAgents.sortedByDescending { it.listingsCount }
            AgentSortOption.MOST_SOLD -> state.filteredAgents.sortedByDescending { it.soldCount }
            AgentSortOption.MOST_EXPERIENCED -> state.filteredAgents.sortedByDescending { it.yearsExperience }
        }
        state = state.copy(filteredAgents = sorted)
    }
}
