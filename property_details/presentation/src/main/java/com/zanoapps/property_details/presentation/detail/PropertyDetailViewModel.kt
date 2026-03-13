package com.zanoapps.property_details.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.search.domain.model.MockData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PropertyDetailViewModel : ViewModel() {

    var state by mutableStateOf(PropertyDetailState())
        private set

    private val eventChannel = Channel<PropertyDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: PropertyDetailAction) {
        when (action) {
            is PropertyDetailAction.OnLoadProperty -> loadProperty(action.propertyId)
            PropertyDetailAction.OnToggleFavorite -> {
                state = state.copy(isFavorite = !state.isFavorite)
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
            // Load from mock data for now
            val property = MockData.getMockProperties().find { it.id == propertyId }
            val similarProperties = MockData.getMockProperties()
                .filter { it.id != propertyId }
                .take(3)

            state = state.copy(
                property = property,
                similarProperties = similarProperties,
                isLoading = false
            )
        }
    }

    private fun sendMessage() {
        viewModelScope.launch {
            state = state.copy(isSendingMessage = true)
            delay(1500) // Simulate network call
            state = state.copy(
                isSendingMessage = false,
                messageSent = true,
                isContactAgentSheetOpen = false
            )
            eventChannel.send(PropertyDetailEvent.MessageSentSuccess)
        }
    }
}
