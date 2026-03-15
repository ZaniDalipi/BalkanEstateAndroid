package com.zanoapps.map.presentation.map

import com.zanoapps.map.domain.model.MapProperty
import com.zanoapps.map.domain.model.MapRegion

data class MapState(
    val properties: List<MapProperty> = emptyList(),
    val selectedProperty: MapProperty? = null,
    val region: MapRegion = MapRegion(),
    val isLoading: Boolean = false
)
