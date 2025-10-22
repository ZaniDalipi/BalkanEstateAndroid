package com.zanoapps.onboarding.presentation.seller.sellingtime

import com.zanoapps.onboarding.domain.enums.seller.SellingTime

sealed interface SellingTimeAction {

    data class OnPreferenceSelected(val preference: SellingTime) : SellingTimeAction
    data object OnBackClick : SellingTimeAction
    data object OnNextClick : SellingTimeAction
    data object OnSkipClick : SellingTimeAction
}