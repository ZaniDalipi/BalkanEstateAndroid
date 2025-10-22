package com.zanoapps.onboarding.domain.enums.buyer

enum class LifeSituation(
    val title: String,
    val description: String
) {
    STUDENT("Student", "Budget-friendly options near campuses with study spaces"),
    YOUNG_PROFESSIONAL("Young Professional", "Urban locations with commute convenience and social amenities"),
    REMOTE_WORKER("Remote Worker", "Home office potential with high-speed internet and quiet spaces"),
    SINGLE("Single", "Low-maintenance living with access to social and recreational activities"),
    COUPLE("Couple", "Comfortable spaces for shared living with growth potential"),
    GROWING_FAMILY("Growing Family", "Extra bedrooms, outdoor space, and child-friendly neighborhoods"),
    ESTABLISHED_FAMILY("Established Family", "Top-rated schools, community amenities, and family activities"),
    EMPTY_NESTERS("Empty Nesters", "Right-sized homes with minimal upkeep and premium amenities"),
    RETIREE("Retiree", "Single-level living, healthcare access, and leisure activities"),
    INVESTOR("Investor", "Properties with strong rental demand and appreciation potential")
}