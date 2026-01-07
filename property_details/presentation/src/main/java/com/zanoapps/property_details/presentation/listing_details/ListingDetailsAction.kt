package com.zanoapps.property_details.presentation.listing_details

sealed interface ListingDetailsAction {
    // Navigation actions
    data object OnBackClick : ListingDetailsAction

    // Favorite actions
    data object OnFavoriteToggle : ListingDetailsAction

    // Image gallery actions
    data class OnImageSelected(val index: Int) : ListingDetailsAction
    data object OnOpenImageGallery : ListingDetailsAction
    data object OnCloseImageGallery : ListingDetailsAction
    data object OnNextImage : ListingDetailsAction
    data object OnPreviousImage : ListingDetailsAction

    // Contact agent actions
    data object OnContactAgentClick : ListingDetailsAction
    data object OnCloseContactForm : ListingDetailsAction
    data class OnContactNameChanged(val name: String) : ListingDetailsAction
    data class OnContactEmailChanged(val email: String) : ListingDetailsAction
    data class OnContactPhoneChanged(val phone: String) : ListingDetailsAction
    data class OnContactMessageChanged(val message: String) : ListingDetailsAction
    data object OnSubmitContactForm : ListingDetailsAction

    // Schedule tour actions
    data object OnScheduleTourClick : ListingDetailsAction
    data object OnCloseScheduleTour : ListingDetailsAction
    data class OnTourDateSelected(val date: String) : ListingDetailsAction
    data class OnTourTimeSelected(val time: String) : ListingDetailsAction
    data object OnSubmitTourRequest : ListingDetailsAction

    // Share actions
    data object OnShareClick : ListingDetailsAction
    data object OnCloseShareDialog : ListingDetailsAction
    data class OnShareVia(val platform: SharePlatform) : ListingDetailsAction

    // Section expansion actions
    data class OnToggleSection(val section: DetailSection) : ListingDetailsAction

    // Refresh action
    data object OnRefresh : ListingDetailsAction

    // Map actions
    data object OnViewOnMapClick : ListingDetailsAction

    // Call agent action
    data object OnCallAgentClick : ListingDetailsAction
}

enum class SharePlatform {
    COPY_LINK,
    WHATSAPP,
    EMAIL,
    SMS,
    MORE
}
