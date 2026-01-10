package com.zanoapps.property_details.presentation.create

sealed interface CreateListingEvent {
    data object NavigateBack : CreateListingEvent
    data object ListingCreated : CreateListingEvent
    data object DraftSaved : CreateListingEvent
    data class Error(val message: String) : CreateListingEvent
    data class ValidationError(val field: String, val message: String) : CreateListingEvent
}
