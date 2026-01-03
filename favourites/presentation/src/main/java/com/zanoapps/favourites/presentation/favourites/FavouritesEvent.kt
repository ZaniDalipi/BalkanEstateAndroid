package com.zanoapps.favourites.presentation.favourites

import com.zanoapps.presentation.ui.UiText

sealed interface FavouritesEvent {
    data class Error(val error: UiText) : FavouritesEvent
    data object PropertyRemoved : FavouritesEvent
}
