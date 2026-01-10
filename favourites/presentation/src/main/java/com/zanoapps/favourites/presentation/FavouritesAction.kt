package com.zanoapps.favourites.presentation

import com.zanoapps.core.domain.model.BalkanEstateProperty

sealed interface FavouritesAction {
    data object LoadFavourites : FavouritesAction
    data object RefreshFavourites : FavouritesAction
    data class OnPropertyClick(val property: BalkanEstateProperty) : FavouritesAction
    data class OnRemoveFavourite(val propertyId: String) : FavouritesAction
    data object OnToggleViewMode : FavouritesAction
    data object OnClearAll : FavouritesAction
    data class OnViewDetails(val property: BalkanEstateProperty) : FavouritesAction
}
