package com.zanoapps.onboarding.presentation.clientintent

import com.zanoapps.onboarding.domain.enums.ClientIntent

sealed interface ClientIntentAction {
    data class OnOptionSelected(val intent: ClientIntent): ClientIntentAction
    data object OnSkipClick : ClientIntentAction
    data object OnNavigateToNextScreen : ClientIntentAction
}