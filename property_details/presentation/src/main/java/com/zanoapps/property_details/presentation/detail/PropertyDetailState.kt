package com.zanoapps.property_details.presentation.detail

import com.zanoapps.core.domain.model.BalkanEstateProperty

data class PropertyDetailState(
    val property: BalkanEstateProperty? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val selectedImageIndex: Int = 0,
    val isContactAgentSheetOpen: Boolean = false,
    val isShareSheetOpen: Boolean = false,
    val showAllAmenities: Boolean = false,
    val errorMessage: String? = null,
    val similarProperties: List<BalkanEstateProperty> = emptyList(),
    val isLoadingSimilar: Boolean = false,
    val contactName: String = "",
    val contactEmail: String = "",
    val contactPhone: String = "",
    val contactMessage: String = "I'm interested in this property. Please contact me with more details.",
    val isSendingMessage: Boolean = false,
    val messageSent: Boolean = false
)
