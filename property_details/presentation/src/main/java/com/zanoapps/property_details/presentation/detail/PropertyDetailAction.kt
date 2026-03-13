package com.zanoapps.property_details.presentation.detail

sealed interface PropertyDetailAction {
    data class OnLoadProperty(val propertyId: String) : PropertyDetailAction
    data object OnToggleFavorite : PropertyDetailAction
    data object OnShareProperty : PropertyDetailAction
    data object OnBackClick : PropertyDetailAction
    data class OnImageSelected(val index: Int) : PropertyDetailAction
    data object OnContactAgentClick : PropertyDetailAction
    data object OnDismissContactSheet : PropertyDetailAction
    data object OnToggleShowAllAmenities : PropertyDetailAction
    data class OnContactNameChanged(val name: String) : PropertyDetailAction
    data class OnContactEmailChanged(val email: String) : PropertyDetailAction
    data class OnContactPhoneChanged(val phone: String) : PropertyDetailAction
    data class OnContactMessageChanged(val message: String) : PropertyDetailAction
    data object OnSendMessage : PropertyDetailAction
    data class OnSimilarPropertyClick(val property: com.zanoapps.core.domain.model.BalkanEstateProperty) : PropertyDetailAction
    data object OnScheduleTourClick : PropertyDetailAction
    data object OnVirtualTourClick : PropertyDetailAction
    data object OnGetDirectionsClick : PropertyDetailAction
    data class OnCallAgent(val phoneNumber: String) : PropertyDetailAction
    data class OnEmailAgent(val email: String) : PropertyDetailAction
}
