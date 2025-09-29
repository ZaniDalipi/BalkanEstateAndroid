package com.zanoapps.onboarding.presentation.buyer.amenities

import com.zanoapps.onboarding.domain.enums.buyer.Amenity
import com.zanoapps.onboarding.domain.enums.buyer.LifeSituation

sealed interface AmenitiesActions {
        data class OnPreferenceSelected(val preference: AmenitiesActions) : AmenitiesActions
        data object OnBackClick : AmenitiesActions
        data object OnNextClick : AmenitiesActions
        data object OnSkipClick : AmenitiesActions
    }
