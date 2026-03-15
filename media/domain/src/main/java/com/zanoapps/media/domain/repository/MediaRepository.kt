package com.zanoapps.media.domain.repository

import com.zanoapps.media.domain.model.MediaItem
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getMediaForProperty(propertyId: String): Flow<List<MediaItem>>
    suspend fun addMedia(media: MediaItem)
    suspend fun deleteMedia(mediaId: String)
    suspend fun reorderMedia(propertyId: String, mediaIds: List<String>)
}
