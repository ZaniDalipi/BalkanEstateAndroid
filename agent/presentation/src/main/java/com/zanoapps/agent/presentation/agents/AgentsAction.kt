package com.zanoapps.agent.presentation.agents

sealed interface AgentsAction {
    data object LoadAgents : AgentsAction
    data object LoadAgencies : AgentsAction
    data class OnTabSelected(val tab: AgentTab) : AgentsAction
    data class OnSearchQueryChanged(val query: String) : AgentsAction
    data class OnAgentClick(val agent: Agent) : AgentsAction
    data class OnAgencyClick(val agency: Agency) : AgentsAction
    data class OnContactAgent(val agent: Agent) : AgentsAction
    data class OnContactAgency(val agency: Agency) : AgentsAction
    data object OnDismissContactDialog : AgentsAction
    data class OnCallClick(val phoneNumber: String) : AgentsAction
    data class OnEmailClick(val email: String) : AgentsAction
}
