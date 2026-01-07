package com.zanoapps.search.presentation.search

import androidx.compose.foundation.text.input.TextFieldState
import com.zanoapps.core.domain.enums.SortOption
import com.zanoapps.search.domain.model.MapLocation
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.search.domain.model.SearchFilters
import com.zanoapps.search.presentation.components.AIChatMessage
import com.zanoapps.search.presentation.components.SearchFiltersData

data class SearchState(
    val mapLocation: MapLocation = MapLocation(
        latitude = 41.3275,
        longitude = 19.8187,
        zoom = 12f
    ),

    val isMapTypeRoad: Boolean = true,

    val properties: List<BalkanEstateProperty> = emptyList(),
    val filteredProperties: List<BalkanEstateProperty> = emptyList(),
    val selectedBalkanEstateProperty: BalkanEstateProperty? = null,
    val favoritePropertyIds: Set<String> = emptySet(),

    val searchQuery: TextFieldState = TextFieldState(),
    val filters: SearchFilters = SearchFilters(),
    val sortOption: SortOption = SortOption.NEWEST,
    val hasActiveFilter: Boolean = false,

    val isLoadingProperties: Boolean = false,
    val isRefreshing: Boolean = false,
    val isSavingSearch: Boolean = false,

    val isBottomSheetExpanded: Boolean = false,

    val savedSearchCount: Int = 0,

    val errorMessage: String? = null,

    // Drawer and view mode states
    val isDrawerOpen: Boolean = false,
    val isListView: Boolean = true,

    // Subscription
    val subscriptionEmail: String = "",

    // Filters screen state
    val isFiltersScreenVisible: Boolean = false,
    val filtersData: SearchFiltersData = SearchFiltersData(),

    // AI Chat state
    val aiChatMessages: List<AIChatMessage> = listOf(
        AIChatMessage(
            id = "welcome",
            content = "Hello! Welcome to Balkan Estate. How can I help you find a property today?",
            isFromUser = false
        )
    ),
    val isAITyping: Boolean = false,

    // Map visibility banner
    val showingMapVisibleProperties: Boolean = false
)
