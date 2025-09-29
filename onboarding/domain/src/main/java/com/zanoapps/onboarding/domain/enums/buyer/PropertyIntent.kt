package com.zanoapps.onboarding.domain.enums.buyer

enum class PropertyIntent(
    val title: String,
    val description: String
) {
    BUY("Buy", "I want to purchase a property"),
    RENT("Rent", "I'm looking for a rental property"),
    OPEN_TO_BOTH("Open to Both", "Show me buying and rental options")

}