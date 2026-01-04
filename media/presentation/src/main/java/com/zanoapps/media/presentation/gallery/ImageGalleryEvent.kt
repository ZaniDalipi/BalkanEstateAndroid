package com.zanoapps.media.presentation.gallery

sealed interface ImageGalleryEvent {
    data object NavigateBack : ImageGalleryEvent
    data class ShareImage(val url: String) : ImageGalleryEvent
    data class DownloadImage(val url: String) : ImageGalleryEvent
    data class ShowError(val message: String) : ImageGalleryEvent
}
