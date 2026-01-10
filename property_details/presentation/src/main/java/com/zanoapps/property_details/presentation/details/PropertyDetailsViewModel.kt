package com.zanoapps.property_details.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.model.BalkanEstateProperty
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PropertyDetailsViewModel : ViewModel() {

    var state by mutableStateOf(PropertyDetailsState())
        private set

    private val eventChannel = Channel<PropertyDetailsEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: PropertyDetailsAction) {
        when (action) {
            is PropertyDetailsAction.LoadProperty -> loadProperty(action.propertyId)
            PropertyDetailsAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(PropertyDetailsEvent.NavigateBack)
                }
            }
            PropertyDetailsAction.OnFavoriteToggle -> {
                state = state.copy(isFavorite = !state.isFavorite)
            }
            PropertyDetailsAction.OnShareClick -> {
                state = state.copy(isShareDialogVisible = true)
            }
            PropertyDetailsAction.OnContactAgentClick -> {
                state = state.copy(isContactAgentDialogVisible = true)
            }
            PropertyDetailsAction.OnDismissContactDialog -> {
                state = state.copy(isContactAgentDialogVisible = false)
            }
            PropertyDetailsAction.OnDismissShareDialog -> {
                state = state.copy(isShareDialogVisible = false)
            }
            is PropertyDetailsAction.OnCallAgent -> {
                viewModelScope.launch {
                    eventChannel.send(PropertyDetailsEvent.OpenDialer(action.phoneNumber))
                }
            }
            is PropertyDetailsAction.OnEmailAgent -> {
                viewModelScope.launch {
                    val subject = "Inquiry about: ${state.property?.title ?: "Property"}"
                    eventChannel.send(PropertyDetailsEvent.OpenEmail(action.email, subject))
                }
            }
            is PropertyDetailsAction.OnWhatsAppAgent -> {
                viewModelScope.launch {
                    val message = "Hi, I'm interested in the property: ${state.property?.title ?: ""}"
                    eventChannel.send(PropertyDetailsEvent.OpenWhatsApp(action.phoneNumber, message))
                }
            }
            is PropertyDetailsAction.OnImageClick -> {
                state = state.copy(currentImageIndex = action.index)
            }
            PropertyDetailsAction.OnNextImage -> {
                val maxIndex = (state.property?.let { 5 } ?: 1) - 1
                val nextIndex = if (state.currentImageIndex < maxIndex) state.currentImageIndex + 1 else 0
                state = state.copy(currentImageIndex = nextIndex)
            }
            PropertyDetailsAction.OnPreviousImage -> {
                val maxIndex = (state.property?.let { 5 } ?: 1) - 1
                val prevIndex = if (state.currentImageIndex > 0) state.currentImageIndex - 1 else maxIndex
                state = state.copy(currentImageIndex = prevIndex)
            }
            PropertyDetailsAction.OnToggleImageFullscreen -> {
                state = state.copy(isImageFullscreen = !state.isImageFullscreen)
            }
            is PropertyDetailsAction.OnSimilarPropertyClick -> {
                viewModelScope.launch {
                    eventChannel.send(PropertyDetailsEvent.NavigateToProperty(action.property.id))
                }
            }
            PropertyDetailsAction.OnScheduleVisitClick -> {
                state = state.copy(isContactAgentDialogVisible = true)
            }
            PropertyDetailsAction.OnViewOnMapClick -> {
                state.property?.let { property ->
                    viewModelScope.launch {
                        eventChannel.send(
                            PropertyDetailsEvent.OpenMap(
                                latitude = property.latitude,
                                longitude = property.longitude,
                                address = property.address
                            )
                        )
                    }
                }
            }
        }
    }

    private fun loadProperty(propertyId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            // Mock property data for now
            val mockProperty = getMockPropertyById(propertyId)
            val similarProperties = getMockSimilarProperties()

            state = state.copy(
                property = mockProperty,
                similarProperties = similarProperties,
                isLoading = false
            )
        }
    }

    private fun getMockPropertyById(propertyId: String): BalkanEstateProperty {
        return BalkanEstateProperty(
            id = propertyId,
            title = "Modern 3BR Apartment in City Center",
            price = 185000.0,
            currency = "EUR",
            imageUrl = "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2",
            bedrooms = 3,
            bathrooms = 2,
            squareFootage = 120,
            address = "123 Main Street, Downtown",
            city = "Tirana",
            country = "Albania",
            latitude = 41.3275,
            longitude = 19.8187,
            propertyType = "Apartment",
            listingType = "Sale",
            agentName = "Besmir Kola",
            isFeatured = true,
            isUrgent = false
        )
    }

    private fun getMockSimilarProperties(): List<BalkanEstateProperty> {
        return listOf(
            BalkanEstateProperty(
                id = "similar1",
                title = "Cozy 2BR Near Park",
                price = 145000.0,
                currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688",
                bedrooms = 2,
                bathrooms = 1,
                squareFootage = 85,
                address = "456 Park Avenue",
                city = "Tirana",
                country = "Albania",
                latitude = 41.3290,
                longitude = 19.8200,
                propertyType = "Apartment",
                listingType = "Sale",
                agentName = "Ana Hoxha",
                isFeatured = false,
                isUrgent = false
            ),
            BalkanEstateProperty(
                id = "similar2",
                title = "Spacious 4BR Family Home",
                price = 275000.0,
                currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1512917774080-9991f1c4c750",
                bedrooms = 4,
                bathrooms = 3,
                squareFootage = 200,
                address = "789 Family Lane",
                city = "Tirana",
                country = "Albania",
                latitude = 41.3250,
                longitude = 19.8150,
                propertyType = "House",
                listingType = "Sale",
                agentName = "Arben Dedja",
                isFeatured = true,
                isUrgent = false
            )
        )
    }
}
