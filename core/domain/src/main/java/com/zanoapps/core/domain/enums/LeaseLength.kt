package com.zanoapps.core.domain.enums

enum class LeaseLength(val key: String, val months: Int) {
    MONTHLY("monthly", 1),
    SIX_MONTHS("six_months", 6),
    YEARLY("yearly", 12),
    TWO_YEARS("two_years", 24),
    FLEXIBLE("flexible", 0)
}