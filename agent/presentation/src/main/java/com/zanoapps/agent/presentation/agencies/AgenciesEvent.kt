package com.zanoapps.agent.presentation.agencies

import com.zanoapps.presentation.ui.UiText

sealed interface AgenciesEvent {
    data class Error(val error: UiText) : AgenciesEvent
    data class NavigateToAgencyDetail(val agencyId: String) : AgenciesEvent
}
