package com.zanoapps.media.presentation.gallery

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.BalkanEstateTextSecondary
import com.zanoapps.core.presentation.designsystem.BalkanEstateBackground
import com.zanoapps.core.presentation.designsystem.BalkanEstateCardBackground
import com.zanoapps.media.domain.model.MediaItem
import com.zanoapps.media.domain.model.MediaType
import com.zanoapps.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun MediaGalleryScreenRoot(
    viewModel: MediaGalleryViewModel = koinViewModel(),
    propertyId: String,
    onBack: () -> Unit
) {
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is MediaGalleryEvent.Error -> { /* Handle error */ }
            MediaGalleryEvent.MediaDeleted -> { /* Handle deletion */ }
        }
    }

    if (viewModel.state.propertyId.isEmpty()) {
        viewModel.onAction(MediaGalleryAction.OnLoadMedia(propertyId))
    }

    MediaGalleryScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun MediaGalleryScreen(
    state: MediaGalleryState,
    onAction: (MediaGalleryAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BalkanEstateBackground)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BalkanEstatePrimaryBlue)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Media Gallery",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Filter Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filters = listOf(
                null to "All",
                MediaType.IMAGE.name to "Images",
                MediaType.VIDEO.name to "Videos",
                MediaType.FLOOR_PLAN.name to "Floor Plans",
                MediaType.VIRTUAL_TOUR.name to "Virtual Tours"
            )

            items(filters.size) { index ->
                val (filterValue, label) = filters[index]
                val isSelected = state.typeFilter == filterValue
                AssistChip(
                    onClick = { onAction(MediaGalleryAction.OnFilterByType(filterValue)) },
                    label = {
                        Text(
                            text = label,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (isSelected) BalkanEstatePrimaryBlue else BalkanEstateCardBackground,
                        labelColor = if (isSelected) Color.White else BalkanEstateGray
                    ),
                    border = AssistChipDefaults.assistChipBorder(
                        enabled = true,
                        borderColor = if (isSelected) BalkanEstatePrimaryBlue else BalkanEstateGray.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        // Content
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
                }
            }
            state.filteredItems.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = BalkanEstateGray.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No media found",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = BalkanEstateGray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "There are no media items to display for this property.",
                            fontSize = 14.sp,
                            color = BalkanEstateTextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 48.dp)
                        )
                    }
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.filteredItems, key = { it.id }) { mediaItem ->
                        MediaGridItem(
                            mediaItem = mediaItem,
                            onClick = { onAction(MediaGalleryAction.OnMediaSelected(mediaItem.id)) }
                        )
                    }
                }
            }
        }
    }

    // Full-screen dialog
    if (state.selectedMedia != null) {
        FullScreenMediaDialog(
            mediaItem = state.selectedMedia,
            onDismiss = { onAction(MediaGalleryAction.OnDismissFullScreen) },
            onDelete = { onAction(MediaGalleryAction.OnDeleteMedia(state.selectedMedia.id)) }
        )
    }
}

@Composable
private fun MediaGridItem(
    mediaItem: MediaItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = BalkanEstateCardBackground)
    ) {
        Column {
            // Placeholder colored box with media type icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(getMediaTypeColor(mediaItem.type)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getMediaTypeIcon(mediaItem.type),
                    contentDescription = mediaItem.type.name,
                    modifier = Modifier.size(40.dp),
                    tint = Color.White.copy(alpha = 0.8f)
                )

                // Type badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = getMediaTypeLabel(mediaItem.type),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Caption
            if (mediaItem.caption.isNotEmpty()) {
                Text(
                    text = mediaItem.caption,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
private fun FullScreenMediaDialog(
    mediaItem: MediaItem,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Media placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .align(Alignment.Center)
                    .background(getMediaTypeColor(mediaItem.type)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = getMediaTypeIcon(mediaItem.type),
                        contentDescription = mediaItem.type.name,
                        modifier = Modifier.size(72.dp),
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = mediaItem.caption,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Top bar with close and delete
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                IconButton(onClick = {
                    onDelete()
                    onDismiss()
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = BalkanEstateRed,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Bottom caption bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = mediaItem.caption,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = getMediaTypeLabel(mediaItem.type),
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

private fun getMediaTypeColor(type: MediaType): Color {
    return when (type) {
        MediaType.IMAGE -> Color(0xFF5B8DEF)
        MediaType.VIDEO -> Color(0xFFE06B6B)
        MediaType.FLOOR_PLAN -> Color(0xFF4CAF93)
        MediaType.VIRTUAL_TOUR -> Color(0xFFA06BCC)
    }
}

private fun getMediaTypeIcon(type: MediaType): ImageVector {
    return when (type) {
        MediaType.IMAGE -> Icons.Default.Home
        MediaType.VIDEO -> Icons.Default.PlayArrow
        MediaType.FLOOR_PLAN -> Icons.Default.Place
        MediaType.VIRTUAL_TOUR -> Icons.Default.Place
    }
}

private fun getMediaTypeLabel(type: MediaType): String {
    return when (type) {
        MediaType.IMAGE -> "Image"
        MediaType.VIDEO -> "Video"
        MediaType.FLOOR_PLAN -> "Floor Plan"
        MediaType.VIRTUAL_TOUR -> "Virtual Tour"
    }
}
