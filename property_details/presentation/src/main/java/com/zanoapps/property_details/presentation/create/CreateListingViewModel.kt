package com.zanoapps.property_details.presentation.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CreateListingViewModel : ViewModel() {

    var state by mutableStateOf(CreateListingState())
        private set

    private val eventChannel = Channel<CreateListingEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: CreateListingAction) {
        when (action) {
            CreateListingAction.OnNextStep -> {
                if (state.currentStep < state.totalSteps - 1) {
                    state = state.copy(currentStep = state.currentStep + 1)
                }
            }
            CreateListingAction.OnPreviousStep -> {
                if (state.currentStep > 0) {
                    state = state.copy(currentStep = state.currentStep - 1)
                }
            }
            is CreateListingAction.OnTitleChanged -> state = state.copy(title = action.title)
            is CreateListingAction.OnDescriptionChanged -> state = state.copy(description = action.description)
            is CreateListingAction.OnPropertyTypeChanged -> state = state.copy(propertyType = action.type)
            is CreateListingAction.OnListingTypeChanged -> state = state.copy(listingType = action.type)
            is CreateListingAction.OnAddressChanged -> state = state.copy(address = action.address)
            is CreateListingAction.OnCityChanged -> state = state.copy(city = action.city)
            is CreateListingAction.OnCountryChanged -> state = state.copy(country = action.country)
            is CreateListingAction.OnPostalCodeChanged -> state = state.copy(postalCode = action.code)
            is CreateListingAction.OnPriceChanged -> state = state.copy(price = action.price)
            is CreateListingAction.OnCurrencyChanged -> state = state.copy(currency = action.currency)
            is CreateListingAction.OnBedroomsChanged -> state = state.copy(bedrooms = action.bedrooms)
            is CreateListingAction.OnBathroomsChanged -> state = state.copy(bathrooms = action.bathrooms)
            is CreateListingAction.OnSquareFootageChanged -> state = state.copy(squareFootage = action.sqft)
            is CreateListingAction.OnYearBuiltChanged -> state = state.copy(yearBuilt = action.year)
            is CreateListingAction.OnParkingChanged -> state = state.copy(parkingSpaces = action.parking)
            is CreateListingAction.OnFloorChanged -> state = state.copy(floorNumber = action.floor)
            is CreateListingAction.OnAmenityToggle -> {
                val current = state.selectedAmenities.toMutableSet()
                if (current.contains(action.amenity)) current.remove(action.amenity)
                else current.add(action.amenity)
                state = state.copy(selectedAmenities = current)
            }
            is CreateListingAction.OnFurnishedTypeChanged -> state = state.copy(furnishedType = action.type)
            is CreateListingAction.OnHeatingTypeChanged -> state = state.copy(heatingType = action.type)
            is CreateListingAction.OnPhotoAdded -> {
                state = state.copy(photoUris = state.photoUris + action.uri)
            }
            is CreateListingAction.OnPhotoRemoved -> {
                state = state.copy(photoUris = state.photoUris.toMutableList().also { it.removeAt(action.index) })
            }
            CreateListingAction.OnSubmitListing -> submitListing()
            CreateListingAction.OnBackClick -> {
                viewModelScope.launch { eventChannel.send(CreateListingEvent.NavigateBack) }
            }
        }
    }

    private fun submitListing() {
        viewModelScope.launch {
            state = state.copy(isSubmitting = true)
            delay(2000) // Simulate API call
            state = state.copy(isSubmitting = false)
            eventChannel.send(CreateListingEvent.ListingCreated)
        }
    }
}
