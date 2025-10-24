package com.zanoapps.search.presentation.search

import com.zanoapps.core.domain.enums.SortOption
import com.zanoapps.search.domain.model.MapLocation
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.search.domain.model.SearchFilters

sealed interface SearchAction {

    data class OnMapMoved(val location: MapLocation) : SearchAction
    data class OnMarkerClicked(val balkanEstateProperty: BalkanEstateProperty) : SearchAction

    data object OnMyLocationClick : SearchAction
    data object OnMapTypeToggle : SearchAction

    data class OnSearchQueryChanged(val query: String) : SearchAction
    data object OnSearchSubmit : SearchAction

    data class OnFiltersApplied(val filters: SearchFilters) : SearchAction
    data object OnClearFilters : SearchAction

    data class OnSortChanged(val sortOption: SortOption) : SearchAction

    data class OnPropertyClicked(val balkanEstateProperty: BalkanEstateProperty) : SearchAction
    data class OnFavoriteToggle(val propertyId: String) : SearchAction
    data object OnRefreshProperties : SearchAction

    data object OnExpandBottomSheet : SearchAction
    data object OnCollapseBottomSheet : SearchAction

    data object OnSaveSearchClick : SearchAction
    data class OnSaveSearch(val searchName: String, val enabledNotifications: Boolean) :
        SearchAction

    data class OnLoadSavedSearch(val searchId: Long) : SearchAction
    data class OnDeleteSavedSearch(val searchId: Long) : SearchAction
    data object OnViewSavedSearches : SearchAction

    data object OnCreateListingClick : SearchAction
    data object OnFilterClick : SearchAction


}