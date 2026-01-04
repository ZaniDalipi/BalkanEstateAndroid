package com.zanoapps.agent.presentation.listings

import com.zanoapps.agent.domain.model.Agent

sealed interface AgentListingsAction {
    data object OnBackClick : AgentListingsAction
    data object OnRefresh : AgentListingsAction
    data class OnAgentClick(val agent: Agent) : AgentListingsAction
    data class OnSearchQueryChange(val query: String) : AgentListingsAction
}
