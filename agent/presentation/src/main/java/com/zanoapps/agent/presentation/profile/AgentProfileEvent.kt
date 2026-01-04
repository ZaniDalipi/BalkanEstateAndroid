package com.zanoapps.agent.presentation.profile

import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.ui.UiText

sealed interface AgentProfileEvent {
    data class Error(val message: UiText) : AgentProfileEvent
    data class OpenDialer(val phoneNumber: String) : AgentProfileEvent
    data class OpenEmailClient(val email: String) : AgentProfileEvent
    data class NavigateToProperty(val property: BalkanEstateProperty) : AgentProfileEvent
    data object MessageSentSuccess : AgentProfileEvent
}
