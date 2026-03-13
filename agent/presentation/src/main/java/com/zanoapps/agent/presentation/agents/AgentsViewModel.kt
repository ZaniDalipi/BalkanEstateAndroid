package com.zanoapps.agent.presentation.agents

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.agent.domain.model.Agent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AgentsViewModel : ViewModel() {

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
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val agents = getMockAgents()
            state = state.copy(
                agents = agents,
                filteredAgents = agents,
                isLoading = false
            )
        }
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

    private fun getMockAgents(): List<Agent> = listOf(
        Agent("a1", "Besmir Kola", agency = "Balkan Property Group", specialization = "Residential", location = "Tirana", rating = 4.9f, reviewsCount = 156, listingsCount = 42, soldCount = 89, yearsExperience = 12, languages = listOf("Albanian", "English", "Italian"), isVerified = true, isPremium = true, bio = "Specializing in luxury residential properties in Tirana."),
        Agent("a2", "Eglantina Dervishi", agency = "Elite Realty", specialization = "Commercial", location = "Tirana", rating = 4.8f, reviewsCount = 98, listingsCount = 35, soldCount = 67, yearsExperience = 8, languages = listOf("Albanian", "English", "Greek"), isVerified = true, isPremium = true, bio = "Expert in commercial real estate and investment properties."),
        Agent("a3", "Arben Dedja", agency = "Balkan Property Group", specialization = "Luxury", location = "Durrës", rating = 4.7f, reviewsCount = 124, listingsCount = 28, soldCount = 54, yearsExperience = 15, languages = listOf("Albanian", "English"), isVerified = true, isPremium = false, bio = "Luxury property specialist with extensive market knowledge."),
        Agent("a4", "Mirela Hoxha", agency = "Sunshine Properties", specialization = "Residential", location = "Vlorë", rating = 4.6f, reviewsCount = 87, listingsCount = 23, soldCount = 45, yearsExperience = 6, languages = listOf("Albanian", "English", "French"), isVerified = true, isPremium = false, bio = "Dedicated to finding the perfect home for every client."),
        Agent("a5", "Gentian Leka", agency = "Adriatic Real Estate", specialization = "Land", location = "Sarandë", rating = 4.5f, reviewsCount = 65, listingsCount = 19, soldCount = 38, yearsExperience = 10, languages = listOf("Albanian", "English", "German"), isVerified = true, isPremium = true, bio = "Specialized in coastal properties and land development."),
        Agent("a6", "Klodian Mëhilli", agency = "Elite Realty", specialization = "Commercial", location = "Tirana", rating = 4.4f, reviewsCount = 56, listingsCount = 31, soldCount = 42, yearsExperience = 7, languages = listOf("Albanian", "English"), isVerified = false, isPremium = false, bio = "Commercial and office space specialist in central Tirana.")
    )
}
