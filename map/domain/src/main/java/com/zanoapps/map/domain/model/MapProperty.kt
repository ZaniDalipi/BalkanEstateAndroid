package com.zanoapps.map.domain.model

data class MapProperty(
    val id: String,
    val title: String,
    val price: Double,
    val currency: String = "EUR",
    val latitude: Double,
    val longitude: Double,
    val propertyType: String = "",
    val imageUrl: String = "",
    val bedrooms: Int = 0,
    val bathrooms: Int = 0,
    val area: Double = 0.0
)

data class MapRegion(
    val centerLatitude: Double = 41.9981,
    val centerLongitude: Double = 21.4254,
    val latitudeDelta: Double = 2.0,
    val longitudeDelta: Double = 2.0
)
