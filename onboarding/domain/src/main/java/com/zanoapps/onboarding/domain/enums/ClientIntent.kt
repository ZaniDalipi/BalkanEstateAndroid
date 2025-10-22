package com.zanoapps.onboarding.domain.enums

enum class ClientIntent(
    val title: String,
    val description: String,
    val navigationPath: String
) {
    BUY_RENT("I'm Looking to Buy/Rent", "Find more than a house. Find a place where your life can grow.", "buy_rent_path"),
    SELL("I Want to Sell My Property","Sell your home faster and for more money, without the hassle. Start with a free valuation from Balkan Estate.", "sell_path")
}