package com.zanoapps.onboarding.domain.enums.seller

enum class PropertyTypeSeller(
    val displayName: String,
    val description: String
) {
    APARTMENT_CONDO("Apartment/Condo", "Unit in a building or complex"),
    HOUSE("House", "Detached or semi-detached home"),
    VILLA_LUXURY_HOME("Villa/Luxury Home", "Premium property with special features"),
    LAND_PLOT("Land/Plot", "Vacant land for development"),
    COMMERCIAL_PROPERTY("Commercial Property", "Office, retail, or business space")

}