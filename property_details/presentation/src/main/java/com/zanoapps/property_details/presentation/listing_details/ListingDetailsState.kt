package com.zanoapps.property_details.presentation.listing_details

import com.zanoapps.property_details.domain.model.PropertyImageState
import com.zanoapps.property_details.domain.model.PropertyState

data class ListingDetailsState(
    // Property data
    val property: PropertyState? = null,
    val propertyId: String = "",

    // Loading states
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,

    // Error state
    val errorMessage: String? = null,

    // Favorite state
    val isFavorite: Boolean = false,

    // Image gallery state
    val selectedImageIndex: Int = 0,
    val isImageGalleryOpen: Boolean = false,

    // Contact form state
    val isContactFormOpen: Boolean = false,
    val contactName: String = "",
    val contactEmail: String = "",
    val contactPhone: String = "",
    val contactMessage: String = "",
    val isSubmittingContact: Boolean = false,

    // Schedule tour state
    val isScheduleTourOpen: Boolean = false,
    val selectedTourDate: String = "",
    val selectedTourTime: String = "",
    val isSubmittingTour: Boolean = false,

    // Share state
    val isShareDialogOpen: Boolean = false,

    // Expanded sections state
    val expandedSections: Set<DetailSection> = setOf(
        DetailSection.OVERVIEW,
        DetailSection.FEATURES
    )
)

enum class DetailSection {
    OVERVIEW,
    FEATURES,
    AMENITIES,
    FINANCIAL,
    LOCATION,
    AGENT
}

// Extension to get the main image URL
val ListingDetailsState.mainImageUrl: String
    get() = property?.images?.firstOrNull { it.isMain }?.url
        ?: property?.images?.firstOrNull()?.url
        ?: ""

// Extension to get all image URLs
val ListingDetailsState.allImages: List<PropertyImageState>
    get() = property?.images ?: emptyList()

// Extension to get the current selected image
val ListingDetailsState.currentImage: PropertyImageState?
    get() = property?.images?.getOrNull(selectedImageIndex)
