package com.zanoapps.search.presentation.filter

import com.zanoapps.search.domain.model.SearchFilters

data class FilterState(
    val filters: SearchFilters = SearchFilters(),
    val minPriceText: String = "",
    val maxPriceText: String = "",
    val minSqftText: String = "",
    val maxSqftText: String = "",
    val selectedBedrooms: Int? = null,
    val selectedBathrooms: Int? = null,
    val selectedPropertyTypes: Set<String> = emptySet(),
    val selectedListingTypes: Set<String> = emptySet(),
    val selectedAmenities: Set<String> = emptySet(),
    val selectedFurnished: String? = null,
    val selectedParking: String? = null,
    val petFriendly: Boolean = false,
    val hasActiveFilters: Boolean = false
)
