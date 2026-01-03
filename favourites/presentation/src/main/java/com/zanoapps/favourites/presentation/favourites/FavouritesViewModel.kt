package com.zanoapps.favourites.presentation.favourites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.util.Result
import com.zanoapps.favourites.domain.repository.FavouritesRepository
import com.zanoapps.favourites.presentation.R
import com.zanoapps.presentation.ui.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {

    var state by mutableStateOf(FavouritesState())
        private set

    private val eventChannel = Channel<FavouritesEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadFavourites()
    }

    fun onAction(action: FavouritesAction) {
        when (action) {
            FavouritesAction.OnBackClick -> Unit // Handled by navigation
            is FavouritesAction.OnPropertyClick -> Unit // Handled by navigation
            is FavouritesAction.OnRemoveFromFavourites -> removeFromFavourites(action.propertyId)
            FavouritesAction.OnRefresh -> refresh()
        }
    }

    private fun loadFavourites() {
        state = state.copy(isLoading = true)
        favouritesRepository.getFavouriteProperties()
            .onEach { properties ->
                state = state.copy(
                    properties = properties,
                    isLoading = false,
                    isEmpty = properties.isEmpty()
                )
            }
            .launchIn(viewModelScope)
    }

    private fun refresh() {
        state = state.copy(isRefreshing = true)
        viewModelScope.launch {
            // Simulate refresh - in real app would fetch from server
            kotlinx.coroutines.delay(1000)
            state = state.copy(isRefreshing = false)
        }
    }

    private fun removeFromFavourites(propertyId: String) {
        viewModelScope.launch {
            when (val result = favouritesRepository.removeFromFavourites(propertyId)) {
                is Result.Success -> {
                    eventChannel.send(FavouritesEvent.PropertyRemoved)
                }
                is Result.Error -> {
                    eventChannel.send(
                        FavouritesEvent.Error(UiText.StringResource(R.string.error_removing_favourite))
                    )
                }
            }
        }
    }
}
