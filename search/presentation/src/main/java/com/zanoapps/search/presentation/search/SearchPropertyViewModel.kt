package com.zanoapps.search.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.enums.SortOption
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.search.domain.model.MockData
import com.zanoapps.search.domain.model.SearchFilters
import com.zanoapps.search.presentation.filter.FilterChipData
import com.zanoapps.search.presentation.filter.FilterSortViewModel
import com.zanoapps.search.presentation.filter.FilterState
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
                    filters = SearchFilters(),
                    hasActiveFilter = false,
                    filteredProperties = applySorting(state.properties, state.sortOption)
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
                // Open filter screen - handled by navigation callback
            }
            is SearchAction.OnFiltersApplied -> {
                state = state.copy(
                    filters = action.filters,
                    hasActiveFilter = hasActiveFilters(action.filters)
                )
                applyFiltersAndSort()
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
                applyFiltersAndSort()
            }
            is SearchAction.OnSortChanged -> {
                state = state.copy(sortOption = action.sortOption)
                applyFiltersAndSort()
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

    /**
     * Gets the current filters and sort option for passing to the filter screen
     */
    fun getCurrentFilters(): SearchFilters = state.filters

    /**
     * Gets the current sort option
     */
    fun getCurrentSortOption(): SortOption = state.sortOption

    /**
     * Gets the active filter chips for display in the active filters bar
     */
    fun getActiveFilterChips(): List<FilterChipData> {
        return FilterState.fromSearchFilters(state.filters, state.sortOption).activeFilterChips
    }

    /**
     * Gets the count of active filters
     */
    fun getActiveFilterCount(): Int {
        return FilterState.fromSearchFilters(state.filters, state.sortOption).activeFilterCount
    }

    /**
     * Removes a specific filter chip
     */
    fun removeFilterChip(chipData: FilterChipData) {
        val currentFilters = state.filters
        val updatedFilters = when (chipData) {
            is FilterChipData.ListingTypeChip -> currentFilters.copy(
                listingTypes = currentFilters.listingTypes - chipData.listingType
            )
            is FilterChipData.PropertyTypeChip -> currentFilters.copy(
                propertyTypes = currentFilters.propertyTypes - chipData.propertyType
            )
            is FilterChipData.PriceRangeChip -> currentFilters.copy(
                minPrice = null,
                maxPrice = null
            )
            is FilterChipData.BedroomsChip -> currentFilters.copy(bedrooms = null)
            is FilterChipData.BathroomsChip -> currentFilters.copy(bathrooms = null)
            is FilterChipData.SquareFootageChip -> currentFilters.copy(
                minSquareFootage = null,
                maxSquareFootage = null
            )
            is FilterChipData.AmenityChip -> currentFilters.copy(
                amenities = currentFilters.amenities - chipData.amenity
            )
            is FilterChipData.MoreAmenitiesChip -> currentFilters // Don't remove, just show filter screen
            is FilterChipData.FurnishedTypeChip -> currentFilters.copy(
                furnishedType = currentFilters.furnishedType - chipData.furnishedType
            )
            is FilterChipData.PetPolicyChip -> currentFilters.copy(
                petPolicy = currentFilters.petPolicy - chipData.petPolicy
            )
            is FilterChipData.ParkingTypeChip -> currentFilters.copy(
                parkingType = currentFilters.parkingType - chipData.parkingType
            )
        }

        state = state.copy(
            filters = updatedFilters,
            hasActiveFilter = hasActiveFilters(updatedFilters)
        )
        applyFiltersAndSort()
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
                filteredProperties = applySorting(properties, state.sortOption),
                isLoadingProperties = false,
                isRefreshing = false
            )
        }
    }

    private fun applyFiltersAndSort() {
        // First apply text search from the search bar
        val query = state.searchQuery.text.toString()
        val filtersWithQuery = state.filters.copy(query = query)

        // Apply filters
        val filtered = FilterSortViewModel.applyFiltersToProperties(
            properties = state.properties,
            filters = filtersWithQuery
        )

        // Then apply sorting
        val sorted = applySorting(filtered, state.sortOption)

        state = state.copy(filteredProperties = sorted)
    }

    private fun applySorting(properties: List<BalkanEstateProperty>, sortOption: SortOption): List<BalkanEstateProperty> {
        return FilterSortViewModel.applySortToProperties(properties, sortOption)
    }

    private fun hasActiveFilters(filters: SearchFilters): Boolean {
        return filters.query.isNotBlank() ||
                filters.minPrice != null ||
                filters.maxPrice != null ||
                filters.propertyTypes.isNotEmpty() ||
                filters.listingTypes.isNotEmpty() ||
                filters.bedrooms != null ||
                filters.bathrooms != null ||
                filters.minSquareFootage != null ||
                filters.maxSquareFootage != null ||
                filters.amenities.isNotEmpty() ||
                filters.furnishedType.isNotEmpty() ||
                filters.petPolicy.isNotEmpty() ||
                filters.parkingType.isNotEmpty()
    }
}
