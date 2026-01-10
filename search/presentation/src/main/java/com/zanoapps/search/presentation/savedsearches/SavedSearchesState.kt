package com.zanoapps.search.presentation.savedsearches

data class SavedSearch(
    val id: String,
    val name: String,
    val criteria: String,
    val newListingsCount: Int,
    val notificationsEnabled: Boolean,
    val createdAt: String
)

data class SavedSearchesState(
    val savedSearches: List<SavedSearch> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isEmpty: Boolean = false,
    val errorMessage: String? = null,
    val selectedSearch: SavedSearch? = null,
    val isDeleteDialogVisible: Boolean = false
)
