package com.zanoapps.search.presentation.filters

import com.zanoapps.core.domain.enums.Amenity
import com.zanoapps.core.domain.enums.FurnishedType
import com.zanoapps.core.domain.enums.ListingType
import com.zanoapps.core.domain.enums.PropertyType

sealed interface FiltersAction {
    // Listing Type
    data class OnListingTypeSelected(val type: ListingType?) : FiltersAction

    // Property Type
    data class OnPropertyTypeToggled(val type: PropertyType) : FiltersAction

    // Price
    data class OnMinPriceChanged(val price: String) : FiltersAction
    data class OnMaxPriceChanged(val price: String) : FiltersAction

    // Bedrooms & Bathrooms
    data class OnMinBedroomsChanged(val count: Int) : FiltersAction
    data class OnMinBathroomsChanged(val count: Int) : FiltersAction

    // Size
    data class OnMinSizeChanged(val size: String) : FiltersAction
    data class OnMaxSizeChanged(val size: String) : FiltersAction

    // Furnished
    data class OnFurnishedTypeChanged(val type: FurnishedType?) : FiltersAction

    // Amenities
    data class OnAmenityToggled(val amenity: Amenity) : FiltersAction

    // Other
    data object OnToggleOnlyWithPhotos : FiltersAction
    data object OnToggleOnlyFeatured : FiltersAction
    data object OnToggleOnlyNew : FiltersAction

    // Actions
    data object OnApplyFilters : FiltersAction
    data object OnResetFilters : FiltersAction
    data object OnBackClick : FiltersAction
}
