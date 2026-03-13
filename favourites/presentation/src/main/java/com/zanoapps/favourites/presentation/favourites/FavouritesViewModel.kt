package com.zanoapps.favourites.presentation.favourites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.favourites.domain.repository.FavouritesRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {

    var state by mutableStateOf(FavouritesState())
        private set

    private val eventChannel = Channel<FavouritesEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadFavourites()
    }

    fun onAction(action: FavouritesAction) {
        when (action) {
            FavouritesAction.OnLoadFavourites -> loadFavourites()
            is FavouritesAction.OnRemoveFavourite -> removeFavourite(action.propertyId)
            is FavouritesAction.OnPropertyClick -> {
                viewModelScope.launch {
                    eventChannel.send(FavouritesEvent.NavigateToPropertyDetail(action.property.id))
                }
            }
            is FavouritesAction.OnSortChanged -> {
                state = state.copy(selectedSortOption = action.sortOption)
                applySorting()
            }
            is FavouritesAction.OnSearchQueryChanged -> {
                state = state.copy(searchQuery = action.query)
                applyFilter()
            }
            FavouritesAction.OnToggleCompareMode -> {
                state = state.copy(
                    isCompareMode = !state.isCompareMode,
                    selectedForCompare = emptySet()
                )
            }
            is FavouritesAction.OnToggleCompareSelection -> {
                val current = state.selectedForCompare.toMutableSet()
                if (current.contains(action.propertyId)) {
                    current.remove(action.propertyId)
                } else if (current.size < 3) {
                    current.add(action.propertyId)
                }
                state = state.copy(selectedForCompare = current)
            }
            FavouritesAction.OnCompareSelected -> {
                viewModelScope.launch {
                    eventChannel.send(
                        FavouritesEvent.NavigateToCompare(state.selectedForCompare.toList())
                    )
                }
            }
            is FavouritesAction.OnViewDetails -> {
                viewModelScope.launch {
                    eventChannel.send(FavouritesEvent.NavigateToPropertyDetail(action.property.id))
                }
            }
        }
    }

    private fun loadFavourites() {
        state = state.copy(isLoading = true)
        favouritesRepository.getFavouriteProperties()
            .onEach { properties ->
                state = state.copy(
                    savedProperties = properties,
                    filteredProperties = properties,
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }

    private fun removeFavourite(propertyId: String) {
        viewModelScope.launch {
            favouritesRepository.removeFavourite(propertyId)
            eventChannel.send(FavouritesEvent.PropertyRemoved(propertyId))
        }
    }

    private fun applyFilter() {
        val query = state.searchQuery.lowercase()
        val filtered = if (query.isEmpty()) {
            state.savedProperties
        } else {
            state.savedProperties.filter {
                it.title.lowercase().contains(query) ||
                        it.address.lowercase().contains(query) ||
                        it.city.lowercase().contains(query)
            }
        }
        state = state.copy(filteredProperties = filtered)
    }

    private fun applySorting() {
        val sorted = when (state.selectedSortOption) {
            FavouritesSortOption.RECENTLY_SAVED -> state.filteredProperties
            FavouritesSortOption.PRICE_LOW_TO_HIGH -> state.filteredProperties.sortedBy { it.price }
            FavouritesSortOption.PRICE_HIGH_TO_LOW -> state.filteredProperties.sortedByDescending { it.price }
            FavouritesSortOption.MOST_BEDROOMS -> state.filteredProperties.sortedByDescending { it.bedrooms }
        }
        state = state.copy(filteredProperties = sorted)
    }
}
