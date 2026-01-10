package com.zanoapps.agent.presentation.agents

data class Agent(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val agency: String,
    val specialization: String,
    val rating: Float,
    val reviewCount: Int,
    val listingsCount: Int,
    val soldCount: Int,
    val phone: String,
    val email: String,
    val isVerified: Boolean = true,
    val yearsExperience: Int
)

data class Agency(
    val id: String,
    val name: String,
    val logoUrl: String?,
    val address: String,
    val city: String,
    val agentCount: Int,
    val listingsCount: Int,
    val rating: Float,
    val reviewCount: Int,
    val phone: String,
    val email: String,
    val website: String?,
    val isVerified: Boolean = true
)

data class AgentsState(
    val agents: List<Agent> = emptyList(),
    val agencies: List<Agency> = emptyList(),
    val isLoadingAgents: Boolean = false,
    val isLoadingAgencies: Boolean = false,
    val selectedTab: AgentTab = AgentTab.AGENTS,
    val searchQuery: String = "",
    val selectedAgent: Agent? = null,
    val selectedAgency: Agency? = null,
    val isContactDialogVisible: Boolean = false,
    val errorMessage: String? = null
)

enum class AgentTab {
    AGENTS,
    AGENCIES
}
