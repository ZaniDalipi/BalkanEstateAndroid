package com.zanoapps.onboarding.presentation.buyer.currentlifesituation

import com.zanoapps.onboarding.domain.enums.buyer.LifeSituation

sealed interface CurrentLifeSituationAction {
    data class OnPreferenceSelected(val preference: LifeSituation) : CurrentLifeSituationAction
    data object OnBackClick : CurrentLifeSituationAction
    data object OnNextClick : CurrentLifeSituationAction
    data object OnSkipClick : CurrentLifeSituationAction
}