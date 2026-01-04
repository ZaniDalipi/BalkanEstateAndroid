package com.zanoapps.map.presentation.map

import com.zanoapps.core.domain.model.BalkanEstateProperty

sealed interface MapAction {
    data object OnBackClick : MapAction
    data class OnMarkerClick(val propertyId: String) : MapAction
    data class OnPropertySelected(val property: BalkanEstateProperty) : MapAction
    data object OnDismissPropertyCard : MapAction
    data class OnCameraMove(val latitude: Double, val longitude: Double, val zoom: Float) : MapAction
    data object OnMyLocationClick : MapAction
    data object OnZoomIn : MapAction
    data object OnZoomOut : MapAction
    data object OnToggleMapLayer : MapAction
    data object OnToggleDrawingMode : MapAction
    data object OnToggle3DBuildings : MapAction
    data class OnCategorySelected(val category: PropertyCategory) : MapAction
    data class OnViewModeToggle(val isListView: Boolean) : MapAction
    data object OnOpenDrawer : MapAction
    data object OnCloseDrawer : MapAction
    data class OnDrawerItemClick(val item: String) : MapAction
    data object OnFilterClick : MapAction
    data object OnProfileClick : MapAction
    data class OnSearchQueryChanged(val query: String) : MapAction
    data class OnAreaDrawn(val south: Double, val west: Double, val north: Double, val east: Double) : MapAction
    data object OnClearDrawnArea : MapAction
    data class OnSubscribe(val email: String) : MapAction
}
