package com.zanoapps.search.presentation.filters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FiltersViewModel : ViewModel() {

    var state by mutableStateOf(FiltersState())
        private set

    private val eventChannel = Channel<FiltersEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: FiltersAction) {
        when (action) {
            // Listing Type
            is FiltersAction.OnListingTypeSelected -> {
                state = state.copy(selectedListingType = action.type)
                updateActiveFiltersCount()
            }

            // Property Type
            is FiltersAction.OnPropertyTypeToggled -> {
                val currentTypes = state.selectedPropertyTypes.toMutableSet()
                if (currentTypes.contains(action.type)) {
                    currentTypes.remove(action.type)
                } else {
                    currentTypes.add(action.type)
                }
                state = state.copy(selectedPropertyTypes = currentTypes)
                updateActiveFiltersCount()
            }

            // Price
            is FiltersAction.OnMinPriceChanged -> {
                state = state.copy(minPrice = action.price)
                updateActiveFiltersCount()
            }
            is FiltersAction.OnMaxPriceChanged -> {
                state = state.copy(maxPrice = action.price)
                updateActiveFiltersCount()
            }

            // Bedrooms & Bathrooms
            is FiltersAction.OnMinBedroomsChanged -> {
                state = state.copy(minBedrooms = action.count)
                updateActiveFiltersCount()
            }
            is FiltersAction.OnMinBathroomsChanged -> {
                state = state.copy(minBathrooms = action.count)
                updateActiveFiltersCount()
            }

            // Size
            is FiltersAction.OnMinSizeChanged -> {
                state = state.copy(minSize = action.size)
                updateActiveFiltersCount()
            }
            is FiltersAction.OnMaxSizeChanged -> {
                state = state.copy(maxSize = action.size)
                updateActiveFiltersCount()
            }

            // Furnished
            is FiltersAction.OnFurnishedTypeChanged -> {
                state = state.copy(furnishedType = action.type)
                updateActiveFiltersCount()
            }

            // Amenities
            is FiltersAction.OnAmenityToggled -> {
                val currentAmenities = state.selectedAmenities.toMutableSet()
                if (currentAmenities.contains(action.amenity)) {
                    currentAmenities.remove(action.amenity)
                } else {
                    currentAmenities.add(action.amenity)
                }
                state = state.copy(selectedAmenities = currentAmenities)
                updateActiveFiltersCount()
            }

            // Other
            FiltersAction.OnToggleOnlyWithPhotos -> {
                state = state.copy(onlyWithPhotos = !state.onlyWithPhotos)
                updateActiveFiltersCount()
            }
            FiltersAction.OnToggleOnlyFeatured -> {
                state = state.copy(onlyFeatured = !state.onlyFeatured)
                updateActiveFiltersCount()
            }
            FiltersAction.OnToggleOnlyNew -> {
                state = state.copy(onlyNew = !state.onlyNew)
                updateActiveFiltersCount()
            }

            // Actions
            FiltersAction.OnApplyFilters -> {
                viewModelScope.launch {
                    eventChannel.send(FiltersEvent.FiltersApplied(state))
                }
            }
            FiltersAction.OnResetFilters -> {
                state = FiltersState()
                viewModelScope.launch {
                    eventChannel.send(FiltersEvent.FiltersReset)
                }
            }
            FiltersAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(FiltersEvent.NavigateBack)
                }
            }
        }
    }

    private fun updateActiveFiltersCount() {
        var count = 0
        if (state.selectedListingType != null) count++
        if (state.selectedPropertyTypes.isNotEmpty()) count++
        if (state.minPrice.isNotBlank() || state.maxPrice.isNotBlank()) count++
        if (state.minBedrooms > 0) count++
        if (state.minBathrooms > 0) count++
        if (state.minSize.isNotBlank() || state.maxSize.isNotBlank()) count++
        if (state.furnishedType != null) count++
        if (state.selectedAmenities.isNotEmpty()) count++
        if (state.onlyWithPhotos) count++
        if (state.onlyFeatured) count++
        if (state.onlyNew) count++
        state = state.copy(activeFiltersCount = count)
    }
}
