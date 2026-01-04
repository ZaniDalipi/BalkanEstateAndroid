package com.zanoapps.media.data.repository

import com.zanoapps.media.domain.model.MediaImage
import com.zanoapps.media.domain.repository.MediaRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes

class MediaRepositoryImpl(
    private val httpClient: HttpClient
) : MediaRepository {

    override suspend fun getPropertyImages(propertyId: String): List<MediaImage> {
        // This would normally fetch from API
        // For now, return mock placeholder images
        return listOf(
            MediaImage(
                id = "1",
                url = "https://images.unsplash.com/photo-1564013799919-ab600027ffc6?w=800",
                caption = "Living Room"
            ),
            MediaImage(
                id = "2",
                url = "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=800",
                caption = "Kitchen"
            ),
            MediaImage(
                id = "3",
                url = "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=800",
                caption = "Bedroom"
            ),
            MediaImage(
                id = "4",
                url = "https://images.unsplash.com/photo-1552321554-5fefe8c9ef14?w=800",
                caption = "Bathroom"
            ),
            MediaImage(
                id = "5",
                url = "https://images.unsplash.com/photo-1558036117-15d82a90b9b1?w=800",
                caption = "Exterior"
            )
        )
    }

    override suspend fun downloadImage(url: String): ByteArray? {
        return try {
            httpClient.get(url).readBytes()
        } catch (e: Exception) {
            null
        }
    }
}
