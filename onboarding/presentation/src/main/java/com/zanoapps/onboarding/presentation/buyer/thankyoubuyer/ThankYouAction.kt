package com.zanoapps.onboarding.presentation.buyer.thankyoubuyer

import com.zanoapps.onboarding.presentation.buyer.currentlifesituation.CurrentLifeSituationAction


sealed interface ThankYouAction {
    // TODO("Object with all details in it")
    data object OnSearchPropertiesClicked : ThankYouAction
    data object OnBackClick : ThankYouAction

}