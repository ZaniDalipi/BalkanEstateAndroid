package com.zanoapps.property_details.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.enums.Amenity
import com.zanoapps.property_details.domain.model.PropertyImageState
import com.zanoapps.property_details.domain.model.PropertyMedia
import com.zanoapps.property_details.domain.model.PropertyVideoState
import com.zanoapps.property_details.domain.model.VideoSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Property Details screen
 */
class PropertyDetailsViewModel : ViewModel() {

    var state by mutableStateOf(PropertyDetailsState())
        private set

    private val _events = Channel<PropertyDetailsEvent>()
    val events = _events.receiveAsFlow()

    /**
     * Loads property details by ID
     * In a real app, this would call a repository/use case
     */
    fun loadProperty(propertyId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            // Simulate network delay
            delay(500)

            // For demo purposes, using mock data
            // In production, fetch from repository
            val mockProperty = getMockProperty(propertyId)
            val mockMedia = getMockMedia(propertyId)

            state = state.copy(
                property = mockProperty,
                media = mockMedia,
                isLoading = false
            )
        }
    }

    fun onAction(action: PropertyDetailsAction) {
        when (action) {
            is PropertyDetailsAction.OnFavoriteClick -> {
                state = state.copy(isFavorite = !state.isFavorite)
            }
            is PropertyDetailsAction.OnShareClick -> {
                viewModelScope.launch {
                    state.property?.let {
                        _events.send(PropertyDetailsEvent.ShareProperty("https://balkanestate.com/property/${it.id}"))
                    }
                }
            }
            is PropertyDetailsAction.OnCallAgentClick -> {
                viewModelScope.launch {
                    state.property?.let {
                        _events.send(PropertyDetailsEvent.CallAgent(it.agentPhone))
                    }
                }
            }
            is PropertyDetailsAction.OnEmailAgentClick -> {
                viewModelScope.launch {
                    state.property?.let {
                        _events.send(PropertyDetailsEvent.EmailAgent(it.agentEmail))
                    }
                }
            }
            is PropertyDetailsAction.OnContactAgentClick -> {
                viewModelScope.launch {
                    _events.send(PropertyDetailsEvent.OpenContactDialog)
                }
            }
            is PropertyDetailsAction.OnScheduleViewingClick -> {
                viewModelScope.launch {
                    _events.send(PropertyDetailsEvent.OpenSchedulingDialog)
                }
            }
            is PropertyDetailsAction.OnMediaClick -> {
                // Handle fullscreen media view
            }
            else -> {}
        }
    }

    /**
     * Mock data for demo purposes
     * Replace with actual repository call in production
     */
    private fun getMockProperty(id: String): PropertyDetailsUiModel {
        return PropertyDetailsUiModel(
            id = id,
            title = "Stunning Modern Villa with Panoramic Sea Views",
            description = """
                Experience luxury living at its finest in this breathtaking modern villa featuring panoramic sea views.

                This exceptional property showcases contemporary architecture with floor-to-ceiling windows that flood the interior with natural light while capturing stunning vistas of the Adriatic Sea.

                The open-concept living space flows seamlessly onto a spacious terrace, perfect for entertaining or simply relaxing while watching the sunset. The gourmet kitchen features top-of-the-line appliances, custom cabinetry, and a large island.

                The master suite offers a private balcony, walk-in closet, and an en-suite bathroom with a soaking tub overlooking the water. Three additional bedrooms provide ample space for family or guests.

                Additional features include smart home technology, underfloor heating, a private pool, and landscaped gardens. Located in a prestigious neighborhood with easy access to beaches, restaurants, and the city center.
            """.trimIndent(),
            price = 485000.0,
            currency = "EUR",
            listingType = "Sale",
            propertyType = "Villa",
            address = "123 Coastal Boulevard",
            city = "Durrës",
            country = "Albania",
            bedrooms = 4,
            bathrooms = 3,
            squareFootage = 320,
            parkingSpaces = 2,
            yearBuilt = 2022,
            lotSize = 850,
            agentName = "Elena Kosta",
            agentPhone = "+355 69 234 5678",
            agentEmail = "elena.kosta@balkanestate.com",
            amenities = listOf(
                Amenity.SWIMMING_POOL,
                Amenity.AIR_CONDITIONING,
                Amenity.SECURITY_SYSTEM,
                Amenity.GARDEN,
                Amenity.BALCONY,
                Amenity.GYM,
                Amenity.STORAGE
            )
        )
    }

    /**
     * Mock media for demo purposes
     * Replace with actual repository call in production
     */
    private fun getMockMedia(id: String): PropertyMedia {
        return PropertyMedia(
            videos = listOf(
                PropertyVideoState(
                    id = "video_1",
                    url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    thumbnailUrl = "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=800",
                    caption = "Property Tour",
                    isMain = true,
                    order = 0,
                    videoSource = VideoSource.DIRECT
                )
            ),
            images = listOf(
                PropertyImageState(
                    id = "img_1",
                    url = "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=1200",
                    caption = "Front View",
                    order = 0,
                    isMain = true
                ),
                PropertyImageState(
                    id = "img_2",
                    url = "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?w=1200",
                    caption = "Living Room",
                    order = 1
                ),
                PropertyImageState(
                    id = "img_3",
                    url = "https://images.unsplash.com/photo-1600566753190-17f0baa2a6c3?w=1200",
                    caption = "Kitchen",
                    order = 2
                ),
                PropertyImageState(
                    id = "img_4",
                    url = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=1200",
                    caption = "Master Bedroom",
                    order = 3
                ),
                PropertyImageState(
                    id = "img_5",
                    url = "https://images.unsplash.com/photo-1600573472592-401b489a3cdc?w=1200",
                    caption = "Pool Area",
                    order = 4
                ),
                PropertyImageState(
                    id = "img_6",
                    url = "https://images.unsplash.com/photo-1600047509807-ba8f99d2cdde?w=1200",
                    caption = "Garden",
                    order = 5
                )
            )
        )
    }
}
