package com.zanoapps.agent.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.agent.domain.repository.AgentRepository
import com.zanoapps.core.domain.util.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AgentProfileViewModel(
    private val agentRepository: AgentRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val agentId: String = savedStateHandle.get<String>("agentId") ?: ""

    var state by mutableStateOf(AgentProfileState())
        private set

    private val eventChannel = Channel<AgentProfileEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        if (agentId.isNotEmpty()) {
            observeAgent()
            observeListings()
            refreshData()
        }
    }

    private fun observeAgent() {
        agentRepository.getAgentById(agentId)
            .onEach { agent ->
                state = state.copy(agent = agent, isLoading = false)
            }
            .launchIn(viewModelScope)
    }

    private fun observeListings() {
        agentRepository.getAgentListings(agentId)
            .onEach { listings ->
                state = state.copy(listings = listings)
            }
            .launchIn(viewModelScope)
    }

    private fun refreshData() {
        viewModelScope.launch {
            state = state.copy(isLoading = state.agent == null, isRefreshing = state.agent != null)
            agentRepository.refreshAgent(agentId)
            agentRepository.refreshAgentListings(agentId)
            state = state.copy(isLoading = false, isRefreshing = false)
        }
    }

    fun onAction(action: AgentProfileAction) {
        when (action) {
            AgentProfileAction.OnBackClick -> Unit // Handled by navigation
            AgentProfileAction.OnRefresh -> refreshData()
            AgentProfileAction.OnCallClick -> {
                state.agent?.phone?.let { phone ->
                    viewModelScope.launch {
                        eventChannel.send(AgentProfileEvent.OpenDialer(phone))
                    }
                }
            }
            AgentProfileAction.OnEmailClick -> {
                state.agent?.email?.let { email ->
                    viewModelScope.launch {
                        eventChannel.send(AgentProfileEvent.OpenEmailClient(email))
                    }
                }
            }
            AgentProfileAction.OnShowContactForm -> {
                state = state.copy(isContactFormVisible = true)
            }
            AgentProfileAction.OnHideContactForm -> {
                state = state.copy(isContactFormVisible = false)
            }
            is AgentProfileAction.OnPropertyClick -> {
                viewModelScope.launch {
                    eventChannel.send(AgentProfileEvent.NavigateToProperty(action.property))
                }
            }
            is AgentProfileAction.OnContactNameChange -> {
                state = state.copy(contactName = action.name)
            }
            is AgentProfileAction.OnContactEmailChange -> {
                state = state.copy(contactEmail = action.email)
            }
            is AgentProfileAction.OnContactPhoneChange -> {
                state = state.copy(contactPhone = action.phone)
            }
            is AgentProfileAction.OnContactMessageChange -> {
                state = state.copy(contactMessage = action.message)
            }
            AgentProfileAction.OnSendMessage -> sendContactMessage()
            AgentProfileAction.OnMessageSentDismiss -> {
                state = state.copy(messageSentSuccess = false)
            }
        }
    }

    private fun sendContactMessage() {
        if (state.contactName.isBlank() || state.contactEmail.isBlank() || state.contactMessage.isBlank()) {
            return
        }

        viewModelScope.launch {
            state = state.copy(isSendingMessage = true)
            val result = agentRepository.contactAgent(
                agentId = agentId,
                name = state.contactName,
                email = state.contactEmail,
                phone = state.contactPhone.takeIf { it.isNotBlank() },
                message = state.contactMessage,
                propertyId = null
            )

            when (result) {
                is Result.Success -> {
                    state = state.copy(
                        isSendingMessage = false,
                        isContactFormVisible = false,
                        contactName = "",
                        contactEmail = "",
                        contactPhone = "",
                        contactMessage = "",
                        messageSentSuccess = true
                    )
                    eventChannel.send(AgentProfileEvent.MessageSentSuccess)
                }
                is Result.Error -> {
                    state = state.copy(isSendingMessage = false)
                }
            }
        }
    }
}
