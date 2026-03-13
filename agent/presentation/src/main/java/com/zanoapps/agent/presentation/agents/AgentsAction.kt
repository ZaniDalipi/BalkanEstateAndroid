package com.zanoapps.agent.presentation.agents

import com.zanoapps.agent.domain.model.Agent

sealed interface AgentsAction {
    data object OnLoadAgents : AgentsAction
    data class OnSearchQueryChanged(val query: String) : AgentsAction
    data class OnSortChanged(val sortOption: AgentSortOption) : AgentsAction
    data class OnAgentClick(val agent: Agent) : AgentsAction
    data class OnContactAgent(val agent: Agent) : AgentsAction
    data class OnSpecializationFilter(val specialization: String?) : AgentsAction
    data class OnLocationFilter(val location: String?) : AgentsAction
}
