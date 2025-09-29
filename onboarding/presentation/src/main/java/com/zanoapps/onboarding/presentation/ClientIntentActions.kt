package com.zanoapps.onboarding.presentation

import com.zanoapps.onboarding.domain.enums.ClientIntent
import com.zanoapps.onboarding.presentation.seller.sellingtime.SellingTimeActions

sealed interface ClientIntentActions {
    data class OnOptionSelected(val option: ClientIntent): ClientIntentActions
    data object OnSkipClick : ClientIntentActions
}