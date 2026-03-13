package com.zanoapps.search.presentation.saved

import com.zanoapps.presentation.ui.UiText

sealed interface SavedSearchesEvent {
    data class Error(val error: UiText) : SavedSearchesEvent
    data class NavigateToSearch(val searchId: String) : SavedSearchesEvent
    data class SearchDeleted(val searchId: String) : SavedSearchesEvent
}
