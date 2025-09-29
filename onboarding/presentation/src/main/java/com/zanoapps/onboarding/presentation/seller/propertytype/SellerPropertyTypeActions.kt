package com.zanoapps.onboarding.presentation.seller.propertytype

import com.zanoapps.onboarding.domain.enums.buyer.LifeSituation
import com.zanoapps.onboarding.domain.enums.seller.PropertyTypeSeller
import com.zanoapps.onboarding.presentation.buyer.currentlifesituation.CurrentLifeSituationActions

interface SellerPropertyTypeActions {

    data class OnPreferenceSelected(val preference: PropertyTypeSeller) : SellerPropertyTypeActions
    data object OnBackClick : SellerPropertyTypeActions
    data object OnNextClick : SellerPropertyTypeActions
    data object OnSkipClick : SellerPropertyTypeActions
}