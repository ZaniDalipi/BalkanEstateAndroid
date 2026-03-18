package com.zanoapps.favourites.presentation.compare

import com.zanoapps.core.domain.model.BalkanEstateProperty

data class PropertyComparisonState(
    val properties: List<BalkanEstateProperty> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
