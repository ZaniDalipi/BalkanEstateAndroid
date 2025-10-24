package com.zanoapps.core.domain.enums

enum class PropertySubType(
    val displayName: String,
    val parentType: PropertyType
) {
    SINGLE_FAMILY("single_family", PropertyType.HOUSE),
    MULTI_FAMILY("multi_family", PropertyType.HOUSE),
    COTTAGE("cottage", PropertyType.HOUSE),
    MANSION("mansion", PropertyType.HOUSE),
    BUNGALOW("bungalow", PropertyType.HOUSE),

    // Apartment subtypes
    HIGH_RISE("high_rise", PropertyType.APARTMENT),
    GARDEN_STYLE("garden_style", PropertyType.APARTMENT),
    DUPLEX("duplex", PropertyType.APARTMENT),
    TRIPLEX("triplex", PropertyType.APARTMENT),
    PENTHOUSE("penthouse", PropertyType.APARTMENT),


    // Commercial subtypes
    OFFICE("office", PropertyType.COMMERCIAL),
    RETAIL("retail", PropertyType.COMMERCIAL),
    WAREHOUSE("warehouse", PropertyType.COMMERCIAL),
    RESTAURANT("restaurant", PropertyType.COMMERCIAL),
    HOTEL("hotel", PropertyType.COMMERCIAL),
    INDUSTRIAL("industrial", PropertyType.COMMERCIAL),

    // Land subtypes
    RESIDENTIAL_LAND("residential_land", PropertyType.LAND),
    COMMERCIAL_LAND("commercial_land", PropertyType.LAND),
    AGRICULTURAL_LAND("agricultural_land", PropertyType.LAND),
    VACANT_LOT("vacant_lot", PropertyType.LAND)
}