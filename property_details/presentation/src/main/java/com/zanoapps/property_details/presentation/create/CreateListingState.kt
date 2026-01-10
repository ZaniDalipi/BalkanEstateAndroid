package com.zanoapps.property_details.presentation.create

import com.zanoapps.core.domain.enums.Amenity
import com.zanoapps.core.domain.enums.FurnishedType
import com.zanoapps.core.domain.enums.ListingType
import com.zanoapps.core.domain.enums.PropertyType

data class CreateListingState(
    val currentStep: Int = 0,
    val totalSteps: Int = 4,

    // Basic Info
    val title: String = "",
    val description: String = "",
    val listingType: ListingType? = null,
    val propertyType: PropertyType? = null,

    // Location
    val address: String = "",
    val city: String = "",
    val country: String = "Albania",
    val postalCode: String = "",

    // Details
    val price: String = "",
    val bedrooms: Int = 1,
    val bathrooms: Int = 1,
    val squareFootage: String = "",
    val yearBuilt: String = "",
    val furnished: FurnishedType = FurnishedType.UNFURNISHED,

    // Amenities
    val selectedAmenities: Set<Amenity> = emptySet(),

    // Images
    val imageUris: List<String> = emptyList(),

    // Contact
    val contactName: String = "",
    val contactPhone: String = "",
    val contactEmail: String = "",

    // State
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val validationErrors: Map<String, String> = emptyMap()
)
