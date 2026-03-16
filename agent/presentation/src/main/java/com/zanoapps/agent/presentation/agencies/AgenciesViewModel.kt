package com.zanoapps.agent.presentation.agencies

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

class AgenciesViewModel(
    private val agentRepository: AgentRepository
) : ViewModel() {

    var state by mutableStateOf(AgenciesState())
        private set

    private val eventChannel = Channel<AgenciesEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadAgencies()
    }

    fun onAction(action: AgenciesAction) {
        when (action) {
            AgenciesAction.OnLoadAgencies -> loadAgencies()
            is AgenciesAction.OnSearchQueryChanged -> {
                state = state.copy(searchQuery = action.query)
                filterAgencies()
            }
            is AgenciesAction.OnAgencyClick -> {
                viewModelScope.launch {
                    eventChannel.send(AgenciesEvent.NavigateToAgencyDetail(action.agency.id))
                }
            }
            is AgenciesAction.OnContactAgency -> {
                viewModelScope.launch {
                    eventChannel.send(
                        AgenciesEvent.ContactAgency(
                            phone = action.agency.phone,
                            email = action.agency.email
                        )
                    )
                }
            }
        }
    }

    private fun loadAgencies() {
        state = state.copy(isLoading = true)
        agentRepository.getAgencies()
            .onEach { agencies ->
                state = state.copy(
                    agencies = agencies,
                    filteredAgencies = agencies,
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }

    private fun filterAgencies() {
        val query = state.searchQuery.lowercase()
        val filtered = if (query.isEmpty()) state.agencies else state.agencies.filter {
            it.name.lowercase().contains(query) || it.city.lowercase().contains(query)
        }
        state = state.copy(filteredAgencies = filtered)
    }
}
