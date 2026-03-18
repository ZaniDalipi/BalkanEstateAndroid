package com.zanoapps.balkanestateandroid.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.agent.domain.repository.AgentRepository
import com.zanoapps.search.domain.repository.PropertyRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val propertyRepository: PropertyRepository,
    private val agentRepository: AgentRepository
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadFeaturedProperties()
        loadTopAgents()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnSearchClick -> {
                viewModelScope.launch {
                    eventChannel.send(HomeEvent.NavigateToSearch(state.searchQuery))
                }
            }

            is HomeAction.OnPropertyClick -> {
                viewModelScope.launch {
                    eventChannel.send(HomeEvent.NavigateToPropertyDetail(action.propertyId))
                }
            }

            is HomeAction.OnCountryClick -> {
                viewModelScope.launch {
                    eventChannel.send(HomeEvent.NavigateToCountry(action.country))
                }
            }

            is HomeAction.OnCityClick -> {
                viewModelScope.launch {
                    eventChannel.send(HomeEvent.NavigateToCity(action.city))
                }
            }

            is HomeAction.OnAgentClick -> {
                viewModelScope.launch {
                    eventChannel.send(HomeEvent.NavigateToAgentDetail(action.agentId))
                }
            }

            HomeAction.OnViewAllProperties -> {
                viewModelScope.launch {
                    eventChannel.send(HomeEvent.NavigateToAllProperties)
                }
            }

            HomeAction.OnViewAllAgents -> {
                viewModelScope.launch {
                    eventChannel.send(HomeEvent.NavigateToAllAgents)
                }
            }

            is HomeAction.OnSubscribeEmail -> {
                subscribeEmail(action.email)
            }

            is HomeAction.OnPropertyTypeClick -> {
                viewModelScope.launch {
                    eventChannel.send(HomeEvent.NavigateToPropertyType(action.type))
                }
            }

            is HomeAction.OnSearchQueryChanged -> {
                state = state.copy(searchQuery = action.query)
            }
        }
    }

    private fun loadFeaturedProperties() {
        state = state.copy(isLoadingProperties = true)
        propertyRepository.getProperties()
            .onEach { properties ->
                state = state.copy(
                    featuredProperties = properties.filter { it.isFeatured }.take(10),
                    isLoadingProperties = false
                )
            }
            .launchIn(viewModelScope)
    }

    private fun loadTopAgents() {
        state = state.copy(isLoadingAgents = true)
        agentRepository.getAgents()
            .onEach { agents ->
                state = state.copy(
                    topAgents = agents.sortedByDescending { it.rating }.take(10),
                    isLoadingAgents = false
                )
            }
            .launchIn(viewModelScope)
    }

    private fun subscribeEmail(email: String) {
        if (email.isBlank() || !email.contains("@")) {
            viewModelScope.launch {
                eventChannel.send(HomeEvent.SubscriptionError("Please enter a valid email address"))
            }
            return
        }
        state = state.copy(isSubscribing = true)
        viewModelScope.launch {
            state = state.copy(
                isSubscribing = false,
                subscriptionEmail = ""
            )
            eventChannel.send(HomeEvent.SubscriptionSuccess)
        }
    }
}
