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
        state = state.copy(
            markers = markers,
            properties = properties
        )

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

            is MapAction.OnMarkerClick -> {
                val property = state.properties.find { it.id == action.propertyId }
                property?.let {
                    state = state.copy(
                        selectedProperty = it,
                        showPropertyCard = true
                    )
                }
            }

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

            MapAction.OnToggleMapLayer -> {
                val newLayer = if (state.mapLayerType == MapLayerType.STREET) {
                    MapLayerType.SATELLITE
                } else {
                    MapLayerType.STREET
                }
                state = state.copy(mapLayerType = newLayer)
            }

            MapAction.OnToggleDrawingMode -> {
                state = state.copy(
                    isDrawingMode = !state.isDrawingMode,
                    drawnAreaBounds = if (state.isDrawingMode) null else state.drawnAreaBounds
                )
            }

            MapAction.OnToggle3DBuildings -> {
                state = state.copy(show3DBuildings = !state.show3DBuildings)
            }

            is MapAction.OnCategorySelected -> {
                state = state.copy(selectedCategory = action.category)
                // Filter markers based on category
                filterMarkersByCategory(action.category)
            }

            is MapAction.OnViewModeToggle -> {
                state = state.copy(isListView = action.isListView)
            }

            MapAction.OnOpenDrawer -> {
                state = state.copy(isDrawerOpen = true)
            }

            MapAction.OnCloseDrawer -> {
                state = state.copy(isDrawerOpen = false)
            }

            is MapAction.OnDrawerItemClick -> {
                state = state.copy(isDrawerOpen = false)
                // Navigation handled externally
            }

            MapAction.OnFilterClick -> {
                viewModelScope.launch {
                    eventChannel.send(MapEvent.OpenFilters)
                }
            }

            MapAction.OnProfileClick -> {
                viewModelScope.launch {
                    eventChannel.send(MapEvent.NavigateToProfile)
                }
            }

            is MapAction.OnSearchQueryChanged -> {
                // Search query is handled by TextFieldState
            }

            is MapAction.OnAreaDrawn -> {
                state = state.copy(
                    drawnAreaBounds = DrawnAreaBounds(
                        south = action.south,
                        west = action.west,
                        north = action.north,
                        east = action.east
                    )
                )
                // Filter properties within drawn area
                filterPropertiesInArea()
            }

            MapAction.OnClearDrawnArea -> {
                state = state.copy(
                    drawnAreaBounds = null,
                    isDrawingMode = false
                )
                // Reset to show all properties
                setProperties(state.properties)
            }

            is MapAction.OnSubscribe -> {
                // Handle email subscription
                viewModelScope.launch {
                    eventChannel.send(MapEvent.SubscriptionSuccess)
                }
            }
        }
    }

    private fun filterMarkersByCategory(category: PropertyCategory) {
        val filteredProperties = if (category == PropertyCategory.ALL) {
            state.properties
        } else {
            state.properties.filter { property ->
                when (category) {
                    PropertyCategory.RESIDENTIAL -> property.propertyType in listOf("Apartment", "House", "Villa", "Condo")
                    PropertyCategory.COMMERCIAL -> property.propertyType in listOf("Office", "Shop", "Warehouse", "Commercial")
                    PropertyCategory.LAND -> property.propertyType in listOf("Land", "Plot", "Farm")
                    PropertyCategory.ALL -> true
                }
            }
        }

        val markers = filteredProperties.map { property ->
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
    }

    private fun filterPropertiesInArea() {
        val bounds = state.drawnAreaBounds ?: return

        val filteredProperties = state.properties.filter { property ->
            property.latitude >= bounds.south &&
            property.latitude <= bounds.north &&
            property.longitude >= bounds.west &&
            property.longitude <= bounds.east
        }

        val markers = filteredProperties.map { property ->
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
    }
}
