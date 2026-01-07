package com.zanoapps.property_details.presentation.listing_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.enums.Amenity
import com.zanoapps.core.domain.enums.CoolingType
import com.zanoapps.core.domain.enums.FurnishedType
import com.zanoapps.core.domain.enums.HeatingType
import com.zanoapps.core.domain.enums.ListingType
import com.zanoapps.core.domain.enums.ParkingType
import com.zanoapps.core.domain.enums.PetPolicy
import com.zanoapps.core.domain.enums.PropertyStatus
import com.zanoapps.core.domain.enums.PropertyType
import com.zanoapps.presentation.ui.UiText
import com.zanoapps.property_details.domain.model.PropertyImageState
import com.zanoapps.property_details.domain.model.PropertyState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ListingDetailsViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(ListingDetailsState())
        private set

    private val eventChannel = Channel<ListingDetailsEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        val propertyId = savedStateHandle.get<String>("propertyId") ?: ""
        state = state.copy(propertyId = propertyId)
        loadPropertyDetails(propertyId)
    }

    fun onAction(action: ListingDetailsAction) {
        when (action) {
            // Navigation
            ListingDetailsAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(ListingDetailsEvent.NavigateBack)
                }
            }

            // Favorite
            ListingDetailsAction.OnFavoriteToggle -> {
                val newFavoriteState = !state.isFavorite
                state = state.copy(isFavorite = newFavoriteState)
                viewModelScope.launch {
                    if (newFavoriteState) {
                        eventChannel.send(ListingDetailsEvent.AddedToFavorites)
                    } else {
                        eventChannel.send(ListingDetailsEvent.RemovedFromFavorites)
                    }
                }
            }

            // Image gallery
            is ListingDetailsAction.OnImageSelected -> {
                state = state.copy(selectedImageIndex = action.index)
            }

            ListingDetailsAction.OnOpenImageGallery -> {
                state = state.copy(isImageGalleryOpen = true)
            }

            ListingDetailsAction.OnCloseImageGallery -> {
                state = state.copy(isImageGalleryOpen = false)
            }

            ListingDetailsAction.OnNextImage -> {
                val images = state.property?.images ?: return
                val nextIndex = (state.selectedImageIndex + 1) % images.size
                state = state.copy(selectedImageIndex = nextIndex)
            }

            ListingDetailsAction.OnPreviousImage -> {
                val images = state.property?.images ?: return
                val prevIndex = if (state.selectedImageIndex == 0) {
                    images.size - 1
                } else {
                    state.selectedImageIndex - 1
                }
                state = state.copy(selectedImageIndex = prevIndex)
            }

            // Contact form
            ListingDetailsAction.OnContactAgentClick -> {
                state = state.copy(isContactFormOpen = true)
            }

            ListingDetailsAction.OnCloseContactForm -> {
                state = state.copy(isContactFormOpen = false)
            }

            is ListingDetailsAction.OnContactNameChanged -> {
                state = state.copy(contactName = action.name)
            }

            is ListingDetailsAction.OnContactEmailChanged -> {
                state = state.copy(contactEmail = action.email)
            }

            is ListingDetailsAction.OnContactPhoneChanged -> {
                state = state.copy(contactPhone = action.phone)
            }

            is ListingDetailsAction.OnContactMessageChanged -> {
                state = state.copy(contactMessage = action.message)
            }

            ListingDetailsAction.OnSubmitContactForm -> {
                submitContactForm()
            }

            // Schedule tour
            ListingDetailsAction.OnScheduleTourClick -> {
                state = state.copy(isScheduleTourOpen = true)
            }

            ListingDetailsAction.OnCloseScheduleTour -> {
                state = state.copy(isScheduleTourOpen = false)
            }

            is ListingDetailsAction.OnTourDateSelected -> {
                state = state.copy(selectedTourDate = action.date)
            }

            is ListingDetailsAction.OnTourTimeSelected -> {
                state = state.copy(selectedTourTime = action.time)
            }

            ListingDetailsAction.OnSubmitTourRequest -> {
                submitTourRequest()
            }

            // Share
            ListingDetailsAction.OnShareClick -> {
                state = state.copy(isShareDialogOpen = true)
            }

            ListingDetailsAction.OnCloseShareDialog -> {
                state = state.copy(isShareDialogOpen = false)
            }

            is ListingDetailsAction.OnShareVia -> {
                handleShare(action.platform)
            }

            // Section expansion
            is ListingDetailsAction.OnToggleSection -> {
                val currentSections = state.expandedSections.toMutableSet()
                if (currentSections.contains(action.section)) {
                    currentSections.remove(action.section)
                } else {
                    currentSections.add(action.section)
                }
                state = state.copy(expandedSections = currentSections)
            }

            // Refresh
            ListingDetailsAction.OnRefresh -> {
                loadPropertyDetails(state.propertyId, isRefresh = true)
            }

            // Map
            ListingDetailsAction.OnViewOnMapClick -> {
                val property = state.property ?: return
                val lat = property.latitude ?: return
                val lng = property.longitude ?: return
                viewModelScope.launch {
                    eventChannel.send(
                        ListingDetailsEvent.OpenMap(
                            latitude = lat,
                            longitude = lng,
                            address = property.address
                        )
                    )
                }
            }

            // Call agent
            ListingDetailsAction.OnCallAgentClick -> {
                val phoneNumber = state.property?.agentPhone ?: return
                viewModelScope.launch {
                    eventChannel.send(ListingDetailsEvent.OpenDialer(phoneNumber))
                }
            }
        }
    }

    private fun loadPropertyDetails(propertyId: String, isRefresh: Boolean = false) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = !isRefresh,
                isRefreshing = isRefresh,
                errorMessage = null
            )

            // Simulate network delay
            delay(500)

            // Load mock property data
            val property = getMockPropertyDetails(propertyId)
            state = state.copy(
                property = property,
                isLoading = false,
                isRefreshing = false
            )
        }
    }

    private fun submitContactForm() {
        viewModelScope.launch {
            state = state.copy(isSubmittingContact = true)

            // Simulate network request
            delay(1000)

            state = state.copy(
                isSubmittingContact = false,
                isContactFormOpen = false,
                contactName = "",
                contactEmail = "",
                contactPhone = "",
                contactMessage = ""
            )

            eventChannel.send(ListingDetailsEvent.ContactFormSubmitted)
        }
    }

    private fun submitTourRequest() {
        viewModelScope.launch {
            state = state.copy(isSubmittingTour = true)

            // Simulate network request
            delay(1000)

            state = state.copy(
                isSubmittingTour = false,
                isScheduleTourOpen = false,
                selectedTourDate = "",
                selectedTourTime = ""
            )

            eventChannel.send(ListingDetailsEvent.TourRequestSubmitted)
        }
    }

    private fun handleShare(platform: SharePlatform) {
        val property = state.property ?: return
        val shareUrl = "https://balkanestate.com/property/${property.id}"
        val shareTitle = property.title

        viewModelScope.launch {
            when (platform) {
                SharePlatform.COPY_LINK -> {
                    eventChannel.send(ListingDetailsEvent.LinkCopied)
                }

                else -> {
                    eventChannel.send(ListingDetailsEvent.ShareProperty(shareUrl, shareTitle))
                }
            }
            state = state.copy(isShareDialogOpen = false)
        }
    }

    // Mock data for development
    private fun getMockPropertyDetails(propertyId: String): PropertyState {
        return PropertyState(
            id = propertyId.ifEmpty { "mock-property-1" },
            title = "Luxury Modern Villa with Sea View",
            description = """
                Experience luxury living in this stunning modern villa featuring breathtaking sea views.
                This exceptional property offers an open-concept design with floor-to-ceiling windows that
                flood the space with natural light while showcasing panoramic coastal vistas.

                The gourmet kitchen is equipped with top-of-the-line appliances, custom cabinetry, and
                a large island perfect for entertaining. The master suite includes a spa-like bathroom
                with dual vanities, a soaking tub, and a walk-in shower.

                Outside, enjoy the infinity pool, landscaped gardens, and multiple terraces ideal for
                al fresco dining. Located in an exclusive neighborhood with easy access to beaches,
                restaurants, and amenities.
            """.trimIndent(),
            price = 450000.0,
            currency = "EUR",
            listingType = ListingType.SALE,
            propertyType = PropertyType.VILLA,
            propertySubType = null,
            address = "123 Coastal Drive, Bay View Heights",
            city = "Tirana",
            country = "Albania",
            postalCode = "1001",
            latitude = 41.3275,
            longitude = 19.8187,
            neighbourhood = "Bay View Heights",
            bedrooms = 4,
            bathrooms = 3,
            halfBathrooms = 1,
            squareFootage = 2500,
            lotSize = 5000,
            yearBuilt = 2022,
            floors = 2,
            parking = ParkingType.GARAGE,
            parkingSpaces = 2,
            heating = HeatingType.CENTRAL_AIR,
            cooling = CoolingType.CENTRAL,
            furnished = FurnishedType.FULLY_FURNISHED,
            petPolicy = PetPolicy.ALLOWED,
            amenities = listOf(
                Amenity.POOL,
                Amenity.GYM,
                Amenity.GARDEN,
                Amenity.BALCONY,
                Amenity.TERRACE,
                Amenity.FIREPLACE,
                Amenity.SECURITY_SYSTEM,
                Amenity.AIR_CONDITIONING,
                Amenity.HIGH_CEILINGS,
                Amenity.HARDWOOD_FLOORS
            ),
            appliances = emptyList(),
            images = listOf(
                PropertyImageState(
                    id = "img1",
                    url = "https://images.unsplash.com/photo-1613490493576-7fde63acd811?w=800",
                    caption = "Front View",
                    order = 0,
                    isMain = true
                ),
                PropertyImageState(
                    id = "img2",
                    url = "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=800",
                    caption = "Living Room",
                    order = 1,
                    isMain = false
                ),
                PropertyImageState(
                    id = "img3",
                    url = "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?w=800",
                    caption = "Kitchen",
                    order = 2,
                    isMain = false
                ),
                PropertyImageState(
                    id = "img4",
                    url = "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=800",
                    caption = "Master Bedroom",
                    order = 3,
                    isMain = false
                ),
                PropertyImageState(
                    id = "img5",
                    url = "https://images.unsplash.com/photo-1584622650111-993a426fbf0a?w=800",
                    caption = "Bathroom",
                    order = 4,
                    isMain = false
                ),
                PropertyImageState(
                    id = "img6",
                    url = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=800",
                    caption = "Pool Area",
                    order = 5,
                    isMain = false
                )
            ),
            virtualTour = "https://tour.balkanestate.com/property/mock-1",
            floorPlan = "https://plans.balkanestate.com/property/mock-1",
            monthlyRent = null,
            securityDeposit = null,
            utilitiesIncluded = false,
            hoa = 200.0,
            propertyTax = 1500.0,
            insurance = 800.0,
            agentId = "agent-1",
            agentName = "Besmir Kola",
            agentPhone = "+355 69 123 4567",
            agentEmail = "besmir.kola@balkanestate.com",
            status = PropertyStatus.ACTIVE,
            availableFrom = LocalDateTime.now(),
            leaseLength = null,
            createdAt = LocalDateTime.now().minusDays(7),
            updatedAt = LocalDateTime.now(),
            featured = true,
            urgent = false,
            views = 1234,
            favorites = 56
        )
    }
}
