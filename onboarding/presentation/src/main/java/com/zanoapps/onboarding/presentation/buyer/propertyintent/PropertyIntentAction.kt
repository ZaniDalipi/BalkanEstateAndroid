package com.zanoapps.onboarding.presentation.buyer.propertyintent

import com.zanoapps.onboarding.domain.enums.buyer.PropertyIntent

sealed interface PropertyIntentAction {
    data class OnPreferenceSelected(val propertyIntent: PropertyIntent) : PropertyIntentAction
    object OnBackClick : PropertyIntentAction
    object OnNextClick : PropertyIntentAction
    object OnSkipClick : PropertyIntentAction
}