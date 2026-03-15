package com.zanoapps.media.data.repository

import com.zanoapps.media.domain.model.MediaItem
import com.zanoapps.media.domain.model.MediaType
import com.zanoapps.media.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MediaRepositoryImpl : MediaRepository {

    private val mediaItems = MutableStateFlow(generateMockMedia())

    override fun getMediaForProperty(propertyId: String): Flow<List<MediaItem>> {
        return mediaItems.map { items ->
            items.filter { it.propertyId == propertyId }.sortedBy { it.order }
        }
    }

    override suspend fun addMedia(media: MediaItem) {
        mediaItems.value = mediaItems.value + media
    }

    override suspend fun deleteMedia(mediaId: String) {
        mediaItems.value = mediaItems.value.filter { it.id != mediaId }
    }

    override suspend fun reorderMedia(propertyId: String, mediaIds: List<String>) {
        mediaItems.value = mediaItems.value.map { item ->
            if (item.propertyId == propertyId) {
                val newOrder = mediaIds.indexOf(item.id)
                if (newOrder >= 0) item.copy(order = newOrder) else item
            } else item
        }
    }

    private fun generateMockMedia(): List<MediaItem> {
        return listOf(
            MediaItem(id = "m1", url = "https://picsum.photos/800/600?random=1", type = MediaType.IMAGE, caption = "Living Room", propertyId = "p1", order = 0),
            MediaItem(id = "m2", url = "https://picsum.photos/800/600?random=2", type = MediaType.IMAGE, caption = "Kitchen", propertyId = "p1", order = 1),
            MediaItem(id = "m3", url = "https://picsum.photos/800/600?random=3", type = MediaType.IMAGE, caption = "Bedroom", propertyId = "p1", order = 2),
            MediaItem(id = "m4", url = "https://picsum.photos/800/600?random=4", type = MediaType.FLOOR_PLAN, caption = "Floor Plan", propertyId = "p1", order = 3),
            MediaItem(id = "m5", url = "https://picsum.photos/800/600?random=5", type = MediaType.IMAGE, caption = "Exterior", propertyId = "p2", order = 0),
            MediaItem(id = "m6", url = "https://picsum.photos/800/600?random=6", type = MediaType.IMAGE, caption = "Pool Area", propertyId = "p2", order = 1),
            MediaItem(id = "m7", url = "https://picsum.photos/800/600?random=7", type = MediaType.VIRTUAL_TOUR, caption = "Virtual Tour", propertyId = "p2", order = 2)
        )
    }
}
