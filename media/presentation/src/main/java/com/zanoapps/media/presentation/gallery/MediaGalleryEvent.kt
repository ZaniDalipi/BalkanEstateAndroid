package com.zanoapps.media.presentation.gallery

import com.zanoapps.presentation.ui.UiText

sealed interface MediaGalleryEvent {
    data class Error(val error: UiText) : MediaGalleryEvent
    data object MediaDeleted : MediaGalleryEvent
}
