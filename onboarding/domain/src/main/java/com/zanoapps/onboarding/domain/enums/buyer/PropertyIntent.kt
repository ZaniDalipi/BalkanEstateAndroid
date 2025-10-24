package com.zanoapps.onboarding.domain.enums.buyer

enum class PropertyIntent(
    val title: String,
    val description: String
) {
    BUY("Buy", "I want to purchase a property"),
    RENT("Rent", "I'm looking for a rental property"),
    OPEN_TO_BOTH("Open to Both", "Show me buying and rental options"),
    BUY_TO_INVEST("Buy to invest", "Im interested in buying to invest in it"),
    JUST_LOOKING_AROUND("Just looking around", "Im just looking around, thank you"),


}