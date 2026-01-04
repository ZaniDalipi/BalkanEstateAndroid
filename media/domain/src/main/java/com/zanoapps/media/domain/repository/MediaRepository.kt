package com.zanoapps.media.domain.repository

import com.zanoapps.media.domain.model.MediaImage

interface MediaRepository {
    suspend fun getPropertyImages(propertyId: String): List<MediaImage>
    suspend fun downloadImage(url: String): ByteArray?
}
