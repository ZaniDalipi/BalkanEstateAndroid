package com.zanoapps.onboarding.domain.enums.buyer

enum class Amenity(
    val title: String,
    val description: String
) {
    // Location & Neighborhood
    PRIME_BUSINESS_LOCATION("Prime Business Location", "Situated in the heart of the city's central business district"),
    QUIET_NEIGHBORHOOD("Quiet Neighborhood", "Peaceful, low-traffic residential area"),

    // Education
    GOOD_SCHOOLS("Good Schools", "Quality K-12 educational institutions nearby"),
    EDUCATION("Near Universities", "Minutes away from leading universities and colleges"),

    // Transportation & Access
    PUBLIC_TRANSPORT("Public Transport", "Easy access to buses, trains, and metro"),
    PARKING("Convenient Parking", "Secure, on-site parking available"),

    // Lifestyle & Daily Needs
    SHOPPING_CENTERS("Shopping Centers", "Malls, markets, and retail areas nearby"),
    RESTAURANTS_CAFES("Restaurants & Cafes", "Wide variety of dining and coffee options"),
    GROCERY_STORES("Grocery Stores", "Convenient access to supermarkets and fresh markets"),

    // Health & Recreation
    HEALTHCARE("Healthcare", "Hospitals, clinics, and medical facilities nearby"),
    GYM_FITNESS("Fitness Center", "On-site gym or fitness facility access"),
    PARKS_RECREATION("Parks & Recreation", "Green spaces, trails, and outdoor activities"),

    // Work & Connectivity
    INTERNET("High-Speed Internet", "Reliable high-speed fiber internet included"),
    COWORKING_SPACE("Co-Working Space", "Professional workspaces and business lounge"),

    // Entertainment & Social
    ENTERTAINMENT("Entertainment", "Movie theaters, bowling, and family activities"),
    NIGHTLIFE("Nightlife", "Bars, clubs, and social venues for evening entertainment"),

    // Property Features
    PET_POLICY("Pet-Friendly", "Pet-friendly community with amenities"),
    FURNISHED("Furnished", "Comes with furniture and essential appliances included"),
    INVESTMENT("High Rental Demand", "Strong rental potential in desirable neighborhood"),

    // Safety & Security
    SECURITY("Security", "24/7 security, gated access, and surveillance systems"),
    MAINTENANCE("Maintenance", "On-call maintenance and repair services"),

    // renting
    RENTAL_POTENTIAL("Rental Potential", "High demand area with strong rental market")
}