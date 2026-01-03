package com.zanoapps.map.presentation.map

import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.map.domain.model.PropertyMarker

data class MapState(
    val markers: List<PropertyMarker> = emptyList(),
    val selectedProperty: BalkanEstateProperty? = null,
    val cameraLatitude: Double = 41.3275, // Default: Tirana, Albania
    val cameraLongitude: Double = 19.8187,
    val cameraZoom: Float = 12f,
    val isLoading: Boolean = false,
    val isMapTypeNormal: Boolean = true,
    val showPropertyCard: Boolean = false
)
