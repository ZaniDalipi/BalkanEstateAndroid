package com.zanoapps.property_details.presentation.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.property_details.domain.model.CreatePropertyForm
import com.zanoapps.property_details.domain.repository.PropertyDetailRepository
import com.zanoapps.core.domain.util.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CreateListingViewModel(
    private val propertyDetailRepository: PropertyDetailRepository
) : ViewModel() {

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
        validateForm()
    }

    private fun validateForm() {
        val isValid = state.title.isNotBlank() &&
                state.propertyType.isNotBlank() &&
                state.address.isNotBlank() &&
                state.city.isNotBlank() &&
                state.price.isNotBlank() &&
                state.price.toDoubleOrNull() != null &&
                state.price.toDoubleOrNull()?.let { it > 0 } == true
        state = state.copy(isFormValid = isValid)
    }

    private fun submitListing() {
        if (!state.isFormValid) {
            viewModelScope.launch {
                eventChannel.send(CreateListingEvent.ValidationError("Please fill in all required fields"))
            }
            return
        }
        viewModelScope.launch {
            state = state.copy(isSubmitting = true)
            val form = CreatePropertyForm(
                title = state.title,
                description = state.description,
                price = state.price,
                currency = state.currency,
                address = state.address,
                city = state.city,
                country = state.country,
                postalCode = state.postalCode,
                bedrooms = state.bedrooms.toIntOrNull() ?: 1,
                bathrooms = state.bathrooms.toIntOrNull() ?: 1,
                squareFootage = state.squareFootage,
                yearBuilt = state.yearBuilt
            )
            when (val result = propertyDetailRepository.createListing(form)) {
                is Result.Success -> {
                    state = state.copy(isSubmitting = false)
                    eventChannel.send(CreateListingEvent.ListingCreated)
                }
                is Result.Error -> {
                    state = state.copy(
                        isSubmitting = false,
                        errorMessage = "Failed to create listing. Please try again."
                    )
                    eventChannel.send(CreateListingEvent.ValidationError("Failed to create listing"))
                }
            }
        }
    }
}
