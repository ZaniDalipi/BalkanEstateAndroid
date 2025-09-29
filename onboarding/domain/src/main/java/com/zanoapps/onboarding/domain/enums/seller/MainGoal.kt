package com.zanoapps.onboarding.domain.enums.seller

enum class MainGoal(
    val displayName: String,
    val description: String
) {
    MAX_SALE(displayName = "Maximize Sale Price", description = "Get the highest possible value"),
    QUICK_SALE(displayName = "Quick Sale", description = "Sell as fast as possible"),
    MIN_HASSLE(displayName = "Minimize Hassle", description = "Easy process with professional handling"),
    STAY_FLEXIBLE(displayName = "Stay Flexible", description = "Open to different approaches")
}