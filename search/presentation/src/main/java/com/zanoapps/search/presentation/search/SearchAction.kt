package com.zanoapps.search.presentation.search

import com.zanoapps.core.domain.enums.SortOption
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.search.domain.model.MapLocation
import com.zanoapps.search.domain.model.SearchFilters

sealed interface SearchAction {
    
    // Map Actions
    data class OnMapMoved(val location: MapLocation) : SearchAction
    data class OnMarkerClicked(val property: BalkanEstateProperty) : SearchAction
    data object OnMyLocationClick : SearchAction
    data object OnMapTypeToggle : SearchAction
    data object OnDrawModeToggle : SearchAction
    
    // Search Actions
    data class OnSearchQueryChanged(val query: String) : SearchAction
    data object OnSearchSubmit : SearchAction
    
    // Filter Actions
    data class OnFiltersApplied(val filters: SearchFilters) : SearchAction
    data object OnClearFilters : SearchAction
    data object OnFilterClick : SearchAction
    
    // Sort Actions
    data class OnSortChanged(val sortOption: SortOption) : SearchAction
    
    // Property Actions
    data class OnPropertyClicked(val property: BalkanEstateProperty) : SearchAction
    data class OnFavoriteToggle(val propertyId: String) : SearchAction
    data object OnRefreshProperties : SearchAction
    
    // View Mode Actions
    data object OnToggleViewMode : SearchAction
    
    // Bottom Sheet Actions
    data object OnExpandBottomSheet : SearchAction
    data object OnCollapseBottomSheet : SearchAction
    
    // Saved Search Actions
    data object OnSaveSearchClick : SearchAction
    data class OnSaveSearch(val searchName: String, val enableNotifications: Boolean) : SearchAction
    data class OnLoadSavedSearch(val searchId: Long) : SearchAction
    data class OnDeleteSavedSearch(val searchId: Long) : SearchAction
    data object OnViewSavedSearches : SearchAction
    
    // Navigation Actions
    data object OnCreateListingClick : SearchAction
    data object OnSavedPropertiesClick : SearchAction
    data object OnTopAgentsClick : SearchAction
    data object OnAgenciesClick : SearchAction
    data object OnSubscriptionClick : SearchAction
    data object OnInboxClick : SearchAction
    data object OnProfileClick : SearchAction
    data object OnMenuClick : SearchAction
    data object OnCloseMenu : SearchAction
    
    // Newsletter Subscription
    data class OnSubscribeNewsletter(val email: String) : SearchAction
}
