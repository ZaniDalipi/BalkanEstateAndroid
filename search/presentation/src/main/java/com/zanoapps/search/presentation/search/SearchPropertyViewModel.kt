package com.zanoapps.search.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.favourites.domain.repository.FavouritesRepository
import com.zanoapps.search.domain.repository.PropertyRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SearchPropertyViewModel(
    private val propertyRepository: PropertyRepository,
    private val favouritesRepository: FavouritesRepository
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
                viewModelScope.launch {
                    val currentFavorites = state.favoritePropertyIds.toMutableSet()
                    if (currentFavorites.contains(action.propertyId)) {
                        currentFavorites.remove(action.propertyId)
                        favouritesRepository.removeFavourite(action.propertyId)
                    } else {
                        currentFavorites.add(action.propertyId)
                        favouritesRepository.addFavourite(action.propertyId)
                    }
                    state = state.copy(favoritePropertyIds = currentFavorites)
                }
            }
            SearchAction.OnFilterClick -> {
                viewModelScope.launch {
                    eventChannel.send(SearchEvent.NavigateToFilters)
                }
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
                viewModelScope.launch {
                    eventChannel.send(SearchEvent.NavigateToPropertyDetails)
                }
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
                viewModelScope.launch {
                    eventChannel.send(SearchEvent.NavigateToSavedSearches)
                }
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
                    eventChannel.send(SearchEvent.NavigateToPropertyDetails)
                }
            }
        }
    }

    private fun loadProperties(isRefresh: Boolean = false) {
        viewModelScope.launch {
            state = state.copy(
                isLoadingProperties = !isRefresh,
                isRefreshing = isRefresh
            )
        }
        propertyRepository.getProperties()
            .onEach { properties ->
                state = state.copy(
                    properties = properties,
                    filteredProperties = properties,
                    isLoadingProperties = false,
                    isRefreshing = false
                )
            }
            .launchIn(viewModelScope)
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
