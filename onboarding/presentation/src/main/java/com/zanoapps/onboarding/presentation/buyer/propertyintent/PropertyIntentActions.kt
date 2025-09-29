package com.zanoapps.onboarding.presentation.buyer.propertyintent

import com.zanoapps.onboarding.domain.enums.buyer.PropertyIntent

sealed interface PropertyIntentActions {
    data class OnPreferenceSelected(val preference: PropertyIntent) : PropertyIntentActions
    data object OnBackClick : PropertyIntentActions
    data object OnNextClick : PropertyIntentActions
    data object OnSkipClick : PropertyIntentActions
}