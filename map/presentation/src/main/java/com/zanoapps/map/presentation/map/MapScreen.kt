package com.zanoapps.map.presentation.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateBackground
import com.zanoapps.core.presentation.designsystem.BalkanEstateCardBackground
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTextPrimary
import com.zanoapps.core.presentation.designsystem.BalkanEstateTextSecondary
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.CrossIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.map.domain.model.MapProperty
import com.zanoapps.map.domain.model.MapRegion
import com.zanoapps.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreenRoot(
    viewModel: MapViewModel = koinViewModel(),
    onNavigateToPropertyDetail: (String) -> Unit
) {
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is MapEvent.Error -> Unit
            is MapEvent.NavigateToPropertyDetail -> {
                onNavigateToPropertyDetail(event.propertyId)
            }
        }
    }

    MapScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun MapScreen(
    state: MapState,
    onAction: (MapAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BalkanEstateBackground)
    ) {
        // Map placeholder area
        MapPlaceholder(
            properties = state.properties,
            selectedProperty = state.selectedProperty,
            onPropertySelected = { propertyId ->
                onAction(MapAction.OnPropertySelected(propertyId))
            },
            modifier = Modifier.fillMaxSize()
        )

        // Loading indicator
        if (state.isLoading) {
            CircularProgressIndicator(
                color = BalkanEstatePrimaryBlue,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
            )
        }

        // Bottom property cards
        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            // Selected property detail card
            AnimatedVisibility(
                visible = state.selectedProperty != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                state.selectedProperty?.let { property ->
                    SelectedPropertyCard(
                        property = property,
                        onDismiss = { onAction(MapAction.OnDismissPropertyCard) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            // Horizontally scrollable property cards
            if (state.properties.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        items = state.properties,
                        key = { it.id }
                    ) { property ->
                        PropertyMarkerCard(
                            property = property,
                            isSelected = state.selectedProperty?.id == property.id,
                            onClick = {
                                onAction(MapAction.OnPropertySelected(property.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MapPlaceholder(
    properties: List<MapProperty>,
    selectedProperty: MapProperty?,
    onPropertySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.background(Color(0xFFE8F0FE))) {
        // Draw a simple map-like background with canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawMapBackground(this)
        }

        // Place property pins on the map area
        properties.forEach { property ->
            val isSelected = selectedProperty?.id == property.id

            // Normalize lat/lng to screen position (approximate placement)
            val normalizedX = ((property.longitude - 15.0) / 15.0).coerceIn(0.05, 0.95)
            val normalizedY = (1.0 - (property.latitude - 39.0) / 8.0).coerceIn(0.05, 0.85)

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                PropertyPin(
                    property = property,
                    isSelected = isSelected,
                    onClick = { onPropertySelected(property.id) },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(
                            x = (normalizedX * 350).dp,
                            y = (normalizedY * 500).dp
                        )
                )
            }
        }

        // Map label
        Text(
            text = "Balkan Region",
            style = MaterialTheme.typography.titleMedium,
            color = BalkanEstateTextSecondary,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
    }
}

private fun drawMapBackground(drawScope: DrawScope) {
    with(drawScope) {
        // Draw grid lines to simulate a map
        val gridColor = Color(0xFFD0D8E8)
        val gridSpacing = 60.dp.toPx()

        // Vertical lines
        var x = 0f
        while (x < size.width) {
            drawLine(
                color = gridColor,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 1f
            )
            x += gridSpacing
        }

        // Horizontal lines
        var y = 0f
        while (y < size.height) {
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )
            y += gridSpacing
        }

        // Draw some landmass-like shapes
        val landColor = Color(0xFFC8E6C9)
        val waterColor = Color(0xFFBBDEFB)

        // Adriatic sea area (left side)
        drawRect(
            color = waterColor,
            topLeft = Offset(0f, size.height * 0.3f),
            size = androidx.compose.ui.geometry.Size(size.width * 0.15f, size.height * 0.5f)
        )

        // Land masses
        drawCircle(
            color = landColor,
            radius = size.width * 0.12f,
            center = Offset(size.width * 0.3f, size.height * 0.4f)
        )
        drawCircle(
            color = landColor,
            radius = size.width * 0.15f,
            center = Offset(size.width * 0.6f, size.height * 0.35f)
        )
        drawCircle(
            color = landColor,
            radius = size.width * 0.1f,
            center = Offset(size.width * 0.8f, size.height * 0.5f)
        )
    }
}

@Composable
private fun PropertyPin(
    property: MapProperty,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .shadow(
                    elevation = if (isSelected) 8.dp else 4.dp,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(
                    color = if (isSelected) BalkanEstatePrimaryBlue else Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = formatPrice(property.price, property.currency),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else BalkanEstatePrimaryBlue
            )
        }
        // Pin triangle
        Canvas(modifier = Modifier.size(8.dp)) {
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width / 2f, size.height)
                close()
            }
            drawPath(
                path = path,
                color = if (isSelected) BalkanEstatePrimaryBlue else Color.White
            )
        }
    }
}

@Composable
private fun PropertyMarkerCard(
    property: MapProperty,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(220.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) BalkanEstatePrimaryBlue else BalkanEstateCardBackground
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = property.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) Color.White else BalkanEstateTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = formatPrice(property.price, property.currency),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else BalkanEstatePrimaryBlue
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Property type badge
                Box(
                    modifier = Modifier
                        .background(
                            color = if (isSelected) Color.White.copy(alpha = 0.2f) else BalkanEstatePrimaryBlue.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = property.propertyType,
                        fontSize = 10.sp,
                        color = if (isSelected) Color.White else BalkanEstatePrimaryBlue
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Bedrooms
                Text(
                    text = "${property.bedrooms} bd",
                    fontSize = 11.sp,
                    color = if (isSelected) Color.White.copy(alpha = 0.8f) else BalkanEstateTextSecondary
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "${property.bathrooms} ba",
                    fontSize = 11.sp,
                    color = if (isSelected) Color.White.copy(alpha = 0.8f) else BalkanEstateTextSecondary
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "${property.area.toInt()} m\u00B2",
                    fontSize = 11.sp,
                    color = if (isSelected) Color.White.copy(alpha = 0.8f) else BalkanEstateTextSecondary
                )
            }
        }
    }
}

@Composable
private fun SelectedPropertyCard(
    property: MapProperty,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = BalkanEstateCardBackground)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = property.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BalkanEstateTextPrimary,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = CrossIcon,
                        contentDescription = "Dismiss",
                        tint = BalkanEstateGray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = formatPrice(property.price, property.currency),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = BalkanEstatePrimaryBlue
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Property type
                DetailChip(label = property.propertyType)

                // Bedrooms
                DetailChip(label = "${property.bedrooms} Bedrooms")

                // Bathrooms
                DetailChip(label = "${property.bathrooms} Bathrooms")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = LocationIcon,
                    contentDescription = null,
                    tint = BalkanEstateTextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${property.area.toInt()} m\u00B2",
                    fontSize = 13.sp,
                    color = BalkanEstateTextSecondary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Lat: ${"%.4f".format(property.latitude)}, Lng: ${"%.4f".format(property.longitude)}",
                    fontSize = 11.sp,
                    color = BalkanEstateTextSecondary
                )
            }
        }
    }
}

@Composable
private fun DetailChip(
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = BalkanEstatePrimaryBlue.copy(alpha = 0.1f),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = BalkanEstatePrimaryBlue,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun formatPrice(price: Double, currency: String): String {
    val formatted = when {
        price >= 1_000_000 -> "${"%.1f".format(price / 1_000_000)}M"
        price >= 1_000 -> "${(price / 1_000).toInt()}K"
        else -> price.toInt().toString()
    }
    return "\u20AC$formatted"
}

@Preview(showBackground = true)
@Composable
private fun MapScreenPreview() {
    BalkanEstateTheme {
        MapScreen(
            state = MapState(
                properties = listOf(
                    MapProperty(
                        id = "mp1",
                        title = "Modern Apartment in Skopje",
                        price = 85000.0,
                        latitude = 41.9973,
                        longitude = 21.4280,
                        propertyType = "Apartment",
                        bedrooms = 2,
                        bathrooms = 1,
                        area = 75.0
                    ),
                    MapProperty(
                        id = "mp2",
                        title = "Lakeside Villa in Ohrid",
                        price = 250000.0,
                        latitude = 41.1231,
                        longitude = 20.8016,
                        propertyType = "Villa",
                        bedrooms = 4,
                        bathrooms = 3,
                        area = 220.0
                    ),
                    MapProperty(
                        id = "mp3",
                        title = "Studio in Belgrade Center",
                        price = 120000.0,
                        latitude = 44.8176,
                        longitude = 20.4633,
                        propertyType = "Studio",
                        bedrooms = 1,
                        bathrooms = 1,
                        area = 45.0
                    )
                ),
                selectedProperty = null,
                region = MapRegion(),
                isLoading = false
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MapScreenWithSelectedPropertyPreview() {
    val selectedProperty = MapProperty(
        id = "mp2",
        title = "Lakeside Villa in Ohrid",
        price = 250000.0,
        latitude = 41.1231,
        longitude = 20.8016,
        propertyType = "Villa",
        bedrooms = 4,
        bathrooms = 3,
        area = 220.0
    )

    BalkanEstateTheme {
        MapScreen(
            state = MapState(
                properties = listOf(
                    MapProperty(
                        id = "mp1",
                        title = "Modern Apartment in Skopje",
                        price = 85000.0,
                        latitude = 41.9973,
                        longitude = 21.4280,
                        propertyType = "Apartment",
                        bedrooms = 2,
                        bathrooms = 1,
                        area = 75.0
                    ),
                    selectedProperty
                ),
                selectedProperty = selectedProperty,
                region = MapRegion(),
                isLoading = false
            ),
            onAction = {}
        )
    }
}
