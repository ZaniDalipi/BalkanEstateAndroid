package com.zanoapps.property_details.domain.enums

enum class PropertyStatus(val key: String) {
    AVAILABLE("available"),
    PENDING("pending"),
    SOLD("sold"),
    RENTED("rented"),
    OFF_MARKET("off_market"),
    DRAFT("draft")
}