package com.zanoapps.onboarding.presentation.buyer.currentlifesituation

import com.zanoapps.onboarding.domain.enums.buyer.LifeSituation

data class CurrentLifeSituationBuyerState(
    val savedLifeSituation: LifeSituation? = null,
    val canNavigateBack: Boolean = true,
    val progress: Float = 0.33f,
    val isLoading: Boolean = false
)