package com.zanoapps.agent.presentation.agents

sealed interface AgentsEvent {
    data class NavigateToAgentDetails(val agentId: String) : AgentsEvent
    data class NavigateToAgencyDetails(val agencyId: String) : AgentsEvent
    data class OpenDialer(val phoneNumber: String) : AgentsEvent
    data class OpenEmail(val email: String) : AgentsEvent
    data class Error(val message: String) : AgentsEvent
}
