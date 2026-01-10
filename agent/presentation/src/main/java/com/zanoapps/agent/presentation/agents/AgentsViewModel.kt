package com.zanoapps.agent.presentation.agents

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        loadAgencies()
    }

    fun onAction(action: AgentsAction) {
        when (action) {
            AgentsAction.LoadAgents -> loadAgents()
            AgentsAction.LoadAgencies -> loadAgencies()
            is AgentsAction.OnTabSelected -> {
                state = state.copy(selectedTab = action.tab)
            }
            is AgentsAction.OnSearchQueryChanged -> {
                state = state.copy(searchQuery = action.query)
            }
            is AgentsAction.OnAgentClick -> {
                viewModelScope.launch {
                    eventChannel.send(AgentsEvent.NavigateToAgentDetails(action.agent.id))
                }
            }
            is AgentsAction.OnAgencyClick -> {
                viewModelScope.launch {
                    eventChannel.send(AgentsEvent.NavigateToAgencyDetails(action.agency.id))
                }
            }
            is AgentsAction.OnContactAgent -> {
                state = state.copy(
                    selectedAgent = action.agent,
                    isContactDialogVisible = true
                )
            }
            is AgentsAction.OnContactAgency -> {
                state = state.copy(
                    selectedAgency = action.agency,
                    isContactDialogVisible = true
                )
            }
            AgentsAction.OnDismissContactDialog -> {
                state = state.copy(
                    selectedAgent = null,
                    selectedAgency = null,
                    isContactDialogVisible = false
                )
            }
            is AgentsAction.OnCallClick -> {
                viewModelScope.launch {
                    eventChannel.send(AgentsEvent.OpenDialer(action.phoneNumber))
                }
            }
            is AgentsAction.OnEmailClick -> {
                viewModelScope.launch {
                    eventChannel.send(AgentsEvent.OpenEmail(action.email))
                }
            }
        }
    }

    private fun loadAgents() {
        viewModelScope.launch {
            state = state.copy(isLoadingAgents = true)
            val agents = getMockAgents()
            state = state.copy(
                agents = agents,
                isLoadingAgents = false
            )
        }
    }

    private fun loadAgencies() {
        viewModelScope.launch {
            state = state.copy(isLoadingAgencies = true)
            val agencies = getMockAgencies()
            state = state.copy(
                agencies = agencies,
                isLoadingAgencies = false
            )
        }
    }

    private fun getMockAgents(): List<Agent> {
        return listOf(
            Agent(
                id = "agent1",
                name = "Marina Lleshi",
                avatarUrl = null,
                agency = "Balkan Estate Agency",
                specialization = "Luxury Properties",
                rating = 4.9f,
                reviewCount = 127,
                listingsCount = 45,
                soldCount = 89,
                phone = "+355 69 111 2222",
                email = "marina@balkanestate.com",
                isVerified = true,
                yearsExperience = 12
            ),
            Agent(
                id = "agent2",
                name = "Besmir Kola",
                avatarUrl = null,
                agency = "Prime Realty",
                specialization = "Residential Sales",
                rating = 4.8f,
                reviewCount = 98,
                listingsCount = 32,
                soldCount = 156,
                phone = "+355 69 222 3333",
                email = "besmir@primerealty.al",
                isVerified = true,
                yearsExperience = 8
            ),
            Agent(
                id = "agent3",
                name = "Elira Hoxha",
                avatarUrl = null,
                agency = "City Properties",
                specialization = "Commercial Real Estate",
                rating = 4.7f,
                reviewCount = 76,
                listingsCount = 28,
                soldCount = 67,
                phone = "+355 69 333 4444",
                email = "elira@cityproperties.al",
                isVerified = true,
                yearsExperience = 6
            ),
            Agent(
                id = "agent4",
                name = "Andi Basha",
                avatarUrl = null,
                agency = "Balkan Estate Agency",
                specialization = "Rentals & Leasing",
                rating = 4.6f,
                reviewCount = 54,
                listingsCount = 67,
                soldCount = 34,
                phone = "+355 69 444 5555",
                email = "andi@balkanestate.com",
                isVerified = true,
                yearsExperience = 4
            )
        )
    }

    private fun getMockAgencies(): List<Agency> {
        return listOf(
            Agency(
                id = "agency1",
                name = "Balkan Estate Agency",
                logoUrl = null,
                address = "Rruga Ismail Qemali 15",
                city = "Tirana",
                agentCount = 24,
                listingsCount = 156,
                rating = 4.8f,
                reviewCount = 342,
                phone = "+355 4 222 3333",
                email = "info@balkanestate.com",
                website = "www.balkanestate.com",
                isVerified = true
            ),
            Agency(
                id = "agency2",
                name = "Prime Realty Albania",
                logoUrl = null,
                address = "Blloku, Rruga Ibrahim Rugova",
                city = "Tirana",
                agentCount = 18,
                listingsCount = 98,
                rating = 4.7f,
                reviewCount = 215,
                phone = "+355 4 333 4444",
                email = "contact@primerealty.al",
                website = "www.primerealty.al",
                isVerified = true
            ),
            Agency(
                id = "agency3",
                name = "City Properties",
                logoUrl = null,
                address = "Rruga Barrikadave 45",
                city = "Tirana",
                agentCount = 12,
                listingsCount = 67,
                rating = 4.5f,
                reviewCount = 128,
                phone = "+355 4 444 5555",
                email = "hello@cityproperties.al",
                website = "www.cityproperties.al",
                isVerified = true
            )
        )
    }
}
