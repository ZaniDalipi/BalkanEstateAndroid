package com.zanoapps.onboarding.domain.enums.buyer

enum class Amenity(
    val title: String,
    val description: String
) {
    GOOD_SCHOOLS("Good Schools", "Quality educational institutions nearby"),
    PARKS_RECREATION("Parks & Recreation", "Green spaces and outdoor activities"),
    PUBLIC_TRANSPORT("Public Transport", "Easy access to buses, trains, metro"),
    SHOPPING_CENTERS("Shopping Centers", "Malls, markets, and retail areas"),
    HEALTHCARE("Healthcare", "Hospitals, clinics, and medical facilities"),
    RESTAURANTS_CAFES("Restaurants & Cafes", "Variety of dining options"),
    ENTERTAINMENT("Entertainment", "Bars, clubs, and nightlife"),
    QUIET_NEIGHBORHOOD("Quiet Neighborhood", "Peaceful, low-traffic areas")

}