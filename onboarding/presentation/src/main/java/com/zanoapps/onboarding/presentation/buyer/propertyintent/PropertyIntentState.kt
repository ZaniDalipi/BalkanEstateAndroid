package com.zanoapps.onboarding.presentation.buyer.propertyintent

import com.zanoapps.onboarding.domain.enums.buyer.PropertyIntent

data class PropertyIntentState(
    val propertyIntent: PropertyIntent? = null,
    val canNavigateBack: Boolean = true,
    val progress: Float = 0.66f,
    val isLoading: Boolean = false
)
