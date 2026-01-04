package com.zanoapps.media.presentation.gallery

import com.zanoapps.media.domain.model.MediaImage

data class ImageGalleryState(
    val images: List<MediaImage> = emptyList(),
    val currentIndex: Int = 0,
    val isLoading: Boolean = false,
    val isZoomed: Boolean = false,
    val errorMessage: String? = null
)
