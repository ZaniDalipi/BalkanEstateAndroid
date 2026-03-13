package com.zanoapps.property_details.presentation.create

import com.zanoapps.presentation.ui.UiText

sealed interface CreateListingEvent {
    data class Error(val error: UiText) : CreateListingEvent
    data object ListingCreated : CreateListingEvent
    data object NavigateBack : CreateListingEvent
}
