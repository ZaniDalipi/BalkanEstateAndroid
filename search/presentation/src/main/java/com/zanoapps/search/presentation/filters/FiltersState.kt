package com.zanoapps.search.presentation.filters

import com.zanoapps.core.domain.enums.Amenity
import com.zanoapps.core.domain.enums.FurnishedType
import com.zanoapps.core.domain.enums.ListingType
import com.zanoapps.core.domain.enums.PropertyType

data class FiltersState(
    // Listing Type
    val selectedListingType: ListingType? = null,

    // Property Type
    val selectedPropertyTypes: Set<PropertyType> = emptySet(),

    // Price Range
    val minPrice: String = "",
    val maxPrice: String = "",

    // Bedrooms & Bathrooms
    val minBedrooms: Int = 0,
    val minBathrooms: Int = 0,

    // Size
    val minSize: String = "",
    val maxSize: String = "",

    // Furnished
    val furnishedType: FurnishedType? = null,

    // Amenities
    val selectedAmenities: Set<Amenity> = emptySet(),

    // Other
    val onlyWithPhotos: Boolean = false,
    val onlyFeatured: Boolean = false,
    val onlyNew: Boolean = false,

    // State
    val activeFiltersCount: Int = 0
)
