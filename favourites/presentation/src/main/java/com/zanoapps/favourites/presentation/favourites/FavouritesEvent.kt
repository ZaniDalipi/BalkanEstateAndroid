package com.zanoapps.favourites.presentation.favourites

import com.zanoapps.presentation.ui.UiText

sealed interface FavouritesEvent {
    data class Error(val error: UiText) : FavouritesEvent
    data class NavigateToPropertyDetail(val propertyId: String) : FavouritesEvent
    data class NavigateToCompare(val propertyIds: List<String>) : FavouritesEvent
    data class PropertyRemoved(val propertyId: String) : FavouritesEvent
}
