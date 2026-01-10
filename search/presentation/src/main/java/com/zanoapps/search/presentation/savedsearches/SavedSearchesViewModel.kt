package com.zanoapps.search.presentation.savedsearches

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
            SavedSearchesAction.LoadSavedSearches -> loadSavedSearches()
            SavedSearchesAction.RefreshSavedSearches -> refreshSavedSearches()
            is SavedSearchesAction.OnSearchClick -> {
                viewModelScope.launch {
                    eventChannel.send(SavedSearchesEvent.NavigateToSearchResults(action.search.id))
                }
            }
            is SavedSearchesAction.OnDeleteClick -> {
                state = state.copy(
                    selectedSearch = action.search,
                    isDeleteDialogVisible = true
                )
            }
            SavedSearchesAction.OnConfirmDelete -> {
                state.selectedSearch?.let { search ->
                    deleteSearch(search.id)
                }
            }
            SavedSearchesAction.OnDismissDelete -> {
                state = state.copy(
                    selectedSearch = null,
                    isDeleteDialogVisible = false
                )
            }
            is SavedSearchesAction.OnToggleNotifications -> {
                toggleNotifications(action.searchId)
            }
            SavedSearchesAction.OnCreateNewSearch -> {
                viewModelScope.launch {
                    eventChannel.send(SavedSearchesEvent.NavigateToCreateSearch)
                }
            }
        }
    }

    private fun loadSavedSearches() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val searches = getMockSavedSearches()
            state = state.copy(
                savedSearches = searches,
                isLoading = false,
                isEmpty = searches.isEmpty()
            )
        }
    }

    private fun refreshSavedSearches() {
        viewModelScope.launch {
            state = state.copy(isRefreshing = true)
            val searches = getMockSavedSearches()
            state = state.copy(
                savedSearches = searches,
                isRefreshing = false,
                isEmpty = searches.isEmpty()
            )
        }
    }

    private fun deleteSearch(searchId: String) {
        viewModelScope.launch {
            val updatedList = state.savedSearches.filter { it.id != searchId }
            state = state.copy(
                savedSearches = updatedList,
                selectedSearch = null,
                isDeleteDialogVisible = false,
                isEmpty = updatedList.isEmpty()
            )
            eventChannel.send(SavedSearchesEvent.SearchDeleted)
        }
    }

    private fun toggleNotifications(searchId: String) {
        val updatedList = state.savedSearches.map { search ->
            if (search.id == searchId) {
                search.copy(notificationsEnabled = !search.notificationsEnabled)
            } else {
                search
            }
        }
        state = state.copy(savedSearches = updatedList)
    }

    private fun getMockSavedSearches(): List<SavedSearch> {
        return listOf(
            SavedSearch(
                id = "1",
                name = "Apartments in Tirana",
                criteria = "2+ bedrooms • €50k-€150k • Tirana",
                newListingsCount = 5,
                notificationsEnabled = true,
                createdAt = "2 days ago"
            ),
            SavedSearch(
                id = "2",
                name = "Villas near beach",
                criteria = "4+ bedrooms • €200k+ • Durrës",
                newListingsCount = 2,
                notificationsEnabled = true,
                createdAt = "1 week ago"
            ),
            SavedSearch(
                id = "3",
                name = "Studios for rent",
                criteria = "Studio • €300-€500/mo • City Center",
                newListingsCount = 0,
                notificationsEnabled = false,
                createdAt = "2 weeks ago"
            )
        )
    }
}
