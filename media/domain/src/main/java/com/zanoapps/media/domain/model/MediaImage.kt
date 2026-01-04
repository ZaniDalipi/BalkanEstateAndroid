package com.zanoapps.media.domain.model

data class MediaImage(
    val id: String,
    val url: String,
    val thumbnailUrl: String? = null,
    val caption: String? = null,
    val width: Int? = null,
    val height: Int? = null
)
