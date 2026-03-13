package com.zanoapps.search.presentation.saved

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SavedSearchesViewModel : ViewModel() {

    var state by mutableStateOf(SavedSearchesState())
        private set

    private val eventChannel = Channel<SavedSearchesEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadSavedSearches()
    }

    fun onAction(action: SavedSearchesAction) {
        when (action) {
            SavedSearchesAction.OnLoadSavedSearches -> loadSavedSearches()
            is SavedSearchesAction.OnDeleteSearch -> deleteSearch(action.searchId)
            is SavedSearchesAction.OnToggleNotifications -> toggleNotifications(action.searchId)
            is SavedSearchesAction.OnSearchClick -> {
                viewModelScope.launch {
                    eventChannel.send(SavedSearchesEvent.NavigateToSearch(action.search.id))
                }
            }
            is SavedSearchesAction.OnEditSearch -> {}
        }
    }

    private fun loadSavedSearches() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            state = state.copy(
                savedSearches = getMockSavedSearches(),
                isLoading = false
            )
        }
    }

    private fun deleteSearch(searchId: String) {
        val updated = state.savedSearches.filter { it.id != searchId }
        state = state.copy(savedSearches = updated)
        viewModelScope.launch {
            eventChannel.send(SavedSearchesEvent.SearchDeleted(searchId))
        }
    }

    private fun toggleNotifications(searchId: String) {
        val updated = state.savedSearches.map {
            if (it.id == searchId) it.copy(notificationsEnabled = !it.notificationsEnabled) else it
        }
        state = state.copy(savedSearches = updated)
    }

    private fun getMockSavedSearches(): List<SavedSearchItem> = listOf(
        SavedSearchItem("s1", "Tirana Apartments", query = "apartment", location = "Tirana", propertyType = "Apartment", priceRange = "€50,000 - €200,000", bedrooms = "2+", notificationsEnabled = true, matchCount = 24, newCount = 3, createdAt = "2 days ago"),
        SavedSearchItem("s2", "Coastal Villas", query = "villa", location = "Durrës, Vlorë", propertyType = "Villa", priceRange = "€200,000 - €500,000", bedrooms = "3+", notificationsEnabled = true, matchCount = 12, newCount = 1, createdAt = "1 week ago"),
        SavedSearchItem("s3", "Commercial Tirana", query = "office", location = "Tirana", propertyType = "Commercial", priceRange = "€1,000 - €5,000/mo", bedrooms = "N/A", notificationsEnabled = false, matchCount = 8, newCount = 0, createdAt = "2 weeks ago"),
        SavedSearchItem("s4", "Student Rentals", query = "studio", location = "Tirana", propertyType = "Studio", priceRange = "€200 - €500/mo", bedrooms = "1", notificationsEnabled = true, matchCount = 15, newCount = 5, createdAt = "3 weeks ago")
    )
}
