package com.zanoapps.search.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.search.domain.model.MockData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SearchPropertyViewModel(
    private val userId: String? = null
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    private val eventChannel = Channel<SearchEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadProperties()
    }

    fun onAction(action: SearchAction) {
        when (action) {
            SearchAction.OnClearFilters -> {
                state = state.copy(
                    hasActiveFilter = false,
                    filteredProperties = state.properties
                )
            }
            SearchAction.OnCollapseBottomSheet -> {
                state = state.copy(isBottomSheetExpanded = false)
            }
            SearchAction.OnCreateListingClick -> {
                // Navigate to create listing screen
            }
            is SearchAction.OnDeleteSavedSearch -> {
                // Delete saved search
            }
            SearchAction.OnExpandBottomSheet -> {
                state = state.copy(isBottomSheetExpanded = true)
            }
            is SearchAction.OnFavoriteToggle -> {
                val currentFavorites = state.favoritePropertyIds.toMutableSet()
                if (currentFavorites.contains(action.propertyId)) {
                    currentFavorites.remove(action.propertyId)
                } else {
                    currentFavorites.add(action.propertyId)
                }
                state = state.copy(favoritePropertyIds = currentFavorites)
            }
            SearchAction.OnFilterClick -> {
                // Open filter dialog
            }
            is SearchAction.OnFiltersApplied -> {
                state = state.copy(
                    filters = action.filters,
                    hasActiveFilter = true
                )
                applyFilters()
            }
            is SearchAction.OnLoadSavedSearch -> {
                // Load saved search
            }
            is SearchAction.OnMapMoved -> {
                state = state.copy(mapLocation = action.location)
            }
            SearchAction.OnMapTypeToggle -> {
                state = state.copy(isMapTypeRoad = !state.isMapTypeRoad)
            }
            is SearchAction.OnMarkerClicked -> {
                state = state.copy(selectedBalkanEstateProperty = action.balkanEstateProperty)
            }
            SearchAction.OnMyLocationClick -> {
                // Get current location
            }
            is SearchAction.OnPropertyClicked -> {
                state = state.copy(selectedBalkanEstateProperty = action.balkanEstateProperty)
            }
            SearchAction.OnRefreshProperties -> {
                loadProperties(isRefresh = true)
            }
            is SearchAction.OnSaveSearch -> {
                state = state.copy(isSavingSearch = true)
                // Save search logic
            }
            SearchAction.OnSaveSearchClick -> {
                // Open save search dialog
            }
            is SearchAction.OnSearchQueryChanged -> {
                // Query change is handled by TextFieldState
            }
            SearchAction.OnSearchSubmit -> {
                applyFilters()
            }
            is SearchAction.OnSortChanged -> {
                state = state.copy(sortOption = action.sortOption)
                applySorting()
            }
            SearchAction.OnViewSavedSearches -> {
                // Navigate to saved searches
            }
            // New drawer actions
            SearchAction.OnOpenDrawer -> {
                state = state.copy(isDrawerOpen = true)
            }
            SearchAction.OnCloseDrawer -> {
                state = state.copy(isDrawerOpen = false)
            }
            is SearchAction.OnDrawerItemClick -> {
                // Handle drawer item navigation
                state = state.copy(isDrawerOpen = false)
            }
            // View mode toggle
            is SearchAction.OnViewModeToggle -> {
                state = state.copy(isListView = action.isListView)
            }
            // Subscription
            is SearchAction.OnSubscribe -> {
                state = state.copy(subscriptionEmail = action.email)
                // Send subscription request
            }
            // View details
            is SearchAction.OnViewDetailsClick -> {
                state = state.copy(selectedBalkanEstateProperty = action.property)
                // Navigate to property details
            }
        }
    }

    private fun loadProperties(isRefresh: Boolean = false) {
        viewModelScope.launch {
            state = state.copy(
                isLoadingProperties = !isRefresh,
                isRefreshing = isRefresh
            )

            // Load mock data for now
            val properties = MockData.getMockProperties()
            state = state.copy(
                properties = properties,
                filteredProperties = properties,
                isLoadingProperties = false,
                isRefreshing = false
            )
        }
    }

    private fun applyFilters() {
        val query = state.searchQuery.text.toString().lowercase()
        val filtered = state.properties.filter { property ->
            if (query.isEmpty()) true
            else {
                property.title.lowercase().contains(query) ||
                        property.address.lowercase().contains(query) ||
                        property.city.lowercase().contains(query)
            }
        }
        state = state.copy(filteredProperties = filtered)
    }

    private fun applySorting() {
        val sorted = when (state.sortOption) {
            com.zanoapps.core.domain.enums.SortOption.PRICE_LOW_TO_HIGH ->
                state.filteredProperties.sortedBy { it.price }
            com.zanoapps.core.domain.enums.SortOption.PRICE_HIGH_TO_LOW ->
                state.filteredProperties.sortedByDescending { it.price }
            com.zanoapps.core.domain.enums.SortOption.NEWEST ->
                state.filteredProperties // Would sort by date if available
            com.zanoapps.core.domain.enums.SortOption.OLDEST ->
                state.filteredProperties.reversed()
            com.zanoapps.core.domain.enums.SortOption.BEDROOMS ->
                state.filteredProperties.sortedByDescending { it.bedrooms }
            com.zanoapps.core.domain.enums.SortOption.SQUARE_FOOTAGE ->
                state.filteredProperties.sortedByDescending { it.squareFootage }
            com.zanoapps.core.domain.enums.SortOption.FEATURED ->
                state.filteredProperties.sortedByDescending { it.isFeatured }
        }
        state = state.copy(filteredProperties = sorted)
    }
}
