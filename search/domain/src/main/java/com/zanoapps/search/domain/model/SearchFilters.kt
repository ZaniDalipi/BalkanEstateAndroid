package com.zanoapps.search.domain.model

import com.zanoapps.core.domain.enums.Amenity
import com.zanoapps.core.domain.enums.FurnishedType
import com.zanoapps.core.domain.enums.ListingType
import com.zanoapps.core.domain.enums.ParkingType
import com.zanoapps.core.domain.enums.PetPolicy
import com.zanoapps.core.domain.enums.PropertyType

data class SearchFilters(
    val query: String = "",
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val propertyTypes: Set<PropertyType> = emptySet(),
    val bedrooms: Int? = null,
    val bathrooms: Int? = null,
    val minSquareFootage: Int? = null,
    val maxSquareFootage: Int? = null,
    val listingTypes: Set<ListingType> = emptySet(),
    val amenities: Set<Amenity> = emptySet(),
    val furnishedType: Set<FurnishedType> = emptySet(),
    val petPolicy: Set<PetPolicy> = emptySet(),
    val parkingType: Set<ParkingType> = emptySet()
)
