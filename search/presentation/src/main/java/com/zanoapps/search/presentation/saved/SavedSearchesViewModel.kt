package com.zanoapps.search.presentation.saved

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.search.domain.repository.SavedSearchRepository
import com.zanoapps.search.domain.repository.SavedSearchItem as DomainSavedSearchItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SavedSearchesViewModel(
    private val savedSearchRepository: SavedSearchRepository
) : ViewModel() {

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
            is SavedSearchesAction.OnEditSearch -> {
                viewModelScope.launch {
                    eventChannel.send(SavedSearchesEvent.NavigateToSearch(action.search.id))
                }
            }
        }
    }

    private fun loadSavedSearches() {
        savedSearchRepository.getSavedSearches()
            .onEach { domainItems ->
                val items = domainItems.map { item ->
                    SavedSearchItem(
                        id = item.id,
                        name = item.name,
                        query = item.query,
                        location = item.location,
                        propertyType = item.propertyType,
                        priceRange = item.priceRange,
                        bedrooms = item.bedrooms,
                        notificationsEnabled = item.notificationsEnabled,
                        matchCount = item.matchCount,
                        newCount = item.newCount,
                        createdAt = formatCreatedAt(item.createdAt)
                    )
                }
                state = state.copy(savedSearches = items, isLoading = false)
            }
            .launchIn(viewModelScope)
    }

    private fun formatCreatedAt(timestamp: Long): String {
        val diff = System.currentTimeMillis() - timestamp
        val days = diff / (1000 * 60 * 60 * 24)
        return when {
            days < 1 -> "Today"
            days < 2 -> "Yesterday"
            days < 7 -> "$days days ago"
            days < 30 -> "${days / 7} week${if (days / 7 > 1) "s" else ""} ago"
            else -> "${days / 30} month${if (days / 30 > 1) "s" else ""} ago"
        }
    }

    private fun deleteSearch(searchId: String) {
        viewModelScope.launch {
            savedSearchRepository.deleteSearch(searchId)
            eventChannel.send(SavedSearchesEvent.SearchDeleted(searchId))
        }
    }

    private fun toggleNotifications(searchId: String) {
        val item = state.savedSearches.find { it.id == searchId } ?: return
        viewModelScope.launch {
            savedSearchRepository.toggleNotifications(searchId, !item.notificationsEnabled)
        }
    }
}
