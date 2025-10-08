package com.zanoapps.onboarding.presentation.buyer.amenities

import com.zanoapps.onboarding.domain.enums.buyer.Amenity

data class AmenityState(
    val savedAmenities: List<Amenity> = emptyList(),
    val canNavigateBack: Boolean = true,
    val progress: Float = 1f,
    val isLoading: Boolean = false

)
