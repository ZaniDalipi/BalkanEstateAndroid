package com.zanoapps.property_details.domain.model

/**
 * Represents a video associated with a property listing.
 * Supports various video sources including direct URLs and social media embeds.
 */
data class PropertyVideoState(
    val id: String,
    val url: String,
    val thumbnailUrl: String? = null,
    val caption: String? = null,
    val duration: Long? = null, // Duration in milliseconds
    val order: Int = 0,
    val isMain: Boolean = false,
    val videoSource: VideoSource = VideoSource.DIRECT
)

/**
 * Enum representing different video sources/platforms
 */
enum class VideoSource {
    DIRECT,      // Direct video URL (mp4, webm, etc.)
    YOUTUBE,     // YouTube video
    TIKTOK,      // TikTok video
    INSTAGRAM,   // Instagram Reels/Video
    FACEBOOK,    // Facebook video
    VIMEO        // Vimeo video
}

/**
 * Represents all media (images and videos) for a property
 */
data class PropertyMedia(
    val images: List<PropertyImageState> = emptyList(),
    val videos: List<PropertyVideoState> = emptyList()
) {
    /**
     * Returns the main video if available
     */
    val mainVideo: PropertyVideoState?
        get() = videos.firstOrNull { it.isMain } ?: videos.firstOrNull()

    /**
     * Returns the main image if available
     */
    val mainImage: PropertyImageState?
        get() = images.firstOrNull { it.isMain } ?: images.firstOrNull()

    /**
     * Returns true if there's at least one video
     */
    val hasVideo: Boolean
        get() = videos.isNotEmpty()

    /**
     * Returns all media items sorted by order, with main video first
     */
    val allMediaSorted: List<Any>
        get() {
            val sortedVideos = videos.sortedWith(
                compareByDescending<PropertyVideoState> { it.isMain }
                    .thenBy { it.order }
            )
            val sortedImages = images.sortedWith(
                compareByDescending<PropertyImageState> { it.isMain }
                    .thenBy { it.order }
            )
            // Videos first, then images
            return sortedVideos + sortedImages
        }
}

/**
 * Helper extension to extract video ID from various platform URLs
 */
fun String.extractVideoId(source: VideoSource): String? {
    return when (source) {
        VideoSource.YOUTUBE -> {
            // Handle various YouTube URL formats
            val patterns = listOf(
                "(?:youtube\\.com/watch\\?v=|youtu\\.be/|youtube\\.com/embed/)([a-zA-Z0-9_-]+)",
                "youtube\\.com/shorts/([a-zA-Z0-9_-]+)"
            )
            patterns.firstNotNullOfOrNull { pattern ->
                Regex(pattern).find(this)?.groupValues?.getOrNull(1)
            }
        }
        VideoSource.TIKTOK -> {
            Regex("tiktok\\.com/@[^/]+/video/(\\d+)").find(this)?.groupValues?.getOrNull(1)
        }
        VideoSource.INSTAGRAM -> {
            Regex("instagram\\.com/(?:reel|p)/([a-zA-Z0-9_-]+)").find(this)?.groupValues?.getOrNull(1)
        }
        VideoSource.VIMEO -> {
            Regex("vimeo\\.com/(\\d+)").find(this)?.groupValues?.getOrNull(1)
        }
        else -> null
    }
}

/**
 * Detects the video source from a URL
 */
fun String.detectVideoSource(): VideoSource {
    return when {
        contains("youtube.com") || contains("youtu.be") -> VideoSource.YOUTUBE
        contains("tiktok.com") -> VideoSource.TIKTOK
        contains("instagram.com") -> VideoSource.INSTAGRAM
        contains("facebook.com") || contains("fb.watch") -> VideoSource.FACEBOOK
        contains("vimeo.com") -> VideoSource.VIMEO
        else -> VideoSource.DIRECT
    }
}
