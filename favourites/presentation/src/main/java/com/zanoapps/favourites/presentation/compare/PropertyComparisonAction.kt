package com.zanoapps.favourites.presentation.compare

sealed interface PropertyComparisonAction {
    data object OnBackClick : PropertyComparisonAction
    data class OnViewDetailsClick(val propertyId: String) : PropertyComparisonAction
}
