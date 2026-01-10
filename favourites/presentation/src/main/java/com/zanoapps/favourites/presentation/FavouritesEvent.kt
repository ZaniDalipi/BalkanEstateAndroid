package com.zanoapps.favourites.presentation

sealed interface FavouritesEvent {
    data class NavigateToPropertyDetails(val propertyId: String) : FavouritesEvent
    data class Error(val message: String) : FavouritesEvent
    data object FavouriteRemoved : FavouritesEvent
    data object AllFavouritesCleared : FavouritesEvent
}
