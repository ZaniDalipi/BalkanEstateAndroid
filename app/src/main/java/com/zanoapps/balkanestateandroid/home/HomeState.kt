package com.zanoapps.balkanestateandroid.home

import com.zanoapps.agent.domain.model.Agent
import com.zanoapps.core.domain.model.BalkanEstateProperty

data class HomeState(
    val featuredProperties: List<BalkanEstateProperty> = emptyList(),
    val topAgents: List<Agent> = emptyList(),
    val isLoadingProperties: Boolean = false,
    val isLoadingAgents: Boolean = false,
    val searchQuery: String = "",
    val subscriptionEmail: String = "",
    val isSubscribing: Boolean = false,
    val errorMessage: String? = null
)

sealed interface HomeAction {
    data object OnSearchClick : HomeAction
    data class OnPropertyClick(val propertyId: String) : HomeAction
    data class OnCountryClick(val country: String) : HomeAction
    data class OnCityClick(val city: String) : HomeAction
    data class OnAgentClick(val agentId: String) : HomeAction
    data object OnViewAllProperties : HomeAction
    data object OnViewAllAgents : HomeAction
    data class OnSubscribeEmail(val email: String) : HomeAction
    data class OnPropertyTypeClick(val type: String) : HomeAction
    data class OnSearchQueryChanged(val query: String) : HomeAction
}

sealed interface HomeEvent {
    data class NavigateToPropertyDetail(val propertyId: String) : HomeEvent
    data class NavigateToSearch(val query: String = "") : HomeEvent
    data class NavigateToCountry(val country: String) : HomeEvent
    data class NavigateToCity(val city: String) : HomeEvent
    data class NavigateToAgentDetail(val agentId: String) : HomeEvent
    data object NavigateToAllProperties : HomeEvent
    data object NavigateToAllAgents : HomeEvent
    data class NavigateToPropertyType(val type: String) : HomeEvent
    data object SubscriptionSuccess : HomeEvent
    data class SubscriptionError(val message: String) : HomeEvent
}
