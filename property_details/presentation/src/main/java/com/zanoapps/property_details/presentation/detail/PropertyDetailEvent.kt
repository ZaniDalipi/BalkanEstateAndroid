package com.zanoapps.property_details.presentation.detail

import com.zanoapps.presentation.ui.UiText

sealed interface PropertyDetailEvent {
    data class Error(val error: UiText) : PropertyDetailEvent
    data object NavigateBack : PropertyDetailEvent
    data class NavigateToPropertyDetail(val propertyId: String) : PropertyDetailEvent
    data object MessageSentSuccess : PropertyDetailEvent
    data class ShareProperty(val url: String) : PropertyDetailEvent
    data class OpenDialer(val phoneNumber: String) : PropertyDetailEvent
    data class OpenEmail(val email: String) : PropertyDetailEvent
    data class OpenDirections(val latitude: Double, val longitude: Double) : PropertyDetailEvent
    data class OpenVirtualTour(val propertyId: String) : PropertyDetailEvent
    data class OpenScheduleTour(val propertyId: String, val agentName: String) : PropertyDetailEvent
}
