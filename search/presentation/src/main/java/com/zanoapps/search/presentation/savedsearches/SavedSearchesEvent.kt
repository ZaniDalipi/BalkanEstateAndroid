package com.zanoapps.search.presentation.savedsearches

sealed interface SavedSearchesEvent {
    data class NavigateToSearchResults(val searchId: String) : SavedSearchesEvent
    data object NavigateToCreateSearch : SavedSearchesEvent
    data class Error(val message: String) : SavedSearchesEvent
    data object SearchDeleted : SavedSearchesEvent
}
