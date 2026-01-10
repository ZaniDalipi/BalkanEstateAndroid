package com.zanoapps.property_details.presentation.details

sealed interface PropertyDetailsEvent {
    data object NavigateBack : PropertyDetailsEvent
    data class OpenDialer(val phoneNumber: String) : PropertyDetailsEvent
    data class OpenEmail(val email: String, val subject: String) : PropertyDetailsEvent
    data class OpenWhatsApp(val phoneNumber: String, val message: String) : PropertyDetailsEvent
    data class ShareProperty(val url: String, val title: String) : PropertyDetailsEvent
    data class NavigateToProperty(val propertyId: String) : PropertyDetailsEvent
    data class OpenMap(val latitude: Double, val longitude: Double, val address: String) : PropertyDetailsEvent
    data class Error(val message: String) : PropertyDetailsEvent
}
