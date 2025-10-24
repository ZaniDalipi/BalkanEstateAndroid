package com.zanoapps.onboarding.presentation.seller.propertytype

import com.zanoapps.onboarding.domain.enums.seller.PropertyTypeSeller

interface SellerPropertyTypeAction {

    data class OnPreferenceSelected(val preference: PropertyTypeSeller) : SellerPropertyTypeAction
    data object OnBackClick : SellerPropertyTypeAction
    data object OnNextClick : SellerPropertyTypeAction
    data object OnSkipClick : SellerPropertyTypeAction
}