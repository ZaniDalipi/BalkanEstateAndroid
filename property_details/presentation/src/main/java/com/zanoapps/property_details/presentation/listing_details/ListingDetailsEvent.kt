package com.zanoapps.property_details.presentation.listing_details

import com.zanoapps.presentation.ui.UiText

sealed interface ListingDetailsEvent {
    // Navigation events
    data object NavigateBack : ListingDetailsEvent

    // Error events
    data class Error(val error: UiText) : ListingDetailsEvent

    // Success events
    data object ContactFormSubmitted : ListingDetailsEvent
    data object TourRequestSubmitted : ListingDetailsEvent
    data object AddedToFavorites : ListingDetailsEvent
    data object RemovedFromFavorites : ListingDetailsEvent

    // Share events
    data class ShareProperty(val url: String, val title: String) : ListingDetailsEvent
    data object LinkCopied : ListingDetailsEvent

    // External actions
    data class OpenDialer(val phoneNumber: String) : ListingDetailsEvent
    data class OpenMap(val latitude: Double, val longitude: Double, val address: String) : ListingDetailsEvent
    data class OpenEmail(val email: String, val subject: String) : ListingDetailsEvent
}
