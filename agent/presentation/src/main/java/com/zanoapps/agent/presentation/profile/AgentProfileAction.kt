package com.zanoapps.agent.presentation.profile

import com.zanoapps.core.domain.model.BalkanEstateProperty

sealed interface AgentProfileAction {
    data object OnBackClick : AgentProfileAction
    data object OnRefresh : AgentProfileAction
    data object OnCallClick : AgentProfileAction
    data object OnEmailClick : AgentProfileAction
    data object OnShowContactForm : AgentProfileAction
    data object OnHideContactForm : AgentProfileAction
    data class OnPropertyClick(val property: BalkanEstateProperty) : AgentProfileAction
    data class OnContactNameChange(val name: String) : AgentProfileAction
    data class OnContactEmailChange(val email: String) : AgentProfileAction
    data class OnContactPhoneChange(val phone: String) : AgentProfileAction
    data class OnContactMessageChange(val message: String) : AgentProfileAction
    data object OnSendMessage : AgentProfileAction
    data object OnMessageSentDismiss : AgentProfileAction
}
