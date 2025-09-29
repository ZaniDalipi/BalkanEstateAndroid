package com.zanoapps.onboarding.presentation.seller.sellingtime

import com.zanoapps.onboarding.domain.enums.seller.SellingTime

sealed interface SellingTimeActions {

    data class OnPreferenceSelected(val preference: SellingTime) : SellingTimeActions
    data object OnBackClick : SellingTimeActions
    data object OnNextClick : SellingTimeActions
    data object OnSkipClick : SellingTimeActions
}