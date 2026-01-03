package com.zanoapps.property_details.presentation.property_detail

import com.zanoapps.presentation.ui.UiText

sealed interface PropertyDetailEvent {
    data class Error(val error: UiText) : PropertyDetailEvent
    data object PropertyAddedToFavorites : PropertyDetailEvent
    data object PropertyRemovedFromFavorites : PropertyDetailEvent
    data object PropertyShared : PropertyDetailEvent
}
