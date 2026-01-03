package com.zanoapps.map.domain.model

data class PropertyMarker(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val price: String,
    val propertyType: String
)
