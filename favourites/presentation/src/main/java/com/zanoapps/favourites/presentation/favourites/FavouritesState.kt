package com.zanoapps.favourites.presentation.favourites

import com.zanoapps.core.domain.model.BalkanEstateProperty

data class FavouritesState(
    val properties: List<BalkanEstateProperty> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isEmpty: Boolean = true
)
