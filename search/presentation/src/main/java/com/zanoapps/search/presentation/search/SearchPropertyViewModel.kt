package com.zanoapps.search.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.util.Result
import com.zanoapps.search.domain.repository.SearchRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SearchPropertyViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    private val eventChannel = Channel<SearchEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        observeProperties()
        refreshProperties()
    }

    fun onAction(action: SearchAction) {
        when (action) {
            SearchAction.OnClearFilters -> {
                state = state.copy(
                    hasActiveFilter = false,
                    filters = com.zanoapps.search.domain.model.SearchFilters()
                )
                observeProperties() // Re-observe without filters
            }
            SearchAction.OnCollapseBottomSheet -> {
                state = state.copy(isBottomSheetExpanded = false)
            }
            SearchAction.OnCreateListingClick -> {
                viewModelScope.launch {
                    eventChannel.send(SearchEvent.NavigateToCreateListing)
                }
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
                state = state.copy(isBottomSheetExpanded = true)
            }
            is SearchAction.OnFiltersApplied -> {
                state = state.copy(
                    filters = action.filters,
                    hasActiveFilter = true
                )
                applyFiltersFromRepository()
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
                refreshProperties()
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
                applyTextSearch()
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
                viewModelScope.launch {
                    eventChannel.send(SearchEvent.NavigateToPropertyDetails(action.property))
                }
            }
        }
    }

    /**
     * Observe properties from Room database (single source of truth).
     * When data changes in Room, this Flow will emit new values.
     */
    private fun observeProperties() {
        searchRepository.getProperties()
            .onEach { properties ->
                state = state.copy(
                    properties = properties,
                    filteredProperties = applyLocalFilters(properties),
                    isLoadingProperties = false,
                    isRefreshing = false
                )
            }
            .launchIn(viewModelScope)
    }

    /**
     * Refresh properties from remote API (MongoDB) and cache to Room.
     */
    private fun refreshProperties() {
        viewModelScope.launch {
            state = state.copy(
                isLoadingProperties = state.properties.isEmpty(),
                isRefreshing = state.properties.isNotEmpty()
            )

            val result = searchRepository.refreshProperties()
            when (result) {
                is Result.Success -> {
                    // Data will be automatically updated through the Flow observer
                    state = state.copy(
                        isLoadingProperties = false,
                        isRefreshing = false,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        isLoadingProperties = false,
                        isRefreshing = false,
                        errorMessage = "Failed to refresh properties. Showing cached data."
                    )
                }
            }
        }
    }

    /**
     * Apply filters using Room database queries.
     */
    private fun applyFiltersFromRepository() {
        val filters = state.filters

        // Build filter parameters
        val listingType = filters.listingTypes.firstOrNull()?.name
        val propertyType = filters.propertyTypes.firstOrNull()?.name

        searchRepository.searchProperties(
            listingType = listingType,
            propertyType = propertyType,
            minPrice = filters.minPrice,
            maxPrice = filters.maxPrice,
            minBedrooms = filters.bedrooms,
            city = if (filters.query.isNotEmpty()) filters.query else null
        )
            .onEach { properties ->
                state = state.copy(
                    properties = properties,
                    filteredProperties = applyLocalFilters(properties)
                )
            }
            .launchIn(viewModelScope)

        // Also refresh from API with the filters
        viewModelScope.launch {
            searchRepository.refreshProperties(
                listingType = listingType,
                propertyType = propertyType,
                minPrice = filters.minPrice,
                maxPrice = filters.maxPrice,
                minBedrooms = filters.bedrooms,
                city = if (filters.query.isNotEmpty()) filters.query else null
            )
        }
    }

    /**
     * Apply text search filter locally on cached data.
     */
    private fun applyTextSearch() {
        val query = state.searchQuery.text.toString().lowercase()
        val filtered = applyLocalFilters(state.properties, query)
        state = state.copy(filteredProperties = filtered)
    }

    /**
     * Apply local filters (text search) on properties.
     */
    private fun applyLocalFilters(
        properties: List<com.zanoapps.core.domain.model.BalkanEstateProperty>,
        query: String = state.searchQuery.text.toString().lowercase()
    ): List<com.zanoapps.core.domain.model.BalkanEstateProperty> {
        if (query.isEmpty()) return properties

        return properties.filter { property ->
            property.title.lowercase().contains(query) ||
                    property.address.lowercase().contains(query) ||
                    property.city.lowercase().contains(query)
        }
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
