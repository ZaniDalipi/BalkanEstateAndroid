package com.zanoapps.favourites.presentation.compare

import com.zanoapps.presentation.ui.UiText

sealed interface PropertyComparisonEvent {
    data class Error(val error: UiText) : PropertyComparisonEvent
    data object NavigateBack : PropertyComparisonEvent
    data class NavigateToPropertyDetail(val propertyId: String) : PropertyComparisonEvent
}
