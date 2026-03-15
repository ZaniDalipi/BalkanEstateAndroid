package com.zanoapps.map.presentation.map

import com.zanoapps.map.domain.model.MapRegion

sealed interface MapAction {
    data object OnLoadProperties : MapAction
    data class OnRegionChanged(val region: MapRegion) : MapAction
    data class OnPropertySelected(val propertyId: String) : MapAction
    data object OnDismissPropertyCard : MapAction
}
