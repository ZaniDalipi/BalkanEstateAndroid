package com.zanoapps.onboarding.presentation.seller.propertytype

import com.zanoapps.onboarding.domain.enums.seller.PropertyTypeSeller

sealed interface SellerPropertyTypeAction {
    data class OnPreferenceSelected(val preference: PropertyTypeSeller) : SellerPropertyTypeAction
    data class OnProgressUpdate(val progress: Float) : SellerPropertyTypeAction
    data object OnBackClick : SellerPropertyTypeAction
    data object OnNextClick : SellerPropertyTypeAction
    data object OnSkipClick : SellerPropertyTypeAction
}