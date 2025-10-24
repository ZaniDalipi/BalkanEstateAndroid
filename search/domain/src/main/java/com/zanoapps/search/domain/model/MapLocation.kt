package com.zanoapps.search.domain.model

data class MapLocation(
    val latitude: Double,
    val longitude: Double,
    val zoom: Float = 10f
)