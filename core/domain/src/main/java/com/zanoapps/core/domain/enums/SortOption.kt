package com.zanoapps.core.domain.enums

enum class SortOption(val displayName: String) {
    PRICE_LOW_TO_HIGH("Price: Low to High"),
    PRICE_HIGH_TO_LOW("Price: High to Low"),
    NEWEST("Newest First"),
    OLDEST("Oldest First"),
    BEDROOMS("Most Bedrooms"),
    SQUARE_FOOTAGE("Largest First"),
    FEATURED("Featured First")
}