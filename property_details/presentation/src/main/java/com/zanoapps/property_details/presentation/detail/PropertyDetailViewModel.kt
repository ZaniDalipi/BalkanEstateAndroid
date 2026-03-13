package com.zanoapps.property_details.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.search.domain.repository.PropertyRepository
import com.zanoapps.favourites.domain.repository.FavouritesRepository
import com.zanoapps.core.domain.util.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PropertyDetailViewModel(
    private val propertyRepository: PropertyRepository,
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {

    var state by mutableStateOf(PropertyDetailState())
        private set

    private val eventChannel = Channel<PropertyDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: PropertyDetailAction) {
        when (action) {
            is PropertyDetailAction.OnLoadProperty -> loadProperty(action.propertyId)
            PropertyDetailAction.OnToggleFavorite -> {
                val propertyId = state.property?.id ?: return
                viewModelScope.launch {
                    if (state.isFavorite) {
                        favouritesRepository.removeFavourite(propertyId)
                    } else {
                        favouritesRepository.addFavourite(propertyId)
                    }
                    state = state.copy(isFavorite = !state.isFavorite)
                }
            }
            PropertyDetailAction.OnShareProperty -> {
                state.property?.let { property ->
                    viewModelScope.launch {
                        eventChannel.send(
                            PropertyDetailEvent.ShareProperty(
                                "https://balkanestateai.com/property/${property.id}"
                            )
                        )
                    }
                }
            }
            PropertyDetailAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(PropertyDetailEvent.NavigateBack)
                }
            }
            is PropertyDetailAction.OnImageSelected -> {
                state = state.copy(selectedImageIndex = action.index)
            }
            PropertyDetailAction.OnContactAgentClick -> {
                state = state.copy(isContactAgentSheetOpen = true)
            }
            PropertyDetailAction.OnDismissContactSheet -> {
                state = state.copy(isContactAgentSheetOpen = false)
            }
            PropertyDetailAction.OnToggleShowAllAmenities -> {
                state = state.copy(showAllAmenities = !state.showAllAmenities)
            }
            is PropertyDetailAction.OnContactNameChanged -> {
                state = state.copy(contactName = action.name)
            }
            is PropertyDetailAction.OnContactEmailChanged -> {
                state = state.copy(contactEmail = action.email)
            }
            is PropertyDetailAction.OnContactPhoneChanged -> {
                state = state.copy(contactPhone = action.phone)
            }
            is PropertyDetailAction.OnContactMessageChanged -> {
                state = state.copy(contactMessage = action.message)
            }
            PropertyDetailAction.OnSendMessage -> sendMessage()
            is PropertyDetailAction.OnSimilarPropertyClick -> {
                viewModelScope.launch {
                    eventChannel.send(
                        PropertyDetailEvent.NavigateToPropertyDetail(action.property.id)
                    )
                }
            }
            PropertyDetailAction.OnScheduleTourClick -> {
                // Schedule tour logic
            }
            PropertyDetailAction.OnVirtualTourClick -> {
                // Virtual tour logic
            }
            PropertyDetailAction.OnGetDirectionsClick -> {
                state.property?.let { property ->
                    viewModelScope.launch {
                        eventChannel.send(
                            PropertyDetailEvent.OpenDirections(property.latitude, property.longitude)
                        )
                    }
                }
            }
            is PropertyDetailAction.OnCallAgent -> {
                viewModelScope.launch {
                    eventChannel.send(PropertyDetailEvent.OpenDialer(action.phoneNumber))
                }
            }
            is PropertyDetailAction.OnEmailAgent -> {
                viewModelScope.launch {
                    eventChannel.send(PropertyDetailEvent.OpenEmail(action.email))
                }
            }
        }
    }

    private fun loadProperty(propertyId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = propertyRepository.getPropertyById(propertyId)
            val isFav = favouritesRepository.isFavourite(propertyId)
            when (result) {
                is Result.Success -> {
                    val property = result.data
                    val similarResult = propertyRepository.searchProperties(
                        property.city,
                        com.zanoapps.search.domain.model.SearchFilters()
                    )
                    val similar = when (similarResult) {
                        is Result.Success -> similarResult.data.filter { it.id != propertyId }.take(3)
                        is Result.Error -> emptyList()
                    }
                    state = state.copy(
                        property = property,
                        similarProperties = similar,
                        isFavorite = isFav,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        isLoading = false,
                        errorMessage = "Failed to load property"
                    )
                }
            }
        }
    }

    private fun sendMessage() {
        viewModelScope.launch {
            state = state.copy(isSendingMessage = true)
            state = state.copy(
                isSendingMessage = false,
                messageSent = true,
                isContactAgentSheetOpen = false
            )
            eventChannel.send(PropertyDetailEvent.MessageSentSuccess)
        }
    }
}
