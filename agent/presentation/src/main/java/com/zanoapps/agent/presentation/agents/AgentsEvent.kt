package com.zanoapps.agent.presentation.agents

import com.zanoapps.presentation.ui.UiText

sealed interface AgentsEvent {
    data class Error(val error: UiText) : AgentsEvent
    data class NavigateToAgentDetail(val agentId: String) : AgentsEvent
    data class NavigateToContactAgent(val agentId: String) : AgentsEvent
}
