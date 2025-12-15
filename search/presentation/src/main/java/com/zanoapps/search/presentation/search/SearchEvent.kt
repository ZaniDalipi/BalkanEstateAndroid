package com.zanoapps.search.presentation.search

import com.zanoapps.core.presentation.ui.UiText

sealed interface SearchEvent {
    data class Error(val error: UiText) : SearchEvent
    data object NavigateToPropertyDetails : SearchEvent
    data object NavigateToFilters : SearchEvent
    data object NavigateToSavedSearches : SearchEvent
    data object NavigateToCreateListing : SearchEvent
    data object SubscriptionSuccess : SearchEvent
    data class SubscriptionError(val error: UiText) : SearchEvent
}
