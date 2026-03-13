package com.zanoapps.profile.presentation.subscription

sealed interface SubscriptionAction {
    data class OnPlanSelected(val plan: SubscriptionPlan) : SubscriptionAction
    data class OnBillingToggle(val isAnnual: Boolean) : SubscriptionAction
    data object OnSubscribe : SubscriptionAction
    data object OnBackClick : SubscriptionAction
}
