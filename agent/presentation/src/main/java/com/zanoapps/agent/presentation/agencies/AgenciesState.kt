package com.zanoapps.agent.presentation.agencies

import com.zanoapps.agent.domain.model.Agency

data class AgenciesState(
    val agencies: List<Agency> = emptyList(),
    val filteredAgencies: List<Agency> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val errorMessage: String? = null
)
