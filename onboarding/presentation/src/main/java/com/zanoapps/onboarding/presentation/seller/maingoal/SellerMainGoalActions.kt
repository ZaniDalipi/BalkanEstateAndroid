package com.zanoapps.onboarding.presentation.seller.maingoal

import com.zanoapps.onboarding.domain.enums.seller.MainGoal

sealed interface SellerMainGoalActions {
    data class OnPreferenceSelected(val preference: MainGoal) : SellerMainGoalActions
    data object OnBackClick : SellerMainGoalActions
    data object OnNextClick : SellerMainGoalActions
    data object OnSkipClick : SellerMainGoalActions
}