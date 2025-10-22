package com.zanoapps.onboarding.presentation.clientintent

import com.zanoapps.onboarding.domain.enums.ClientIntent

data class ClientIntentState(
    val clientSelectedOption: ClientIntent? = null,
    val shouldNavigate: Boolean = false,
    val navigationPath: String? = null
)