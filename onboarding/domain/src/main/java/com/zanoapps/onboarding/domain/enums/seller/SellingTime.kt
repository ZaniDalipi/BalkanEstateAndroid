package com.zanoapps.onboarding.domain.enums.seller

enum class SellingTime(
    val displayName: String,
    val description: String
) {
    SOON(displayName = "As Soon As Possible", description = "I need to sell within 1-2 months"),
    THREE_MONTHS(displayName = "Within 3 Months", description = "I have some flexibility but want to sell soon"),
    SIX_MONTHS(displayName = "Within 6 Months", description = "I can wait for the right buyer and price"),
    NO_RUSH(displayName = "No Rush", description = "I want to get the best possible price")
}