package com.zanoapps.property_details.presentation.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.zanoapps.property_details.domain.model.PropertyVideoState
import com.zanoapps.property_details.domain.model.VideoSource
import kotlinx.coroutines.delay

/**
 * A composable video player that supports various video sources.
 * Features autoplay, mute toggle, and replay functionality.
 */
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    video: PropertyVideoState,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true,
    startMuted: Boolean = true,
    showControls: Boolean = true,
    onVideoEnded: () -> Unit = {},
    onVideoStarted: () -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isPlaying by remember { mutableStateOf(autoPlay) }
    var isMuted by remember { mutableStateOf(startMuted) }
    var isLoading by remember { mutableStateOf(true) }
    var hasEnded by remember { mutableStateOf(false) }
    var showControlsOverlay by remember { mutableStateOf(false) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val videoUrl = getPlayableUrl(video.url, video.videoSource)
            val mediaItem = MediaItem.fromUri(videoUrl)
            setMediaItem(mediaItem)
            playWhenReady = autoPlay
            volume = if (startMuted) 0f else 1f
            repeatMode = Player.REPEAT_MODE_OFF
            prepare()
        }
    }

    // Handle player events
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        isLoading = true
                    }
                    Player.STATE_READY -> {
                        isLoading = false
                        if (exoPlayer.playWhenReady) {
                            onVideoStarted()
                        }
                    }
                    Player.STATE_ENDED -> {
                        hasEnded = true
                        isPlaying = false
                        onVideoEnded()
                    }
                    Player.STATE_IDLE -> {
                        isLoading = false
                    }
                }
            }

            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }
        }

        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    // Handle lifecycle
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer.pause()
                }
                Lifecycle.Event.ON_RESUME -> {
                    if (isPlaying && !hasEnded) {
                        exoPlayer.play()
                    }
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Auto-hide controls
    LaunchedEffect(showControlsOverlay) {
        if (showControlsOverlay) {
            delay(3000)
            showControlsOverlay = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                showControlsOverlay = !showControlsOverlay
            }
    ) {
        // Video surface
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Loading indicator
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }

        // Controls overlay
        if (showControls) {
            AnimatedVisibility(
                visible = showControlsOverlay || hasEnded || !isPlaying,
                enter = fadeIn(animationSpec = tween(200)),
                exit = fadeOut(animationSpec = tween(200)),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                ) {
                    // Center play/pause/replay button
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f))
                            .clickable {
                                when {
                                    hasEnded -> {
                                        exoPlayer.seekTo(0)
                                        exoPlayer.play()
                                        hasEnded = false
                                    }
                                    isPlaying -> exoPlayer.pause()
                                    else -> exoPlayer.play()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when {
                                hasEnded -> Icons.Default.Replay
                                isPlaying -> Icons.Default.Pause
                                else -> Icons.Default.PlayArrow
                            },
                            contentDescription = when {
                                hasEnded -> "Replay"
                                isPlaying -> "Pause"
                                else -> "Play"
                            },
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Mute/unmute button in bottom right
                    IconButton(
                        onClick = {
                            isMuted = !isMuted
                            exoPlayer.volume = if (isMuted) 0f else 1f
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                            contentDescription = if (isMuted) "Unmute" else "Mute",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Converts social media video URLs to playable URLs where possible.
 * For platforms that don't support direct playback, falls back to web embeds.
 */
private fun getPlayableUrl(url: String, source: VideoSource): String {
    return when (source) {
        VideoSource.DIRECT -> url
        VideoSource.YOUTUBE -> {
            // For YouTube, we would need to use YouTube Android Player API
            // or extract the direct video URL (which may require additional libraries)
            // For now, return the original URL - in production, use YouTube Player API
            url
        }
        VideoSource.VIMEO -> {
            // Similar to YouTube, Vimeo requires their player API
            url
        }
        else -> url
    }
}

/**
 * A lightweight video thumbnail that shows a preview image with a play button.
 * Useful for video previews in lists or grids.
 */
@Composable
fun VideoThumbnail(
    thumbnailUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Thumbnail image would go here using Coil
        // For now, show a play button overlay
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.9f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play video",
                tint = Color.Black,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
