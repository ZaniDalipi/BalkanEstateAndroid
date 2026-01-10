package com.zanoapps.property_details.presentation.details

import com.zanoapps.core.domain.model.BalkanEstateProperty

sealed interface PropertyDetailsAction {
    data class LoadProperty(val propertyId: String) : PropertyDetailsAction
    data object OnBackClick : PropertyDetailsAction
    data object OnFavoriteToggle : PropertyDetailsAction
    data object OnShareClick : PropertyDetailsAction
    data object OnContactAgentClick : PropertyDetailsAction
    data object OnDismissContactDialog : PropertyDetailsAction
    data object OnDismissShareDialog : PropertyDetailsAction
    data class OnCallAgent(val phoneNumber: String) : PropertyDetailsAction
    data class OnEmailAgent(val email: String) : PropertyDetailsAction
    data class OnWhatsAppAgent(val phoneNumber: String) : PropertyDetailsAction
    data class OnImageClick(val index: Int) : PropertyDetailsAction
    data object OnNextImage : PropertyDetailsAction
    data object OnPreviousImage : PropertyDetailsAction
    data object OnToggleImageFullscreen : PropertyDetailsAction
    data class OnSimilarPropertyClick(val property: BalkanEstateProperty) : PropertyDetailsAction
    data object OnScheduleVisitClick : PropertyDetailsAction
    data object OnViewOnMapClick : PropertyDetailsAction
}
