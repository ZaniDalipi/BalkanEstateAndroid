package com.zanoapps.property_details.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zanoapps.property_details.domain.model.PropertyImageState
import com.zanoapps.property_details.domain.model.PropertyMedia
import com.zanoapps.property_details.domain.model.PropertyVideoState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Media item type for the gallery
 */
sealed class MediaItem {
    data class Video(val video: PropertyVideoState) : MediaItem()
    data class Image(val image: PropertyImageState) : MediaItem()
}

/**
 * An engaging property media gallery that starts with video autoplay
 * and smoothly transitions to images when the video ends.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PropertyMediaGallery(
    media: PropertyMedia,
    modifier: Modifier = Modifier,
    autoPlayVideo: Boolean = true,
    onMediaClick: (Int) -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    // Convert media to a unified list with videos first
    val mediaItems = remember(media) {
        val items = mutableListOf<MediaItem>()
        media.videos.sortedByDescending { it.isMain }.forEach {
            items.add(MediaItem.Video(it))
        }
        media.images.sortedByDescending { it.isMain }.forEach {
            items.add(MediaItem.Image(it))
        }
        items
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var videoHasPlayed by remember { mutableStateOf(false) }
    var showVideoReplay by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { mediaItems.size }
    )

    // Track if we should show the mini video thumbnail for replay
    val hasVideo = media.hasVideo
    val firstImageIndex = mediaItems.indexOfFirst { it is MediaItem.Image }

    LaunchedEffect(pagerState.currentPage) {
        currentIndex = pagerState.currentPage
        // Show video replay button when we're on images and video has played
        showVideoReplay = videoHasPlayed &&
            currentIndex > 0 &&
            mediaItems.getOrNull(0) is MediaItem.Video
    }

    Column(modifier = modifier) {
        // Main media display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val item = mediaItems.getOrNull(page)

                when (item) {
                    is MediaItem.Video -> {
                        VideoPlayer(
                            video = item.video,
                            autoPlay = autoPlayVideo && page == 0 && !videoHasPlayed,
                            startMuted = true,
                            showControls = true,
                            onVideoEnded = {
                                videoHasPlayed = true
                                // Auto-transition to images when video ends
                                if (firstImageIndex >= 0 && autoPlayVideo) {
                                    scope.launch {
                                        delay(500) // Brief pause before transition
                                        pagerState.animateScrollToPage(
                                            page = firstImageIndex,
                                            animationSpec = tween(500)
                                        )
                                    }
                                }
                            },
                            onVideoStarted = {},
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    is MediaItem.Image -> {
                        PropertyImage(
                            image = item.image,
                            onClick = { onMediaClick(page) },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Gray)
                        )
                    }
                }
            }

            // Gradient overlay at top for status bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.4f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Media counter badge
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.Black.copy(alpha = 0.6f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (mediaItems.getOrNull(currentIndex) is MediaItem.Video)
                            Icons.Default.Videocam else Icons.Default.Image,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${currentIndex + 1}/${mediaItems.size}",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Floating video replay button when viewing images
            AnimatedVisibility(
                visible = showVideoReplay,
                enter = scaleIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + fadeIn(),
                exit = scaleOut() + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = "Watch video",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Watch Video",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Thumbnail strip
        MediaThumbnailStrip(
            mediaItems = mediaItems,
            currentIndex = currentIndex,
            onThumbnailClick = { index ->
                scope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        )
    }
}

/**
 * Displays a single property image with loading state
 */
@Composable
private fun PropertyImage(
    image: PropertyImageState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(image.url)
            .crossfade(true)
            .build(),
        contentDescription = image.caption ?: "Property image",
        modifier = modifier
            .clickable(onClick = onClick),
        contentScale = ContentScale.Crop
    )
}

/**
 * Horizontal thumbnail strip for quick media navigation
 */
@Composable
private fun MediaThumbnailStrip(
    mediaItems: List<MediaItem>,
    currentIndex: Int,
    onThumbnailClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Auto-scroll to current item
    LaunchedEffect(currentIndex) {
        scope.launch {
            listState.animateScrollToItem(
                index = maxOf(0, currentIndex - 1)
            )
        }
    }

    LazyRow(
        state = listState,
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        itemsIndexed(mediaItems) { index, item ->
            val isSelected = index == currentIndex
            val borderColor by animateFloatAsState(
                targetValue = if (isSelected) 1f else 0f,
                animationSpec = tween(200),
                label = "border"
            )

            Box(
                modifier = Modifier
                    .size(width = 72.dp, height = 54.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = borderColor),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onThumbnailClick(index) }
            ) {
                when (item) {
                    is MediaItem.Video -> {
                        VideoThumbnailItem(
                            thumbnailUrl = item.video.thumbnailUrl,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    is MediaItem.Image -> {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(item.image.url)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Selection overlay
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            )
                    )
                }
            }
        }
    }
}

/**
 * Video thumbnail with play icon overlay
 */
@Composable
private fun VideoThumbnailItem(
    thumbnailUrl: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        if (thumbnailUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Play icon overlay
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayCircle,
                contentDescription = "Video",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/**
 * Page indicator dots for the media gallery
 */
@Composable
fun MediaPageIndicator(
    pageCount: Int,
    currentPage: Int,
    hasVideo: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage
            val isVideo = hasVideo && index == 0

            val size by animateDpAsState(
                targetValue = if (isSelected) 10.dp else 6.dp,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "dotSize"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .size(size)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) {
                            if (isVideo) MaterialTheme.colorScheme.tertiary
                            else MaterialTheme.colorScheme.primary
                        } else {
                            Color.White.copy(alpha = 0.5f)
                        }
                    )
            )
        }
    }
}
