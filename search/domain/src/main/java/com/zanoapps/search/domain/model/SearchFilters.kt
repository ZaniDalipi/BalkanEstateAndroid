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
    val parkingType: Set<ParkingType> = emptySet(),
    val boundingBox: BoundingBox? = null
) {
    fun hasActiveFilters(): Boolean {
        return minPrice != null ||
                maxPrice != null ||
                propertyTypes.isNotEmpty() ||
                bedrooms != null ||
                bathrooms != null ||
                minSquareFootage != null ||
                maxSquareFootage != null ||
                listingTypes.isNotEmpty() ||
                amenities.isNotEmpty() ||
                furnishedType.isNotEmpty() ||
                petPolicy.isNotEmpty() ||
                parkingType.isNotEmpty()
    }
}

data class BoundingBox(
    val northEastLat: Double,
    val northEastLng: Double,
    val southWestLat: Double,
    val southWestLng: Double
)
