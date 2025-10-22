package com.zanoapps.onboarding.presentation.seller.maingoal

import com.zanoapps.onboarding.domain.enums.seller.MainGoal

data class SellerMainGoalState(

    val sellerMainGoal: MainGoal?  = null,
    val canNavigateBack: Boolean = true,
    val progress: Float = 1f,
    val isLoading: Boolean = false
)
