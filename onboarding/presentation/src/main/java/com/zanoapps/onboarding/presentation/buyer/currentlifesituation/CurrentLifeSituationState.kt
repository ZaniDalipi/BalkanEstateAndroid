package com.zanoapps.onboarding.presentation.buyer.currentlifesituation

import com.zanoapps.onboarding.domain.enums.buyer.LifeSituation

data class CurrentLifeSituationBuyerState(
    val selectedLifeSituations: List<LifeSituation> = emptyList(),
    val canNavigateBack: Boolean = true,
    val canNavigateNext: Boolean = false,
    val progress: Float = 0.4f,
    val isLoading: Boolean = false
)