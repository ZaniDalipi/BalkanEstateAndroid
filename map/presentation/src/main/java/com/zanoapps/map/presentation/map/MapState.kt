package com.zanoapps.map.presentation.map

import androidx.compose.foundation.text.input.TextFieldState
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.map.domain.model.PropertyMarker

enum class MapLayerType {
    STREET,
    SATELLITE
}

enum class PropertyCategory {
    ALL,
    RESIDENTIAL,
    COMMERCIAL,
    LAND
}

data class MapState(
    val markers: List<PropertyMarker> = emptyList(),
    val properties: List<BalkanEstateProperty> = emptyList(),
    val selectedProperty: BalkanEstateProperty? = null,
    val cameraLatitude: Double = 42.0, // Default: Balkans center
    val cameraLongitude: Double = 20.5,
    val cameraZoom: Float = 8f,
    val isLoading: Boolean = false,
    val mapLayerType: MapLayerType = MapLayerType.STREET,
    val showPropertyCard: Boolean = false,
    val isDrawerOpen: Boolean = false,
    val isDrawingMode: Boolean = false,
    val show3DBuildings: Boolean = true,
    val selectedCategory: PropertyCategory = PropertyCategory.ALL,
    val isListView: Boolean = false,
    val searchQuery: TextFieldState = TextFieldState(),
    val drawnAreaBounds: DrawnAreaBounds? = null
)

data class DrawnAreaBounds(
    val south: Double,
    val west: Double,
    val north: Double,
    val east: Double
)
