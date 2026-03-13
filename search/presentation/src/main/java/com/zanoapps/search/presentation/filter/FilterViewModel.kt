package com.zanoapps.search.presentation.filter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FilterViewModel : ViewModel() {

    var state by mutableStateOf(FilterState())
        private set

    private val eventChannel = Channel<FilterEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: FilterAction) {
        when (action) {
            is FilterAction.OnMinPriceChanged -> state = state.copy(minPriceText = action.price)
            is FilterAction.OnMaxPriceChanged -> state = state.copy(maxPriceText = action.price)
            is FilterAction.OnMinSqftChanged -> state = state.copy(minSqftText = action.sqft)
            is FilterAction.OnMaxSqftChanged -> state = state.copy(maxSqftText = action.sqft)
            is FilterAction.OnBedroomsSelected -> state = state.copy(selectedBedrooms = action.bedrooms)
            is FilterAction.OnBathroomsSelected -> state = state.copy(selectedBathrooms = action.bathrooms)
            is FilterAction.OnPropertyTypeToggle -> {
                val current = state.selectedPropertyTypes.toMutableSet()
                if (current.contains(action.type)) current.remove(action.type) else current.add(action.type)
                state = state.copy(selectedPropertyTypes = current)
            }
            is FilterAction.OnListingTypeToggle -> {
                val current = state.selectedListingTypes.toMutableSet()
                if (current.contains(action.type)) current.remove(action.type) else current.add(action.type)
                state = state.copy(selectedListingTypes = current)
            }
            is FilterAction.OnAmenityToggle -> {
                val current = state.selectedAmenities.toMutableSet()
                if (current.contains(action.amenity)) current.remove(action.amenity) else current.add(action.amenity)
                state = state.copy(selectedAmenities = current)
            }
            is FilterAction.OnFurnishedSelected -> state = state.copy(selectedFurnished = action.type)
            is FilterAction.OnParkingSelected -> state = state.copy(selectedParking = action.type)
            is FilterAction.OnPetFriendlyToggle -> state = state.copy(petFriendly = action.value)
            FilterAction.OnApplyFilters -> {
                viewModelScope.launch { eventChannel.send(FilterEvent.FiltersApplied(state.filters)) }
            }
            FilterAction.OnClearFilters -> {
                state = FilterState()
            }
            FilterAction.OnBackClick -> {
                viewModelScope.launch { eventChannel.send(FilterEvent.NavigateBack) }
            }
        }
    }
}
