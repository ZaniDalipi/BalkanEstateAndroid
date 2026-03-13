package com.zanoapps.search.presentation.saved

sealed interface SavedSearchesAction {
    data object OnLoadSavedSearches : SavedSearchesAction
    data class OnDeleteSearch(val searchId: String) : SavedSearchesAction
    data class OnToggleNotifications(val searchId: String) : SavedSearchesAction
    data class OnSearchClick(val search: SavedSearchItem) : SavedSearchesAction
    data class OnEditSearch(val search: SavedSearchItem) : SavedSearchesAction
}
