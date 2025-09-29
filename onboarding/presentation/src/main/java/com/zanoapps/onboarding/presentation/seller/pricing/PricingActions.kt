package com.zanoapps.onboarding.presentation.seller.pricing

sealed interface PricingActions {
    data class OnSelectPlanClick(val option: PricingCTAStyle): PricingActions
    data object OnSkipClicked: PricingActions

}