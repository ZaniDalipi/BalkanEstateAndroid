package com.zanoapps.media.presentation.gallery

sealed interface MediaGalleryAction {
    data class OnLoadMedia(val propertyId: String) : MediaGalleryAction
    data class OnMediaSelected(val mediaId: String) : MediaGalleryAction
    data class OnDeleteMedia(val mediaId: String) : MediaGalleryAction
    data object OnDismissFullScreen : MediaGalleryAction
    data class OnFilterByType(val typeFilter: String?) : MediaGalleryAction
}
