package com.zanoapps.search.presentation.filter

sealed interface FilterAction {
    data class OnMinPriceChanged(val price: String) : FilterAction
    data class OnMaxPriceChanged(val price: String) : FilterAction
    data class OnMinSqftChanged(val sqft: String) : FilterAction
    data class OnMaxSqftChanged(val sqft: String) : FilterAction
    data class OnBedroomsSelected(val bedrooms: Int?) : FilterAction
    data class OnBathroomsSelected(val bathrooms: Int?) : FilterAction
    data class OnPropertyTypeToggle(val type: String) : FilterAction
    data class OnListingTypeToggle(val type: String) : FilterAction
    data class OnAmenityToggle(val amenity: String) : FilterAction
    data class OnFurnishedSelected(val type: String?) : FilterAction
    data class OnParkingSelected(val type: String?) : FilterAction
    data class OnPetFriendlyToggle(val value: Boolean) : FilterAction
    data object OnApplyFilters : FilterAction
    data object OnClearFilters : FilterAction
    data object OnBackClick : FilterAction
}
