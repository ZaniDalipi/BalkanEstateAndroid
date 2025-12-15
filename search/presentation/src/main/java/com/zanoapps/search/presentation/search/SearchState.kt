package com.zanoapps.search.presentation.search

import androidx.compose.foundation.text.input.TextFieldState
import com.zanoapps.core.domain.enums.SortOption
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.search.domain.model.MapLocation
import com.zanoapps.search.domain.model.SavedSearch
import com.zanoapps.search.domain.model.SearchFilters

enum class ViewMode {
    MAP, LIST
}

data class SearchState(
    // Map State
    val mapLocation: MapLocation = MapLocation(
        latitude = 41.9981,
        longitude = 21.4254,
        zoom = 7f
    ),
    val isMapTypeRoad: Boolean = true,
    val isDrawModeEnabled: Boolean = false,
    
    // Properties State
    val properties: List<BalkanEstateProperty> = emptyList(),
    val filteredProperties: List<BalkanEstateProperty> = emptyList(),
    val selectedProperty: BalkanEstateProperty? = null,
    val favoritePropertyIds: Set<String> = emptySet(),
    
    // Search State
    val searchQuery: TextFieldState = TextFieldState(),
    val filters: SearchFilters = SearchFilters(),
    val sortOption: SortOption = SortOption.NEWEST,
    val hasActiveFilters: Boolean = false,
    
    // View Mode
    val viewMode: ViewMode = ViewMode.MAP,
    
    // Loading States
    val isLoadingProperties: Boolean = false,
    val isRefreshing: Boolean = false,
    val isSavingSearch: Boolean = false,
    val isSubscribing: Boolean = false,
    
    // Bottom Sheet State
    val isBottomSheetExpanded: Boolean = false,
    
    // Saved Searches
    val savedSearches: List<SavedSearch> = emptyList(),
    val savedSearchCount: Int = 0,
    
    // Menu State
    val isMenuOpen: Boolean = false,
    
    // Save Search Dialog
    val showSaveSearchDialog: Boolean = false,
    
    // Newsletter
    val newsletterEmail: String = "",
    
    // Results Count
    val resultsCount: Int = 0,
    
    // Error State
    val errorMessage: String? = null,
    
    // User State
    val isLoggedIn: Boolean = false,
    val userId: String? = null
)
