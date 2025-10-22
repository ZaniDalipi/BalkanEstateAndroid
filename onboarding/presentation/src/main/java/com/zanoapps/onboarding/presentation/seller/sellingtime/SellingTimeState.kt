package com.zanoapps.onboarding.presentation.seller.sellingtime

import com.zanoapps.onboarding.domain.enums.seller.PropertyTypeSeller
import com.zanoapps.onboarding.domain.enums.seller.SellingTime

data class SellingTimeState(
    val sellingTime: SellingTime? = null,
    val canNavigateBack: Boolean = true,
    val progress: Float = 0.66f,
    val isLoading: Boolean = false
)
