package com.zanoapps.media.presentation.gallery

sealed interface ImageGalleryAction {
    data object OnBackClick : ImageGalleryAction
    data class OnPageChange(val index: Int) : ImageGalleryAction
    data object OnShareClick : ImageGalleryAction
    data object OnDownloadClick : ImageGalleryAction
    data object OnToggleZoom : ImageGalleryAction
}
