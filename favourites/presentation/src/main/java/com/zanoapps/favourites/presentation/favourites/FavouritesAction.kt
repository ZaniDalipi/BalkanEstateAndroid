package com.zanoapps.favourites.presentation.favourites

import com.zanoapps.core.domain.model.BalkanEstateProperty

sealed interface FavouritesAction {
    data object OnLoadFavourites : FavouritesAction
    data class OnRemoveFavourite(val propertyId: String) : FavouritesAction
    data class OnPropertyClick(val property: BalkanEstateProperty) : FavouritesAction
    data class OnSortChanged(val sortOption: FavouritesSortOption) : FavouritesAction
    data class OnSearchQueryChanged(val query: String) : FavouritesAction
    data object OnToggleCompareMode : FavouritesAction
    data class OnToggleCompareSelection(val propertyId: String) : FavouritesAction
    data object OnCompareSelected : FavouritesAction
    data class OnViewDetails(val property: BalkanEstateProperty) : FavouritesAction
}
