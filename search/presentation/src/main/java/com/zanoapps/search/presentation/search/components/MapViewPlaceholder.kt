package com.zanoapps.search.presentation.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.HomeIcon
import com.zanoapps.search.domain.model.MapLocation
import com.zanoapps.search.domain.model.MockData
import kotlin.math.roundToInt

@Composable
fun MapViewPlaceholder(
    properties: List<BalkanEstateProperty>,
    mapLocation: MapLocation,
    isDrawModeEnabled: Boolean,
    onMarkerClick: (BalkanEstateProperty) -> Unit,
    onMapMove: (MapLocation) -> Unit,
    onDrawModeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE8E4D9)) // Map-like background color
    ) {
        // This is a placeholder for the actual map implementation
        // In production, you would use Google Maps SDK or similar
        
        // Simulated map background with grid lines
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Map background placeholder
            Text(
                text = "🗺️ Map View\nIntegrate with Google Maps SDK",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            // Property markers
            properties.forEach { property ->
                PropertyMarker(
                    property = property,
                    onClick = { onMarkerClick(property) },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(
                            x = ((property.longitude - mapLocation.longitude) * 100).dp,
                            y = ((mapLocation.latitude - property.latitude) * 100).dp
                        )
                )
            }
        }
        
        // Draw Mode FAB
        FloatingActionButton(
            onClick = onDrawModeToggle,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            containerColor = if (isDrawModeEnabled) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surface
            },
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 4.dp
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = HomeIcon,
                    contentDescription = "Draw",
                    tint = if (isDrawModeEnabled) {
                        Color.White
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Draw",
                    color = if (isDrawModeEnabled) {
                        Color.White
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        // Map Type Toggle FAB
        FloatingActionButton(
            onClick = { /* Toggle map type */ },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 4.dp
            )
        ) {
            Icon(
                imageVector = HomeIcon,
                contentDescription = "Map layers",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun PropertyMarker(
    property: BalkanEstateProperty,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val markerColor = getMarkerColor(property.price)
    val priceText = formatPrice(property.price)
    
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        color = markerColor,
        shadowElevation = 4.dp
    ) {
        Text(
            text = priceText,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}

private fun getMarkerColor(price: Double): Color {
    return when {
        price >= 1_000_000 -> Color(0xFF1976D2) // Blue for 1M+
        price >= 100_000 -> Color(0xFF4CAF50) // Green for 100K+
        price >= 50_000 -> Color(0xFF9C27B0) // Purple for 50K+
        else -> Color(0xFFFFA500) // Orange for less
    }
}

private fun formatPrice(price: Double): String {
    return when {
        price >= 1_000_000 -> "€${(price / 1_000_000).roundToInt()}M"
        price >= 1_000 -> "€${(price / 1_000).roundToInt()}K"
        else -> "€${price.roundToInt()}"
    }
}

@Preview(showBackground = true)
@Composable
private fun MapViewPlaceholderPreview() {
    BalkanEstateTheme {
        MapViewPlaceholder(
            properties = MockData.getMockProperties(),
            mapLocation = MapLocation(
                latitude = 41.3275,
                longitude = 19.8187,
                zoom = 12f
            ),
            isDrawModeEnabled = false,
            onMarkerClick = {},
            onMapMove = {},
            onDrawModeToggle = {}
        )
    }
}
