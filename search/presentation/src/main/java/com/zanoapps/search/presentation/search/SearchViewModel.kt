package com.zanoapps.search.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.enums.SortOption
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.Result
import com.zanoapps.presentation.ui.UiText
import com.zanoapps.search.domain.model.MapLocation
import com.zanoapps.search.domain.model.MockData
import com.zanoapps.search.domain.model.SavedSearch
import com.zanoapps.search.domain.model.SearchFilters
import com.zanoapps.search.domain.repository.SavedSearchRepository
import com.zanoapps.search.domain.repository.SearchRepository
import com.zanoapps.search.presentation.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val savedSearchRepository: SavedSearchRepository,
    private val userId: String?
) : ViewModel() {
    
    var state by mutableStateOf(SearchState(
        userId = userId,
        isLoggedIn = userId != null
    ))
        private set
    
    private val eventChannel = Channel<SearchEvent>()
    val events = eventChannel.receiveAsFlow()
    
    init {
        loadInitialProperties()
        observeSearchQuery()
        observeFavorites()
        observeSavedSearches()
    }
    
    private fun loadInitialProperties() {
        viewModelScope.launch {
            state = state.copy(isLoadingProperties = true)
            
            when (val result = searchRepository.searchProperties(state.filters)) {
                is Result.Success -> {
                    val properties = result.data
                    state = state.copy(
                        properties = properties,
                        filteredProperties = sortProperties(properties, state.sortOption),
                        resultsCount = properties.size,
                        isLoadingProperties = false
                    )
                }
                is Result.Error -> {
                    // Fallback to mock data
                    val mockProperties = MockData.getMockProperties()
                    state = state.copy(
                        properties = mockProperties,
                        filteredProperties = sortProperties(mockProperties, state.sortOption),
                        resultsCount = mockProperties.size,
                        isLoadingProperties = false
                    )
                }
            }
        }
    }
    
    private fun observeSearchQuery() {
        snapshotFlow { state.searchQuery.text }
            .onEach { query ->
                filterProperties(query.toString())
            }
            .launchIn(viewModelScope)
    }
    
    private fun observeFavorites() {
        userId?.let { id ->
            searchRepository.getFavoritePropertyIds(id)
                .onEach { favoriteIds ->
                    state = state.copy(favoritePropertyIds = favoriteIds)
                }
                .launchIn(viewModelScope)
        }
    }
    
    private fun observeSavedSearches() {
        userId?.let { id ->
            savedSearchRepository.getSavedSearches(id)
                .onEach { savedSearches ->
                    state = state.copy(
                        savedSearches = savedSearches,
                        savedSearchCount = savedSearches.size
                    )
                }
                .launchIn(viewModelScope)
        }
    }
    
    fun onAction(action: SearchAction) {
        when (action) {
            // Map Actions
            is SearchAction.OnMapMoved -> handleMapMoved(action.location)
            is SearchAction.OnMarkerClicked -> handleMarkerClicked(action.property)
            SearchAction.OnMyLocationClick -> handleMyLocationClick()
            SearchAction.OnMapTypeToggle -> handleMapTypeToggle()
            SearchAction.OnDrawModeToggle -> handleDrawModeToggle()
            
            // Search Actions
            is SearchAction.OnSearchQueryChanged -> handleSearchQueryChanged(action.query)
            SearchAction.OnSearchSubmit -> handleSearchSubmit()
            
            // Filter Actions
            is SearchAction.OnFiltersApplied -> handleFiltersApplied(action.filters)
            SearchAction.OnClearFilters -> handleClearFilters()
            SearchAction.OnFilterClick -> handleFilterClick()
            
            // Sort Actions
            is SearchAction.OnSortChanged -> handleSortChanged(action.sortOption)
            
            // Property Actions
            is SearchAction.OnPropertyClicked -> handlePropertyClicked(action.property)
            is SearchAction.OnFavoriteToggle -> handleFavoriteToggle(action.propertyId)
            SearchAction.OnRefreshProperties -> handleRefreshProperties()
            
            // View Mode Actions
            SearchAction.OnToggleViewMode -> handleToggleViewMode()
            
            // Bottom Sheet Actions
            SearchAction.OnExpandBottomSheet -> handleExpandBottomSheet()
            SearchAction.OnCollapseBottomSheet -> handleCollapseBottomSheet()
            
            // Saved Search Actions
            SearchAction.OnSaveSearchClick -> handleSaveSearchClick()
            is SearchAction.OnSaveSearch -> handleSaveSearch(action.searchName, action.enableNotifications)
            is SearchAction.OnLoadSavedSearch -> handleLoadSavedSearch(action.searchId)
            is SearchAction.OnDeleteSavedSearch -> handleDeleteSavedSearch(action.searchId)
            SearchAction.OnViewSavedSearches -> handleViewSavedSearches()
            
            // Navigation Actions
            SearchAction.OnCreateListingClick -> handleCreateListingClick()
            SearchAction.OnSavedPropertiesClick -> handleSavedPropertiesClick()
            SearchAction.OnTopAgentsClick -> handleTopAgentsClick()
            SearchAction.OnAgenciesClick -> handleAgenciesClick()
            SearchAction.OnSubscriptionClick -> handleSubscriptionClick()
            SearchAction.OnInboxClick -> handleInboxClick()
            SearchAction.OnProfileClick -> handleProfileClick()
            SearchAction.OnMenuClick -> handleMenuClick()
            SearchAction.OnCloseMenu -> handleCloseMenu()
            
            // Newsletter Subscription
            is SearchAction.OnSubscribeNewsletter -> handleSubscribeNewsletter(action.email)
        }
    }
    
    // Map Handlers
    private fun handleMapMoved(location: MapLocation) {
        state = state.copy(mapLocation = location)
    }
    
    private fun handleMarkerClicked(property: BalkanEstateProperty) {
        state = state.copy(selectedProperty = property)
    }
    
    private fun handleMyLocationClick() {
        // TODO: Implement location services
    }
    
    private fun handleMapTypeToggle() {
        state = state.copy(isMapTypeRoad = !state.isMapTypeRoad)
    }
    
    private fun handleDrawModeToggle() {
        state = state.copy(isDrawModeEnabled = !state.isDrawModeEnabled)
    }
    
    // Search Handlers
    private fun handleSearchQueryChanged(query: String) {
        // Query is automatically observed via snapshotFlow
    }
    
    private fun handleSearchSubmit() {
        viewModelScope.launch {
            state = state.copy(isLoadingProperties = true)
            
            val filters = state.filters.copy(query = state.searchQuery.text.toString())
            when (val result = searchRepository.searchProperties(filters)) {
                is Result.Success -> {
                    state = state.copy(
                        filteredProperties = sortProperties(result.data, state.sortOption),
                        resultsCount = result.data.size,
                        isLoadingProperties = false
                    )
                }
                is Result.Error -> {
                    state = state.copy(isLoadingProperties = false)
                    eventChannel.send(SearchEvent.Error(UiText.StringResource(R.string.search_error)))
                }
            }
        }
    }
    
    // Filter Handlers
    private fun handleFiltersApplied(filters: SearchFilters) {
        state = state.copy(
            filters = filters,
            hasActiveFilters = filters.hasActiveFilters()
        )
        handleSearchSubmit()
    }
    
    private fun handleClearFilters() {
        state = state.copy(
            filters = SearchFilters(),
            hasActiveFilters = false
        )
        handleSearchSubmit()
    }
    
    private fun handleFilterClick() {
        viewModelScope.launch {
            eventChannel.send(SearchEvent.NavigateToFilters)
        }
    }
    
    // Sort Handlers
    private fun handleSortChanged(sortOption: SortOption) {
        state = state.copy(
            sortOption = sortOption,
            filteredProperties = sortProperties(state.filteredProperties, sortOption)
        )
    }
    
    // Property Handlers
    private fun handlePropertyClicked(property: BalkanEstateProperty) {
        viewModelScope.launch {
            eventChannel.send(SearchEvent.NavigateToPropertyDetails(property.id))
        }
    }
    
    private fun handleFavoriteToggle(propertyId: String) {
        viewModelScope.launch {
            if (!state.isLoggedIn) {
                eventChannel.send(SearchEvent.LoginRequired)
                return@launch
            }
            
            userId?.let { id ->
                searchRepository.toggleFavorite(id, propertyId)
            }
        }
    }
    
    private fun handleRefreshProperties() {
        viewModelScope.launch {
            state = state.copy(isRefreshing = true)
            
            when (val result = searchRepository.searchProperties(state.filters)) {
                is Result.Success -> {
                    state = state.copy(
                        properties = result.data,
                        filteredProperties = sortProperties(result.data, state.sortOption),
                        resultsCount = result.data.size,
                        isRefreshing = false
                    )
                }
                is Result.Error -> {
                    state = state.copy(isRefreshing = false)
                    eventChannel.send(SearchEvent.Error(UiText.StringResource(R.string.refresh_error)))
                }
            }
        }
    }
    
    // View Mode Handlers
    private fun handleToggleViewMode() {
        state = state.copy(
            viewMode = if (state.viewMode == ViewMode.MAP) ViewMode.LIST else ViewMode.MAP
        )
    }
    
    // Bottom Sheet Handlers
    private fun handleExpandBottomSheet() {
        state = state.copy(isBottomSheetExpanded = true)
    }
    
    private fun handleCollapseBottomSheet() {
        state = state.copy(isBottomSheetExpanded = false)
    }
    
    // Saved Search Handlers
    private fun handleSaveSearchClick() {
        if (!state.isLoggedIn) {
            viewModelScope.launch {
                eventChannel.send(SearchEvent.LoginRequired)
            }
            return
        }
        state = state.copy(showSaveSearchDialog = true)
    }
    
    private fun handleSaveSearch(name: String, enableNotifications: Boolean) {
        viewModelScope.launch {
            state = state.copy(isSavingSearch = true, showSaveSearchDialog = false)
            
            userId?.let { id ->
                val savedSearch = SavedSearch(
                    name = name,
                    filters = state.filters,
                    notificationsEnabled = enableNotifications
                )
                
                when (savedSearchRepository.saveSearch(id, savedSearch)) {
                    is Result.Success -> {
                        state = state.copy(isSavingSearch = false)
                        eventChannel.send(SearchEvent.SearchSaved)
                    }
                    is Result.Error -> {
                        state = state.copy(isSavingSearch = false)
                        eventChannel.send(SearchEvent.Error(UiText.StringResource(R.string.save_search_error)))
                    }
                }
            }
        }
    }
    
    private fun handleLoadSavedSearch(searchId: Long) {
        viewModelScope.launch {
            userId?.let { id ->
                when (val result = savedSearchRepository.getSavedSearchById(id, searchId)) {
                    is Result.Success -> {
                        result.data?.let { savedSearch ->
                            state = state.copy(
                                filters = savedSearch.filters,
                                hasActiveFilters = savedSearch.filters.hasActiveFilters()
                            )
                            handleSearchSubmit()
                        }
                    }
                    is Result.Error -> {
                        eventChannel.send(SearchEvent.Error(UiText.StringResource(R.string.load_search_error)))
                    }
                }
            }
        }
    }
    
    private fun handleDeleteSavedSearch(searchId: Long) {
        viewModelScope.launch {
            userId?.let { id ->
                when (savedSearchRepository.deleteSavedSearch(id, searchId)) {
                    is Result.Success -> {
                        eventChannel.send(SearchEvent.SearchDeleted)
                    }
                    is Result.Error -> {
                        eventChannel.send(SearchEvent.Error(UiText.StringResource(R.string.delete_search_error)))
                    }
                }
            }
        }
    }
    
    private fun handleViewSavedSearches() {
        viewModelScope.launch {
            eventChannel.send(SearchEvent.NavigateToSavedSearches)
        }
    }
    
    // Navigation Handlers
    private fun handleCreateListingClick() {
        viewModelScope.launch {
            if (!state.isLoggedIn) {
                eventChannel.send(SearchEvent.LoginRequired)
                return@launch
            }
            eventChannel.send(SearchEvent.NavigateToCreateListing)
        }
    }
    
    private fun handleSavedPropertiesClick() {
        viewModelScope.launch {
            if (!state.isLoggedIn) {
                eventChannel.send(SearchEvent.LoginRequired)
                return@launch
            }
            eventChannel.send(SearchEvent.NavigateToSavedProperties)
        }
    }
    
    private fun handleTopAgentsClick() {
        viewModelScope.launch {
            eventChannel.send(SearchEvent.NavigateToTopAgents)
        }
    }
    
    private fun handleAgenciesClick() {
        viewModelScope.launch {
            eventChannel.send(SearchEvent.NavigateToAgencies)
        }
    }
    
    private fun handleSubscriptionClick() {
        viewModelScope.launch {
            if (!state.isLoggedIn) {
                eventChannel.send(SearchEvent.LoginRequired)
                return@launch
            }
            eventChannel.send(SearchEvent.NavigateToSubscription)
        }
    }
    
    private fun handleInboxClick() {
        viewModelScope.launch {
            if (!state.isLoggedIn) {
                eventChannel.send(SearchEvent.LoginRequired)
                return@launch
            }
            eventChannel.send(SearchEvent.NavigateToInbox)
        }
    }
    
    private fun handleProfileClick() {
        viewModelScope.launch {
            eventChannel.send(SearchEvent.NavigateToProfile)
        }
    }
    
    private fun handleMenuClick() {
        state = state.copy(isMenuOpen = true)
    }
    
    private fun handleCloseMenu() {
        state = state.copy(isMenuOpen = false)
    }
    
    // Newsletter Handler
    private fun handleSubscribeNewsletter(email: String) {
        viewModelScope.launch {
            state = state.copy(isSubscribing = true)
            
            when (searchRepository.subscribeToNewProperties(email)) {
                is Result.Success -> {
                    state = state.copy(isSubscribing = false, newsletterEmail = "")
                    eventChannel.send(SearchEvent.SubscribedToNewsletter)
                }
                is Result.Error -> {
                    state = state.copy(isSubscribing = false)
                    eventChannel.send(SearchEvent.Error(UiText.StringResource(R.string.subscribe_error)))
                }
            }
        }
    }
    
    // Helper Functions
    private fun filterProperties(query: String) {
        val filtered = if (query.isBlank()) {
            state.properties
        } else {
            state.properties.filter { property ->
                property.title.contains(query, ignoreCase = true) ||
                        property.city.contains(query, ignoreCase = true) ||
                        property.address.contains(query, ignoreCase = true) ||
                        property.propertyType.contains(query, ignoreCase = true)
            }
        }
        
        state = state.copy(
            filteredProperties = sortProperties(filtered, state.sortOption),
            resultsCount = filtered.size
        )
    }
    
    private fun sortProperties(
        properties: List<BalkanEstateProperty>,
        sortOption: SortOption
    ): List<BalkanEstateProperty> {
        return when (sortOption) {
            SortOption.PRICE_LOW_TO_HIGH -> properties.sortedBy { it.price }
            SortOption.PRICE_HIGH_TO_LOW -> properties.sortedByDescending { it.price }
            SortOption.NEWEST -> properties.sortedByDescending { it.isFeatured }
            SortOption.OLDEST -> properties.sortedBy { it.isFeatured }
            SortOption.BEDROOMS -> properties.sortedByDescending { it.bedrooms }
            SortOption.SQUARE_FOOTAGE -> properties.sortedByDescending { it.squareFootage }
            SortOption.FEATURED -> properties.sortedByDescending { it.isFeatured }
        }
    }
}
