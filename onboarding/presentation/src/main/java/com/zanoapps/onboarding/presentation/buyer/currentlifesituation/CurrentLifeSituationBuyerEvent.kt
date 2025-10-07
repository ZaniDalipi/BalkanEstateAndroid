package com.zanoapps.onboarding.presentation.buyer.currentlifesituation

import com.zanoapps.presentation.ui.UiText

sealed interface CurrentLifeSituationBuyerEvent {
    data object NavigateToNextStep
    data object NavigateBack
    data object SkipSurvey
    data class Error(val message: UiText)
}