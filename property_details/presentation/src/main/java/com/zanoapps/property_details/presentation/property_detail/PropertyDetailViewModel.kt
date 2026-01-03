package com.zanoapps.property_details.presentation.property_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.model.BalkanEstateProperty
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PropertyDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(PropertyDetailState())
        private set

    private val eventChannel = Channel<PropertyDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        // In a real app, we'd fetch the property details from the repository
        // For now, the property is passed via navigation arguments
    }

    fun setProperty(property: BalkanEstateProperty) {
        state = state.copy(property = property)
    }

    fun onAction(action: PropertyDetailAction) {
        when (action) {
            PropertyDetailAction.OnBackClick -> Unit // Handled by navigation
            PropertyDetailAction.OnShareClick -> shareProperty()
            PropertyDetailAction.OnFavoriteClick -> toggleFavorite()
            PropertyDetailAction.OnContactAgentClick -> {
                state = state.copy(isContactFormOpen = true)
            }
            PropertyDetailAction.OnScheduleTourClick -> {
                // TODO: Implement tour scheduling
            }
            PropertyDetailAction.OnViewVirtualTourClick -> {
                // TODO: Open virtual tour
            }
            PropertyDetailAction.OnViewMapClick -> {
                // TODO: Open map view
            }
            is PropertyDetailAction.OnImageClick -> {
                state = state.copy(
                    currentImageIndex = action.index,
                    isImageViewerOpen = true
                )
            }
            PropertyDetailAction.OnDismissImageViewer -> {
                state = state.copy(isImageViewerOpen = false)
            }
            PropertyDetailAction.OnNextImage -> {
                // TODO: Navigate to next image
            }
            PropertyDetailAction.OnPreviousImage -> {
                // TODO: Navigate to previous image
            }
        }
    }

    private fun toggleFavorite() {
        viewModelScope.launch {
            val newFavoriteState = !state.isFavorite
            state = state.copy(isFavorite = newFavoriteState)

            if (newFavoriteState) {
                eventChannel.send(PropertyDetailEvent.PropertyAddedToFavorites)
            } else {
                eventChannel.send(PropertyDetailEvent.PropertyRemovedFromFavorites)
            }
        }
    }

    private fun shareProperty() {
        viewModelScope.launch {
            // TODO: Implement actual sharing functionality
            eventChannel.send(PropertyDetailEvent.PropertyShared)
        }
    }
}
