package com.zanoapps.map.presentation.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.map.domain.model.PropertyMarker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    var state by mutableStateOf(MapState())
        private set

    private val eventChannel = Channel<MapEvent>()
    val events = eventChannel.receiveAsFlow()

    fun setProperties(properties: List<BalkanEstateProperty>) {
        val markers = properties.map { property ->
            PropertyMarker(
                id = property.id,
                latitude = property.latitude,
                longitude = property.longitude,
                title = property.title,
                price = "${property.currency}${property.price.toLong()}",
                propertyType = property.propertyType
            )
        }
        state = state.copy(markers = markers)

        // Center map on first property if available
        properties.firstOrNull()?.let { firstProperty ->
            state = state.copy(
                cameraLatitude = firstProperty.latitude,
                cameraLongitude = firstProperty.longitude
            )
        }
    }

    fun onAction(action: MapAction) {
        when (action) {
            MapAction.OnBackClick -> Unit // Handled by navigation
            is MapAction.OnMarkerClick -> selectPropertyById(action.propertyId)
            is MapAction.OnPropertySelected -> {
                state = state.copy(
                    selectedProperty = action.property,
                    showPropertyCard = true
                )
            }
            MapAction.OnDismissPropertyCard -> {
                state = state.copy(
                    selectedProperty = null,
                    showPropertyCard = false
                )
            }
            is MapAction.OnCameraMove -> {
                state = state.copy(
                    cameraLatitude = action.latitude,
                    cameraLongitude = action.longitude,
                    cameraZoom = action.zoom
                )
            }
            MapAction.OnMyLocationClick -> {
                viewModelScope.launch {
                    eventChannel.send(MapEvent.LocationPermissionRequired)
                }
            }
            MapAction.OnZoomIn -> {
                state = state.copy(cameraZoom = (state.cameraZoom + 1f).coerceAtMost(20f))
            }
            MapAction.OnZoomOut -> {
                state = state.copy(cameraZoom = (state.cameraZoom - 1f).coerceAtLeast(3f))
            }
            MapAction.OnMapTypeToggle -> {
                state = state.copy(isMapTypeNormal = !state.isMapTypeNormal)
            }
        }
    }

    private fun selectPropertyById(propertyId: String) {
        // This would typically fetch the property from a repository
        // For now, we'll just show the card
        state = state.copy(showPropertyCard = true)
    }
}
