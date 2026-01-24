package com.zanoapps.property_details.presentation

import com.zanoapps.core.domain.enums.Amenity
import com.zanoapps.property_details.domain.model.PropertyMedia

/**
 * UI State for the Property Details screen
 */
data class PropertyDetailsState(
    val property: PropertyDetailsUiModel? = null,
    val media: PropertyMedia = PropertyMedia(),
    val isFavorite: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

/**
 * UI Model representing property details for display
 */
data class PropertyDetailsUiModel(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val currency: String,
    val listingType: String,
    val propertyType: String,
    val address: String,
    val city: String,
    val country: String,
    val bedrooms: Int,
    val bathrooms: Int,
    val squareFootage: Int,
    val parkingSpaces: Int = 0,
    val yearBuilt: Int? = null,
    val lotSize: Int? = null,
    val agentName: String,
    val agentPhone: String,
    val agentEmail: String,
    val amenities: List<Amenity> = emptyList()
)

/**
 * Actions that can be triggered from the Property Details screen
 */
sealed interface PropertyDetailsAction {
    data object OnBackClick : PropertyDetailsAction
    data object OnFavoriteClick : PropertyDetailsAction
    data object OnShareClick : PropertyDetailsAction
    data object OnCallAgentClick : PropertyDetailsAction
    data object OnEmailAgentClick : PropertyDetailsAction
    data object OnContactAgentClick : PropertyDetailsAction
    data object OnScheduleViewingClick : PropertyDetailsAction
    data class OnMediaClick(val index: Int) : PropertyDetailsAction
}

/**
 * One-time events from the Property Details screen
 */
sealed interface PropertyDetailsEvent {
    data class ShowError(val message: String) : PropertyDetailsEvent
    data class CallAgent(val phoneNumber: String) : PropertyDetailsEvent
    data class EmailAgent(val email: String) : PropertyDetailsEvent
    data class ShareProperty(val url: String) : PropertyDetailsEvent
    data object OpenSchedulingDialog : PropertyDetailsEvent
    data object OpenContactDialog : PropertyDetailsEvent
}
