package com.zanoapps.property_details.domain.enums

enum class Amenity(val key: String) {
    // Building Amenities
    POOL("pool"),
    GYM("gym"),
    SPA("spa"),
    SAUNA("sauna"),
    CONCIERGE("concierge"),
    DOORMAN("doorman"),
    ELEVATOR("elevator"),
    LAUNDRY_ROOM("laundry_room"),
    STORAGE("storage"),
    BIKE_STORAGE("bike_storage"),
    ROOFTOP_TERRACE("rooftop_terrace"),
    BUSINESS_CENTER("business_center"),
    CONFERENCE_ROOM("conference_room"),
    PLAYGROUND("playground"),
    BBQ_AREA("bbq_area"),
    GARDEN("garden"),

    // Unit Amenities
    BALCONY("balcony"),
    TERRACE("terrace"),
    PATIO("patio"),
    FIREPLACE("fireplace"),
    WALK_IN_CLOSET("walk_in_closet"),
    HARDWOOD_FLOORS("hardwood_floors"),
    HIGH_CEILINGS("high_ceilings"),
    IN_UNIT_LAUNDRY("in_unit_laundry"),
    AIR_CONDITIONING("air_conditioning"),
    CENTRAL_HEATING("central_heating"),

    // Security
    SECURITY_SYSTEM("security_system"),
    GATED_COMMUNITY("gated_community"),
    SECURITY_CAMERAS("security_cameras"),
    INTERCOM("intercom"),

    // Outdoor
    PARKING("parking"),
    GARAGE("garage"),
    DRIVEWAY("driveway"),
    YARD("yard"),

    // Utilities
    WIFI_INCLUDED("wifi_included"),
    UTILITIES_INCLUDED("utilities_included"),
    CABLE_INCLUDED("cable_included"),

    // Location
    NEAR_TRANSPORT("near_transport"),
    SHOPPING_NEARBY("shopping_nearby"),
    SCHOOLS_NEARBY("schools_nearby"),
    PARKS_NEARBY("parks_nearby")

    }