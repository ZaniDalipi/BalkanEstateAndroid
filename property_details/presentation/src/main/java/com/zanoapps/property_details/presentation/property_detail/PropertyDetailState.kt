package com.zanoapps.property_details.presentation.property_detail

import com.zanoapps.core.domain.model.BalkanEstateProperty

data class PropertyDetailState(
    val property: BalkanEstateProperty? = null,
    val isLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val currentImageIndex: Int = 0,
    val isImageViewerOpen: Boolean = false,
    val isContactFormOpen: Boolean = false
)
