package com.zanoapps.favourites.presentation.favourites

import com.zanoapps.core.domain.model.BalkanEstateProperty

data class FavouritesState(
    val savedProperties: List<BalkanEstateProperty> = emptyList(),
    val isLoading: Boolean = false,
    val selectedSortOption: FavouritesSortOption = FavouritesSortOption.RECENTLY_SAVED,
    val searchQuery: String = "",
    val filteredProperties: List<BalkanEstateProperty> = emptyList(),
    val isCompareMode: Boolean = false,
    val selectedForCompare: Set<String> = emptySet(),
    val errorMessage: String? = null
)

enum class FavouritesSortOption(val displayName: String) {
    RECENTLY_SAVED("Recently Saved"),
    PRICE_LOW_TO_HIGH("Price: Low to High"),
    PRICE_HIGH_TO_LOW("Price: High to Low"),
    MOST_BEDROOMS("Most Bedrooms")
}
