package com.zanoapps.property_details.presentation.details

import com.zanoapps.core.domain.model.BalkanEstateProperty

data class PropertyDetailsState(
    val property: BalkanEstateProperty? = null,
    val isLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val currentImageIndex: Int = 0,
    val isContactAgentDialogVisible: Boolean = false,
    val isShareDialogVisible: Boolean = false,
    val isImageFullscreen: Boolean = false,
    val errorMessage: String? = null,
    val similarProperties: List<BalkanEstateProperty> = emptyList(),
    val isLoadingSimilar: Boolean = false
)
