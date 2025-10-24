package com.zanoapps.onboarding.presentation.seller.maingoal

import com.zanoapps.onboarding.domain.enums.seller.MainGoal

sealed interface SellerMainGoalAction {
    data class OnPreferenceSelected(val preference: MainGoal) : SellerMainGoalAction
    data object OnBackClick : SellerMainGoalAction
    data object OnNextClick : SellerMainGoalAction
    data object OnSkipClick : SellerMainGoalAction
}

