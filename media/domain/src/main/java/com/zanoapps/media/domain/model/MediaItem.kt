package com.zanoapps.media.domain.model

data class MediaItem(
    val id: String,
    val url: String,
    val thumbnailUrl: String = url,
    val type: MediaType = MediaType.IMAGE,
    val caption: String = "",
    val propertyId: String = "",
    val order: Int = 0,
    val uploadedAt: Long = System.currentTimeMillis()
)

enum class MediaType {
    IMAGE,
    VIDEO,
    FLOOR_PLAN,
    VIRTUAL_TOUR
}
