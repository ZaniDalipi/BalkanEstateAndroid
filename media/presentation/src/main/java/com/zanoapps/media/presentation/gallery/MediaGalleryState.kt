package com.zanoapps.media.presentation.gallery

import com.zanoapps.media.domain.model.MediaItem

data class MediaGalleryState(
    val mediaItems: List<MediaItem> = emptyList(),
    val filteredItems: List<MediaItem> = emptyList(),
    val selectedMedia: MediaItem? = null,
    val isLoading: Boolean = false,
    val propertyId: String = "",
    val typeFilter: String? = null
)
