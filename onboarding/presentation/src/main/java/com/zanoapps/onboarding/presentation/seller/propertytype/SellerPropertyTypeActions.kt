package com.zanoapps.onboarding.presentation.seller.propertytype

import com.zanoapps.onboarding.domain.enums.seller.PropertyTypeSeller

interface SellerPropertyTypeActions {

    data class OnPreferenceSelected(val preference: PropertyTypeSeller) : SellerPropertyTypeActions
    data object OnBackClick : SellerPropertyTypeActions
    data object OnNextClick : SellerPropertyTypeActions
    data object OnSkipClick : SellerPropertyTypeActions
}