package com.zanoapps.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Property(
    val id: String,
    val title: String,
    val description: String = "",
    val price: Double,
    val currency: String = "EUR",
    val imageUrl: String,
    val images: List<String> = emptyList(),
    val bedrooms: Int,
    val bathrooms: Int,
    val squareMeters: Int,
    val address: String,
    val city: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val propertyType: PropertyType = PropertyType.APARTMENT,
    val listingType: ListingType = ListingType.SALE,
    val agentId: String? = null,
    val agentName: String = "",
    val agentPhone: String = "",
    val agentEmail: String = "",
    val isFeatured: Boolean = false,
    val isUrgent: Boolean = false,
    val isPremium: Boolean = false,
    val amenities: List<String> = emptyList(),
    val yearBuilt: Int? = null,
    val parkingSpots: Int = 0,
    val floorNumber: Int? = null,
    val totalFloors: Int? = null,
    val heatingType: String? = null,
    val furnished: FurnishedType = FurnishedType.UNFURNISHED,
    val virtualTourUrl: String? = null,
    val createdAt: Long = 0,
    val updatedAt: Long = 0
)

@Serializable
enum class PropertyType {
    APARTMENT,
    HOUSE,
    VILLA,
    STUDIO,
    PENTHOUSE,
    DUPLEX,
    COMMERCIAL,
    LAND,
    OFFICE,
    WAREHOUSE,
    OTHER
}

@Serializable
enum class ListingType {
    SALE,
    RENT,
    RENT_TO_OWN,
    AUCTION
}

@Serializable
enum class FurnishedType {
    FURNISHED,
    SEMI_FURNISHED,
    UNFURNISHED
}

@Serializable
data class PropertyFilter(
    val query: String = "",
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val minBedrooms: Int? = null,
    val maxBedrooms: Int? = null,
    val minBathrooms: Int? = null,
    val propertyTypes: List<PropertyType> = emptyList(),
    val listingTypes: List<ListingType> = emptyList(),
    val cities: List<String> = emptyList(),
    val countries: List<String> = emptyList(),
    val amenities: List<String> = emptyList(),
    val minSquareMeters: Int? = null,
    val maxSquareMeters: Int? = null
)

@Serializable
enum class SortOption {
    NEWEST,
    OLDEST,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    SQUARE_METERS_LOW_TO_HIGH,
    SQUARE_METERS_HIGH_TO_LOW
}
