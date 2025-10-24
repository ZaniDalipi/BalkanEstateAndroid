package com.zanoapps.onboarding.presentation.seller.propertytype

import com.zanoapps.onboarding.domain.enums.seller.PropertyTypeSeller

data class PropertyTypeState(

    val propertyTypeSeller: PropertyTypeSeller? = null,
    val canNavigateBack: Boolean = true,
    val progress: Float = 0.33f,
    val isLoading: Boolean = false
)

