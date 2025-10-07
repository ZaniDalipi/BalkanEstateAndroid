package com.zanoapps.onboarding.presentation.buyer.currentlifesituation

import com.zanoapps.onboarding.domain.enums.buyer.LifeSituation
import com.zanoapps.onboarding.domain.enums.buyer.PropertyIntent

sealed interface CurrentLifeSituationActions {
    data class OnToggleLifeSituation(val preference: LifeSituation) : CurrentLifeSituationActions
    data object OnNextClick : CurrentLifeSituationActions
    data object OnBackClick : CurrentLifeSituationActions
    data object OnSkipClick : CurrentLifeSituationActions
}