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
            // Navigation
            CreateListingAction.OnNextStep -> {
                if (validateCurrentStep()) {
                    state = state.copy(currentStep = minOf(state.currentStep + 1, state.totalSteps - 1))
                }
            }
            CreateListingAction.OnPreviousStep -> {
                state = state.copy(currentStep = maxOf(state.currentStep - 1, 0))
            }
            CreateListingAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(CreateListingEvent.NavigateBack)
                }
            }

            // Basic Info
            is CreateListingAction.OnTitleChanged -> {
                state = state.copy(title = action.title, validationErrors = state.validationErrors - "title")
            }
            is CreateListingAction.OnDescriptionChanged -> {
                state = state.copy(description = action.description)
            }
            is CreateListingAction.OnListingTypeSelected -> {
                state = state.copy(listingType = action.type, validationErrors = state.validationErrors - "listingType")
            }
            is CreateListingAction.OnPropertyTypeSelected -> {
                state = state.copy(propertyType = action.type, validationErrors = state.validationErrors - "propertyType")
            }

            // Location
            is CreateListingAction.OnAddressChanged -> {
                state = state.copy(address = action.address, validationErrors = state.validationErrors - "address")
            }
            is CreateListingAction.OnCityChanged -> {
                state = state.copy(city = action.city, validationErrors = state.validationErrors - "city")
            }
            is CreateListingAction.OnCountryChanged -> {
                state = state.copy(country = action.country)
            }
            is CreateListingAction.OnPostalCodeChanged -> {
                state = state.copy(postalCode = action.postalCode)
            }

            // Details
            is CreateListingAction.OnPriceChanged -> {
                state = state.copy(price = action.price, validationErrors = state.validationErrors - "price")
            }
            is CreateListingAction.OnBedroomsChanged -> {
                state = state.copy(bedrooms = action.bedrooms)
            }
            is CreateListingAction.OnBathroomsChanged -> {
                state = state.copy(bathrooms = action.bathrooms)
            }
            is CreateListingAction.OnSquareFootageChanged -> {
                state = state.copy(squareFootage = action.sqft, validationErrors = state.validationErrors - "squareFootage")
            }
            is CreateListingAction.OnYearBuiltChanged -> {
                state = state.copy(yearBuilt = action.year)
            }
            is CreateListingAction.OnFurnishedChanged -> {
                state = state.copy(furnished = action.furnished)
            }

            // Amenities
            is CreateListingAction.OnAmenityToggled -> {
                val currentAmenities = state.selectedAmenities.toMutableSet()
                if (currentAmenities.contains(action.amenity)) {
                    currentAmenities.remove(action.amenity)
                } else {
                    currentAmenities.add(action.amenity)
                }
                state = state.copy(selectedAmenities = currentAmenities)
            }

            // Images
            is CreateListingAction.OnImagesSelected -> {
                state = state.copy(imageUris = state.imageUris + action.uris)
            }
            is CreateListingAction.OnImageRemoved -> {
                state = state.copy(imageUris = state.imageUris - action.uri)
            }

            // Contact
            is CreateListingAction.OnContactNameChanged -> {
                state = state.copy(contactName = action.name)
            }
            is CreateListingAction.OnContactPhoneChanged -> {
                state = state.copy(contactPhone = action.phone)
            }
            is CreateListingAction.OnContactEmailChanged -> {
                state = state.copy(contactEmail = action.email)
            }

            // Submit
            CreateListingAction.OnSubmit -> submitListing()
            CreateListingAction.OnSaveDraft -> saveDraft()
        }
    }

    private fun validateCurrentStep(): Boolean {
        val errors = mutableMapOf<String, String>()

        when (state.currentStep) {
            0 -> { // Basic Info
                if (state.title.isBlank()) errors["title"] = "Title is required"
                if (state.listingType == null) errors["listingType"] = "Select listing type"
                if (state.propertyType == null) errors["propertyType"] = "Select property type"
            }
            1 -> { // Location
                if (state.address.isBlank()) errors["address"] = "Address is required"
                if (state.city.isBlank()) errors["city"] = "City is required"
            }
            2 -> { // Details
                if (state.price.isBlank()) errors["price"] = "Price is required"
                if (state.squareFootage.isBlank()) errors["squareFootage"] = "Size is required"
            }
        }

        state = state.copy(validationErrors = errors)
        return errors.isEmpty()
    }

    private fun submitListing() {
        viewModelScope.launch {
            state = state.copy(isSubmitting = true)

            // Simulate API call
            delay(2000)

            state = state.copy(isSubmitting = false)
            eventChannel.send(CreateListingEvent.ListingCreated)
        }
    }

    private fun saveDraft() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            // Simulate saving draft
            delay(1000)

            state = state.copy(isLoading = false)
            eventChannel.send(CreateListingEvent.DraftSaved)
        }
    }
}
