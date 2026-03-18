package com.zanoapps.agent.presentation.detail

import com.zanoapps.agent.domain.model.Agent
import com.zanoapps.presentation.ui.UiText

data class AgentDetailState(
    val agent: Agent? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface AgentDetailAction {
    data object OnBackClick : AgentDetailAction
    data object OnCallClick : AgentDetailAction
    data object OnEmailClick : AgentDetailAction
    data object OnMessageClick : AgentDetailAction
    data object OnViewListingsClick : AgentDetailAction
    data object OnRetry : AgentDetailAction
}

sealed interface AgentDetailEvent {
    data object NavigateBack : AgentDetailEvent
    data class Call(val phone: String) : AgentDetailEvent
    data class Email(val email: String) : AgentDetailEvent
    data class Message(val agentId: String) : AgentDetailEvent
    data class NavigateToListings(val agentId: String) : AgentDetailEvent
    data class Error(val error: UiText) : AgentDetailEvent
}
