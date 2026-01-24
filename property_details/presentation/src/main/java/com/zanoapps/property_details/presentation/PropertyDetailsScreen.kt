package com.zanoapps.property_details.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalPhone
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zanoapps.property_details.domain.model.PropertyImageState
import com.zanoapps.property_details.domain.model.PropertyMedia
import com.zanoapps.property_details.domain.model.PropertyVideoState
import com.zanoapps.property_details.domain.model.VideoSource
import com.zanoapps.property_details.presentation.components.PropertyMediaGallery
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat

/**
 * Root composable for the Property Details screen
 */
@Composable
fun PropertyDetailsScreenRoot(
    propertyId: String,
    onBackClick: () -> Unit,
    viewModel: PropertyDetailsViewModel = koinViewModel()
) {
    LaunchedEffect(propertyId) {
        viewModel.loadProperty(propertyId)
    }

    PropertyDetailsScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                PropertyDetailsAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PropertyDetailsScreen(
    state: PropertyDetailsState,
    onAction: (PropertyDetailsAction) -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.property) {
        delay(100)
        isVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        } else if (state.property != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                // Media Gallery with Video
                item {
                    Box(modifier = Modifier.statusBarsPadding()) {
                        PropertyMediaGallery(
                            media = state.media,
                            autoPlayVideo = true,
                            onMediaClick = { /* Handle fullscreen */ }
                        )

                        // Back button overlay
                        IconButton(
                            onClick = { onAction(PropertyDetailsAction.OnBackClick) },
                            modifier = Modifier
                                .padding(8.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        // Share and Favorite buttons
                        Row(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            IconButton(
                                onClick = { onAction(PropertyDetailsAction.OnShareClick) },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.5f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = { onAction(PropertyDetailsAction.OnFavoriteClick) },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.5f))
                            ) {
                                Icon(
                                    imageVector = if (state.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = if (state.isFavorite) "Remove from favorites" else "Add to favorites",
                                    tint = if (state.isFavorite) Color.Red else Color.White
                                )
                            }
                        }
                    }
                }

                // Property Info Card
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = slideInVertically(
                            initialOffsetY = { 100 },
                            animationSpec = spring(stiffness = Spring.StiffnessLow)
                        ) + fadeIn()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(top = 16.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Property Type Badge
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = "${state.property.listingType} • ${state.property.propertyType}",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Title
                                Text(
                                    text = state.property.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Location
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${state.property.address}, ${state.property.city}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Price
                                Text(
                                    text = formatPrice(state.property.price, state.property.currency),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                // Property Features
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = slideInVertically(
                            initialOffsetY = { 150 },
                            animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = 0.8f)
                        ) + fadeIn()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(top = 12.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                FeatureItem(
                                    icon = Icons.Default.Bed,
                                    value = state.property.bedrooms.toString(),
                                    label = "Beds"
                                )
                                FeatureItem(
                                    icon = Icons.Default.Bathtub,
                                    value = state.property.bathrooms.toString(),
                                    label = "Baths"
                                )
                                FeatureItem(
                                    icon = Icons.Default.SquareFoot,
                                    value = "${state.property.squareFootage}",
                                    label = "m²"
                                )
                                if (state.property.parkingSpaces > 0) {
                                    FeatureItem(
                                        icon = Icons.Default.DirectionsCar,
                                        value = state.property.parkingSpaces.toString(),
                                        label = "Parking"
                                    )
                                }
                            }
                        }
                    }
                }

                // Description
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = slideInVertically(
                            initialOffsetY = { 200 },
                            animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = 0.8f)
                        ) + fadeIn()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(top = 12.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Description",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = state.property.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 24.sp
                                )
                            }
                        }
                    }
                }

                // Amenities
                if (state.property.amenities.isNotEmpty()) {
                    item {
                        AnimatedVisibility(
                            visible = isVisible,
                            enter = slideInVertically(
                                initialOffsetY = { 250 },
                                animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = 0.8f)
                            ) + fadeIn()
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 12.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Amenities",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        state.property.amenities.forEach { amenity ->
                                            AmenityChip(text = amenity.name)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Agent Info
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = slideInVertically(
                            initialOffsetY = { 300 },
                            animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = 0.8f)
                        ) + fadeIn()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(top = 12.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Listed By",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Agent avatar placeholder
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = state.property.agentName.take(2).uppercase(),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = state.property.agentName,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "Real Estate Agent",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { onAction(PropertyDetailsAction.OnCallAgentClick) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocalPhone,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Call")
                                    }

                                    OutlinedButton(
                                        onClick = { onAction(PropertyDetailsAction.OnEmailAgentClick) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Mail,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Email")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom CTA Button
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                shadowElevation = 16.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { onAction(PropertyDetailsAction.OnScheduleViewingClick) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Schedule Visit",
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = { onAction(PropertyDetailsAction.OnContactAgentClick) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Contact Agent",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureItem(
    icon: ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AmenityChip(text: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = text.replace("_", " ").lowercase()
                .replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

private fun formatPrice(price: Double, currency: String): String {
    val formatter = NumberFormat.getNumberInstance()
    formatter.maximumFractionDigits = 0
    return when (currency.uppercase()) {
        "EUR" -> "€${formatter.format(price)}"
        "USD" -> "$${formatter.format(price)}"
        else -> "${formatter.format(price)} $currency"
    }
}

@Preview(showBackground = true)
@Composable
private fun PropertyDetailsScreenPreview() {
    MaterialTheme {
        PropertyDetailsScreen(
            state = PropertyDetailsState(
                property = PreviewPropertyData.sampleProperty,
                media = PreviewPropertyData.sampleMedia,
                isFavorite = false,
                isLoading = false
            ),
            onAction = {}
        )
    }
}

// Preview data
private object PreviewPropertyData {
    val sampleProperty = PropertyDetailsUiModel(
        id = "1",
        title = "Beautiful Modern Villa with Sea View",
        description = "Stunning modern villa featuring panoramic sea views, contemporary architecture, and premium finishes throughout. This exceptional property offers the perfect blend of luxury and comfort.",
        price = 450000.0,
        currency = "EUR",
        listingType = "Sale",
        propertyType = "Villa",
        address = "123 Seaside Boulevard",
        city = "Tirana",
        country = "Albania",
        bedrooms = 4,
        bathrooms = 3,
        squareFootage = 280,
        parkingSpaces = 2,
        agentName = "John Smith",
        agentPhone = "+355 69 123 4567",
        agentEmail = "john@example.com",
        amenities = emptyList()
    )

    val sampleMedia = PropertyMedia(
        videos = listOf(
            PropertyVideoState(
                id = "v1",
                url = "https://example.com/video.mp4",
                thumbnailUrl = "https://example.com/thumb.jpg",
                isMain = true,
                videoSource = VideoSource.DIRECT
            )
        ),
        images = listOf(
            PropertyImageState(id = "1", url = "https://example.com/1.jpg", caption = null, order = 0),
            PropertyImageState(id = "2", url = "https://example.com/2.jpg", caption = null, order = 1),
            PropertyImageState(id = "3", url = "https://example.com/3.jpg", caption = null, order = 2)
        )
    )
}
