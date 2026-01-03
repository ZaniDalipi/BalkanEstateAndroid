package com.zanoapps.property_details.presentation.property_detail

sealed interface PropertyDetailAction {
    data object OnBackClick : PropertyDetailAction
    data object OnShareClick : PropertyDetailAction
    data object OnFavoriteClick : PropertyDetailAction
    data object OnContactAgentClick : PropertyDetailAction
    data object OnScheduleTourClick : PropertyDetailAction
    data object OnViewVirtualTourClick : PropertyDetailAction
    data object OnViewMapClick : PropertyDetailAction
    data class OnImageClick(val index: Int) : PropertyDetailAction
    data object OnDismissImageViewer : PropertyDetailAction
    data object OnNextImage : PropertyDetailAction
    data object OnPreviousImage : PropertyDetailAction
}
