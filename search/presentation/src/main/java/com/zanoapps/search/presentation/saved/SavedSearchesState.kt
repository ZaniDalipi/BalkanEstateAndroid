package com.zanoapps.search.presentation.saved

data class SavedSearchesState(
    val savedSearches: List<SavedSearchItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class SavedSearchItem(
    val id: String,
    val name: String,
    val query: String = "",
    val location: String = "",
    val propertyType: String = "",
    val priceRange: String = "",
    val bedrooms: String = "",
    val notificationsEnabled: Boolean = true,
    val matchCount: Int = 0,
    val newCount: Int = 0,
    val createdAt: String = ""
)
