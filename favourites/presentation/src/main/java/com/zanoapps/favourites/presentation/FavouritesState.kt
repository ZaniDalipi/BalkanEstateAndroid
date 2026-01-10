package com.zanoapps.favourites.presentation

import com.zanoapps.core.domain.model.BalkanEstateProperty

data class FavouritesState(
    val favouriteProperties: List<BalkanEstateProperty> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val selectedProperty: BalkanEstateProperty? = null,
    val isGridView: Boolean = false,
    val errorMessage: String? = null,
    val isEmpty: Boolean = false
)
