package com.zanoapps.map.presentation.map

import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.map.domain.model.PropertyMarker

sealed interface MapAction {
    data object OnBackClick : MapAction
    data class OnMarkerClick(val propertyId: String) : MapAction
    data class OnPropertySelected(val property: BalkanEstateProperty) : MapAction
    data object OnDismissPropertyCard : MapAction
    data class OnCameraMove(val latitude: Double, val longitude: Double, val zoom: Float) : MapAction
    data object OnMyLocationClick : MapAction
    data object OnZoomIn : MapAction
    data object OnZoomOut : MapAction
    data object OnMapTypeToggle : MapAction
}
