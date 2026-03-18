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

class AgencyDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val agentRepository: AgentRepository
) : ViewModel() {

    var state by mutableStateOf(AgencyDetailState())
        private set

    private val eventChannel = Channel<AgencyDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    private val agencyId: String = savedStateHandle.get<String>("agencyId") ?: ""

    init {
        loadAgency()
    }

    fun onAction(action: AgencyDetailAction) {
        when (action) {
            AgencyDetailAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(AgencyDetailEvent.NavigateBack)
                }
            }
            AgencyDetailAction.OnCallClick -> {
                state.agency?.let { agency ->
                    viewModelScope.launch {
                        eventChannel.send(AgencyDetailEvent.Call(agency.phone))
                    }
                }
            }
            AgencyDetailAction.OnEmailClick -> {
                state.agency?.let { agency ->
                    viewModelScope.launch {
                        eventChannel.send(AgencyDetailEvent.Email(agency.email))
                    }
                }
            }
            AgencyDetailAction.OnWebsiteClick -> {
                state.agency?.let { agency ->
                    viewModelScope.launch {
                        eventChannel.send(AgencyDetailEvent.OpenWebsite(agency.website))
                    }
                }
            }
            AgencyDetailAction.OnRetry -> loadAgency()
        }
    }

    private fun loadAgency() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null)
            when (val result = agentRepository.getAgencyById(agencyId)) {
                is Result.Success -> {
                    state = state.copy(
                        agency = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        isLoading = false,
                        errorMessage = "Failed to load agency details"
                    )
                    eventChannel.send(
                        AgencyDetailEvent.Error(
                            UiText.DynamicString("Failed to load agency details")
                        )
                    )
                }
            }
        }
    }
}
