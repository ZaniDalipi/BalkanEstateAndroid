package com.zanoapps.agent.presentation.listings

import com.zanoapps.agent.domain.model.Agent

data class AgentListingsState(
    val agents: List<Agent> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = ""
)
