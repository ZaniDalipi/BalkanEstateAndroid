package com.zanoapps.property_details.presentation.create

import com.zanoapps.core.domain.enums.Amenity
import com.zanoapps.core.domain.enums.FurnishedType
import com.zanoapps.core.domain.enums.ListingType
import com.zanoapps.core.domain.enums.PropertyType

sealed interface CreateListingAction {
    // Navigation
    data object OnNextStep : CreateListingAction
    data object OnPreviousStep : CreateListingAction
    data object OnBackClick : CreateListingAction

    // Basic Info
    data class OnTitleChanged(val title: String) : CreateListingAction
    data class OnDescriptionChanged(val description: String) : CreateListingAction
    data class OnListingTypeSelected(val type: ListingType) : CreateListingAction
    data class OnPropertyTypeSelected(val type: PropertyType) : CreateListingAction

    // Location
    data class OnAddressChanged(val address: String) : CreateListingAction
    data class OnCityChanged(val city: String) : CreateListingAction
    data class OnCountryChanged(val country: String) : CreateListingAction
    data class OnPostalCodeChanged(val postalCode: String) : CreateListingAction

    // Details
    data class OnPriceChanged(val price: String) : CreateListingAction
    data class OnBedroomsChanged(val bedrooms: Int) : CreateListingAction
    data class OnBathroomsChanged(val bathrooms: Int) : CreateListingAction
    data class OnSquareFootageChanged(val sqft: String) : CreateListingAction
    data class OnYearBuiltChanged(val year: String) : CreateListingAction
    data class OnFurnishedChanged(val furnished: FurnishedType) : CreateListingAction

    // Amenities
    data class OnAmenityToggled(val amenity: Amenity) : CreateListingAction

    // Images
    data class OnImagesSelected(val uris: List<String>) : CreateListingAction
    data class OnImageRemoved(val uri: String) : CreateListingAction

    // Contact
    data class OnContactNameChanged(val name: String) : CreateListingAction
    data class OnContactPhoneChanged(val phone: String) : CreateListingAction
    data class OnContactEmailChanged(val email: String) : CreateListingAction

    // Submit
    data object OnSubmit : CreateListingAction
    data object OnSaveDraft : CreateListingAction
}
