package com.zanoapps.search.presentation.savedsearches

sealed interface SavedSearchesAction {
    data object LoadSavedSearches : SavedSearchesAction
    data object RefreshSavedSearches : SavedSearchesAction
    data class OnSearchClick(val search: SavedSearch) : SavedSearchesAction
    data class OnDeleteClick(val search: SavedSearch) : SavedSearchesAction
    data object OnConfirmDelete : SavedSearchesAction
    data object OnDismissDelete : SavedSearchesAction
    data class OnToggleNotifications(val searchId: String) : SavedSearchesAction
    data object OnCreateNewSearch : SavedSearchesAction
}
