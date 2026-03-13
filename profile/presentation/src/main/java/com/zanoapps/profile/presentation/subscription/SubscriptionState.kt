package com.zanoapps.profile.presentation.subscription

data class SubscriptionState(
    val selectedPlan: SubscriptionPlan = SubscriptionPlan.FREE,
    val isAnnual: Boolean = true,
    val isLoading: Boolean = false,
    val isProcessing: Boolean = false,
    val currentPlan: SubscriptionPlan = SubscriptionPlan.FREE,
    val errorMessage: String? = null
)

enum class SubscriptionPlan(
    val displayName: String,
    val monthlyPrice: Double,
    val annualPrice: Double,
    val features: List<String>
) {
    FREE(
        displayName = "Free",
        monthlyPrice = 0.0,
        annualPrice = 0.0,
        features = listOf(
            "Browse properties",
            "Save up to 5 properties",
            "1 saved search",
            "Basic filters",
            "Email support"
        )
    ),
    STANDARD(
        displayName = "Standard",
        monthlyPrice = 9.99,
        annualPrice = 99.99,
        features = listOf(
            "Everything in Free",
            "Unlimited saved properties",
            "10 saved searches",
            "Advanced filters",
            "Property alerts",
            "Price history",
            "Priority support",
            "No ads"
        )
    ),
    PREMIUM(
        displayName = "Premium",
        monthlyPrice = 24.99,
        annualPrice = 249.99,
        features = listOf(
            "Everything in Standard",
            "Unlimited saved searches",
            "AI property recommendations",
            "Market analytics",
            "Property valuation tool",
            "Featured listings",
            "Dedicated agent matching",
            "24/7 premium support",
            "Virtual tour access"
        )
    ),
    ENTERPRISE(
        displayName = "Enterprise",
        monthlyPrice = 99.99,
        annualPrice = 999.99,
        features = listOf(
            "Everything in Premium",
            "Unlimited listings",
            "Agency dashboard",
            "Team management",
            "API access",
            "Custom branding",
            "Lead generation tools",
            "CRM integration",
            "Priority placement",
            "Dedicated account manager"
        )
    )
}
