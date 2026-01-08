package com.zanoapps.core.domain.enums

enum class ParkingType(val key: String, val displayName: String) {
    GARAGE("garage", "Garage"),
    COVERED("covered", "Covered Parking"),
    DRIVEWAY("driveway", "Driveway"),
    STREET("street", "Street Parking"),
    UNDERGROUND("underground", "Underground Parking"),
    CARPORT("carport", "Carport"),
    VALET("valet", "Valet Parking"),
    NONE("none", "No Parking")
}