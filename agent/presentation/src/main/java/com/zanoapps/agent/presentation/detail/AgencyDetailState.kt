package com.zanoapps.agent.presentation.detail

import com.zanoapps.agent.domain.model.Agency
import com.zanoapps.presentation.ui.UiText

data class AgencyDetailState(
    val agency: Agency? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface AgencyDetailAction {
    data object OnBackClick : AgencyDetailAction
    data object OnCallClick : AgencyDetailAction
    data object OnEmailClick : AgencyDetailAction
    data object OnWebsiteClick : AgencyDetailAction
    data object OnRetry : AgencyDetailAction
}

sealed interface AgencyDetailEvent {
    data object NavigateBack : AgencyDetailEvent
    data class Call(val phone: String) : AgencyDetailEvent
    data class Email(val email: String) : AgencyDetailEvent
    data class OpenWebsite(val url: String) : AgencyDetailEvent
    data class Error(val error: UiText) : AgencyDetailEvent
}
