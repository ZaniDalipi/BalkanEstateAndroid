package com.zanoapps.onboarding.presentation.seller.sellercompletion

import com.zanoapps.onboarding.presentation.buyer.currentlifesituation.CurrentLifeSituationAction
import com.zanoapps.onboarding.presentation.seller.propertytype.SellerPropertyTypeAction

sealed interface SellerCompletionAction {
    data object PropertyValuation: SellerPropertyTypeAction
    data object OnRegister: SellerCompletionAction
    data object OnBackClick : SellerCompletionAction
}