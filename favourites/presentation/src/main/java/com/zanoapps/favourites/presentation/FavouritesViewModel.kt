package com.zanoapps.favourites.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.model.BalkanEstateProperty
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FavouritesViewModel : ViewModel() {

    var state by mutableStateOf(FavouritesState())
        private set

    private val eventChannel = Channel<FavouritesEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadFavourites()
    }

    fun onAction(action: FavouritesAction) {
        when (action) {
            FavouritesAction.LoadFavourites -> loadFavourites()
            FavouritesAction.RefreshFavourites -> refreshFavourites()
            is FavouritesAction.OnPropertyClick -> {
                state = state.copy(selectedProperty = action.property)
            }
            is FavouritesAction.OnRemoveFavourite -> removeFavourite(action.propertyId)
            FavouritesAction.OnToggleViewMode -> {
                state = state.copy(isGridView = !state.isGridView)
            }
            FavouritesAction.OnClearAll -> clearAllFavourites()
            is FavouritesAction.OnViewDetails -> {
                viewModelScope.launch {
                    eventChannel.send(FavouritesEvent.NavigateToPropertyDetails(action.property.id))
                }
            }
        }
    }

    private fun loadFavourites() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            // Load mock favourites
            val favourites = getMockFavourites()

            state = state.copy(
                favouriteProperties = favourites,
                isLoading = false,
                isEmpty = favourites.isEmpty()
            )
        }
    }

    private fun refreshFavourites() {
        viewModelScope.launch {
            state = state.copy(isRefreshing = true)
            val favourites = getMockFavourites()
            state = state.copy(
                favouriteProperties = favourites,
                isRefreshing = false,
                isEmpty = favourites.isEmpty()
            )
        }
    }

    private fun removeFavourite(propertyId: String) {
        viewModelScope.launch {
            val updatedList = state.favouriteProperties.filter { it.id != propertyId }
            state = state.copy(
                favouriteProperties = updatedList,
                isEmpty = updatedList.isEmpty()
            )
            eventChannel.send(FavouritesEvent.FavouriteRemoved)
        }
    }

    private fun clearAllFavourites() {
        viewModelScope.launch {
            state = state.copy(
                favouriteProperties = emptyList(),
                isEmpty = true
            )
            eventChannel.send(FavouritesEvent.AllFavouritesCleared)
        }
    }

    private fun getMockFavourites(): List<BalkanEstateProperty> {
        return listOf(
            BalkanEstateProperty(
                id = "fav1",
                title = "Luxury Penthouse with Sea View",
                price = 450000.0,
                currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1512917774080-9991f1c4c750",
                bedrooms = 4,
                bathrooms = 3,
                squareFootage = 250,
                address = "Waterfront Boulevard 15",
                city = "Durrës",
                country = "Albania",
                latitude = 41.3246,
                longitude = 19.4565,
                propertyType = "Penthouse",
                listingType = "Sale",
                agentName = "Marina Lleshi",
                isFeatured = true,
                isUrgent = false
            ),
            BalkanEstateProperty(
                id = "fav2",
                title = "Modern Studio in Business District",
                price = 85000.0,
                currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688",
                bedrooms = 1,
                bathrooms = 1,
                squareFootage = 45,
                address = "Business Center Street 8",
                city = "Tirana",
                country = "Albania",
                latitude = 41.3275,
                longitude = 19.8187,
                propertyType = "Studio",
                listingType = "Sale",
                agentName = "Andi Basha",
                isFeatured = false,
                isUrgent = true
            ),
            BalkanEstateProperty(
                id = "fav3",
                title = "Charming Villa with Garden",
                price = 320000.0,
                currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914",
                bedrooms = 5,
                bathrooms = 4,
                squareFootage = 350,
                address = "Green Hills Road 42",
                city = "Tirana",
                country = "Albania",
                latitude = 41.3300,
                longitude = 19.8220,
                propertyType = "Villa",
                listingType = "Sale",
                agentName = "Elira Hoxha",
                isFeatured = true,
                isUrgent = false
            )
        )
    }
}
