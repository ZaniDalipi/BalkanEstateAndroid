package com.zanoapps.core.domain.enums

enum class Appliance(displayName: String) {
    // Kitchen
    REFRIGERATOR("refrigerator"),
    DISHWASHER("dishwasher"),
    STOVE("stove"),
    OVEN("oven"),
    MICROWAVE("microwave"),
    GARBAGE_DISPOSAL("garbage_disposal"),
    WINE_COOLER("wine_cooler"),
    ICE_MAKER("ice_maker"),

    // Laundry
    WASHER("washer"),
    DRYER("dryer"),
    WASHER_DRYER_COMBO("washer_dryer_combo"),

    // Climate
    AIR_CONDITIONER("air_conditioner"),
    HEATER("heater"),
    CEILING_FANS("ceiling_fans"),

    // Other
    WATER_HEATER("water_heater"),
    WATER_FILTER("water_filter"),
    HUMIDIFIER("humidifier")
}
