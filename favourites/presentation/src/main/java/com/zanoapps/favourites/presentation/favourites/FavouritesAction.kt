package com.zanoapps.favourites.presentation.favourites

import com.zanoapps.core.domain.model.BalkanEstateProperty

sealed interface FavouritesAction {
    data object OnBackClick : FavouritesAction
    data class OnPropertyClick(val property: BalkanEstateProperty) : FavouritesAction
    data class OnRemoveFromFavourites(val propertyId: String) : FavouritesAction
    data object OnRefresh : FavouritesAction
}
