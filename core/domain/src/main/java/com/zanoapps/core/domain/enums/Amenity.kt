package com.zanoapps.core.domain.enums

enum class Amenity(val key: String, val displayName: String, val category: AmenityCategory) {
    // Building Amenities
    POOL("pool", "Pool", AmenityCategory.BUILDING),
    GYM("gym", "Gym", AmenityCategory.BUILDING),
    SPA("spa", "Spa", AmenityCategory.BUILDING),
    SAUNA("sauna", "Sauna", AmenityCategory.BUILDING),
    CONCIERGE("concierge", "Concierge", AmenityCategory.BUILDING),
    DOORMAN("doorman", "Doorman", AmenityCategory.BUILDING),
    ELEVATOR("elevator", "Elevator", AmenityCategory.BUILDING),
    LAUNDRY_ROOM("laundry_room", "Laundry Room", AmenityCategory.BUILDING),
    STORAGE("storage", "Storage", AmenityCategory.BUILDING),
    BIKE_STORAGE("bike_storage", "Bike Storage", AmenityCategory.BUILDING),
    ROOFTOP_TERRACE("rooftop_terrace", "Rooftop Terrace", AmenityCategory.BUILDING),
    BUSINESS_CENTER("business_center", "Business Center", AmenityCategory.BUILDING),
    CONFERENCE_ROOM("conference_room", "Conference Room", AmenityCategory.BUILDING),
    PLAYGROUND("playground", "Playground", AmenityCategory.BUILDING),
    BBQ_AREA("bbq_area", "BBQ Area", AmenityCategory.BUILDING),
    GARDEN("garden", "Garden", AmenityCategory.BUILDING),

    // Unit Amenities
    BALCONY("balcony", "Balcony", AmenityCategory.UNIT),
    TERRACE("terrace", "Terrace", AmenityCategory.UNIT),
    PATIO("patio", "Patio", AmenityCategory.UNIT),
    FIREPLACE("fireplace", "Fireplace", AmenityCategory.UNIT),
    WALK_IN_CLOSET("walk_in_closet", "Walk-in Closet", AmenityCategory.UNIT),
    HARDWOOD_FLOORS("hardwood_floors", "Hardwood Floors", AmenityCategory.UNIT),
    HIGH_CEILINGS("high_ceilings", "High Ceilings", AmenityCategory.UNIT),
    IN_UNIT_LAUNDRY("in_unit_laundry", "In-Unit Laundry", AmenityCategory.UNIT),
    AIR_CONDITIONING("air_conditioning", "Air Conditioning", AmenityCategory.UNIT),
    CENTRAL_HEATING("central_heating", "Central Heating", AmenityCategory.UNIT),

    // Security
    SECURITY_SYSTEM("security_system", "Security System", AmenityCategory.SECURITY),
    GATED_COMMUNITY("gated_community", "Gated Community", AmenityCategory.SECURITY),
    SECURITY_CAMERAS("security_cameras", "Security Cameras", AmenityCategory.SECURITY),
    INTERCOM("intercom", "Intercom", AmenityCategory.SECURITY),

    // Outdoor
    PARKING("parking", "Parking", AmenityCategory.OUTDOOR),
    GARAGE("garage", "Garage", AmenityCategory.OUTDOOR),
    DRIVEWAY("driveway", "Driveway", AmenityCategory.OUTDOOR),
    YARD("yard", "Yard", AmenityCategory.OUTDOOR),

    // Utilities
    WIFI_INCLUDED("wifi_included", "WiFi Included", AmenityCategory.UTILITIES),
    UTILITIES_INCLUDED("utilities_included", "Utilities Included", AmenityCategory.UTILITIES),
    CABLE_INCLUDED("cable_included", "Cable Included", AmenityCategory.UTILITIES),

    // Location
    NEAR_TRANSPORT("near_transport", "Near Transport", AmenityCategory.LOCATION),
    SHOPPING_NEARBY("shopping_nearby", "Shopping Nearby", AmenityCategory.LOCATION),
    SCHOOLS_NEARBY("schools_nearby", "Schools Nearby", AmenityCategory.LOCATION),
    PARKS_NEARBY("parks_nearby", "Parks Nearby", AmenityCategory.LOCATION);

    companion object {
        fun getByCategory(category: AmenityCategory): List<Amenity> {
            return entries.filter { it.category == category }
        }

        fun getPopularAmenities(): List<Amenity> {
            return listOf(
                POOL, GYM, PARKING, BALCONY, AIR_CONDITIONING,
                ELEVATOR, SECURITY_SYSTEM, GARDEN, IN_UNIT_LAUNDRY
            )
        }
    }
}

enum class AmenityCategory(val displayName: String) {
    BUILDING("Building Amenities"),
    UNIT("Unit Features"),
    SECURITY("Security"),
    OUTDOOR("Outdoor"),
    UTILITIES("Utilities"),
    LOCATION("Location")
}