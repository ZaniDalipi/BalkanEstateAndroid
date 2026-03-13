package com.zanoapps.property_details.presentation.create

sealed interface CreateListingAction {
    data object OnNextStep : CreateListingAction
    data object OnPreviousStep : CreateListingAction
    data class OnTitleChanged(val title: String) : CreateListingAction
    data class OnDescriptionChanged(val description: String) : CreateListingAction
    data class OnPropertyTypeChanged(val type: String) : CreateListingAction
    data class OnListingTypeChanged(val type: String) : CreateListingAction
    data class OnAddressChanged(val address: String) : CreateListingAction
    data class OnCityChanged(val city: String) : CreateListingAction
    data class OnCountryChanged(val country: String) : CreateListingAction
    data class OnPostalCodeChanged(val code: String) : CreateListingAction
    data class OnPriceChanged(val price: String) : CreateListingAction
    data class OnCurrencyChanged(val currency: String) : CreateListingAction
    data class OnBedroomsChanged(val bedrooms: String) : CreateListingAction
    data class OnBathroomsChanged(val bathrooms: String) : CreateListingAction
    data class OnSquareFootageChanged(val sqft: String) : CreateListingAction
    data class OnYearBuiltChanged(val year: String) : CreateListingAction
    data class OnParkingChanged(val parking: String) : CreateListingAction
    data class OnFloorChanged(val floor: String) : CreateListingAction
    data class OnAmenityToggle(val amenity: String) : CreateListingAction
    data class OnFurnishedTypeChanged(val type: String) : CreateListingAction
    data class OnHeatingTypeChanged(val type: String) : CreateListingAction
    data class OnPhotoAdded(val uri: String) : CreateListingAction
    data class OnPhotoRemoved(val index: Int) : CreateListingAction
    data object OnSubmitListing : CreateListingAction
    data object OnBackClick : CreateListingAction
}
