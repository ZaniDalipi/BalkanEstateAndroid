package com.zanoapps.onboarding.presentation.buyer.amenities

import com.zanoapps.onboarding.domain.enums.buyer.Amenity

sealed interface AmenitiesAction {
    data class OnPreferenceSelected(val amenity: Amenity) : AmenitiesAction
    object OnBackClick : AmenitiesAction
    object OnNextClick : AmenitiesAction
    object OnSkipClick : AmenitiesAction
}
