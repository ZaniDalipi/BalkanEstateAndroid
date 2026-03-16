package com.zanoapps.agent.presentation.agencies

import com.zanoapps.presentation.ui.UiText

sealed interface AgenciesEvent {
    data class Error(val error: UiText) : AgenciesEvent
    data class NavigateToAgencyDetail(val agencyId: String) : AgenciesEvent
    data class ContactAgency(val phone: String, val email: String) : AgenciesEvent
}
