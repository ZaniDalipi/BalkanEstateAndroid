package com.zanoapps.profile.presentation.subscription

import com.zanoapps.presentation.ui.UiText

sealed interface SubscriptionEvent {
    data class Error(val error: UiText) : SubscriptionEvent
    data object SubscriptionSuccess : SubscriptionEvent
    data object NavigateBack : SubscriptionEvent
}
