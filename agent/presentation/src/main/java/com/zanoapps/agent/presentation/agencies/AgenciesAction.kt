package com.zanoapps.agent.presentation.agencies

import com.zanoapps.agent.domain.model.Agency

sealed interface AgenciesAction {
    data object OnLoadAgencies : AgenciesAction
    data class OnSearchQueryChanged(val query: String) : AgenciesAction
    data class OnAgencyClick(val agency: Agency) : AgenciesAction
    data class OnContactAgency(val agency: Agency) : AgenciesAction
}
