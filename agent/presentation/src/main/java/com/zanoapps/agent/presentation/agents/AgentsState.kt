package com.zanoapps.agent.presentation.agents

import com.zanoapps.agent.domain.model.Agent

data class AgentsState(
    val agents: List<Agent> = emptyList(),
    val filteredAgents: List<Agent> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedSpecialization: String? = null,
    val selectedLocation: String? = null,
    val sortOption: AgentSortOption = AgentSortOption.TOP_RATED,
    val errorMessage: String? = null
)

enum class AgentSortOption(val displayName: String) {
    TOP_RATED("Top Rated"),
    MOST_LISTINGS("Most Listings"),
    MOST_SOLD("Most Sold"),
    MOST_EXPERIENCED("Most Experienced")
}
