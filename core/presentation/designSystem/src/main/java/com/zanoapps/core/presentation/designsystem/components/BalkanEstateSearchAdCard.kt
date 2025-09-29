package com.zanoapps.core.presentation.designsystem.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zanoapps.core.presentation.designsystem.BathroomsIcon
import com.zanoapps.core.presentation.designsystem.BedroomsIcon
import com.zanoapps.core.presentation.designsystem.SquareMetersIcon
import java.text.NumberFormat
import java.util.Currency

data class PropertyCardData(
    val id: String,
    val price: Double,
    val currency: String,
    val bedrooms: Int,
    val bathrooms: Int,
    val squareFootage: Int,
    val address: String,
    val imageUrl: String,
    val agentName: String? = null
)

@Composable
fun PropertyCard(
    property: PropertyCardData,
    isFavorite: Boolean = false,
    onPropertyClick: (PropertyCardData) -> Unit = {},
    onFavoriteClick: (PropertyCardData) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onPropertyClick(property) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 10.dp
        )
    ) {
        Column {
            // Property Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(property.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Property image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )

                // Favorite Button with better styling
                IconButton(
                    onClick = { onFavoriteClick(property) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (isFavorite) Color(0xFFE53E3E) else Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Property Details Section
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Price - more prominent styling
                Text(
                    text = formatPrice(property.price, property.currency),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Property Features Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (property.bedrooms > 0) {
                        PropertyFeature(
                            icon = BedroomsIcon,
                            value = property.bedrooms.toString(),
                            label = if (property.bedrooms == 1) "bed" else "beds"
                        )
                    }

                    if (property.bathrooms > 0) {
                        PropertyFeature(
                            icon = BathroomsIcon,
                            value = property.bathrooms.toString(),
                            label = if (property.bathrooms == 1) "bath" else "baths"
                        )
                    }

                    if (property.squareFootage > 0) {
                        PropertyFeature(
                            icon = SquareMetersIcon,
                            value = formatSquareFootage(property.squareFootage),
                            label = "sqft"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Address
                Text(
                    text = property.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                )

                // Agent info if available
                property.agentName?.let { agentName ->
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Agent: $agentName",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun PropertyFeature(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "$value $label",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }
}

// Utility functions
private fun formatPrice(price: Double, currencyCode: String): String {
    return try {
        val formatter = NumberFormat.getCurrencyInstance()
        formatter.currency = Currency.getInstance(currencyCode)
        formatter.maximumFractionDigits = 0
        formatter.format(price)
    } catch (e: Exception) {
        when (currencyCode.uppercase()) {
            "EUR" -> "€${formatNumber(price)}"
            "USD" -> "$${formatNumber(price)}"
            "RSD" -> "${formatNumber(price)} RSD"
            else -> "${formatNumber(price)} $currencyCode"
        }
    }
}

private fun formatSquareFootage(sqft: Int): String {
    return NumberFormat.getInstance().format(sqft)
}

private fun formatNumber(number: Double): String {
    return NumberFormat.getInstance().format(number.toLong())
}

// Compact version for grid layouts
@Composable
fun PropertyCardCompact(
    property: PropertyCardData,
    isFavorite: Boolean = false,
    onPropertyClick: (PropertyCardData) -> Unit = {},
    onFavoriteClick: (PropertyCardData) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onPropertyClick(property) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 6.dp
        )
    ) {
        Column {
            // Property Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(property.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Property image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = { onFavoriteClick(property) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (isFavorite) Color(0xFFE53E3E) else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Compact Details
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = formatPrice(property.price, property.currency),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Compact features
                Text(
                    text = "${property.bedrooms} beds • ${property.bathrooms} baths",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = property.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// Previews
@Preview(showBackground = true, widthDp = 360)
@Composable
private fun PropertyCardPreview() {
    MaterialTheme {
        PropertyCard(
            property = PropertyCardData(
                id = "1",
                price = 417500000000.0,
                currency = "LEK",
                bedrooms = 3,
                bathrooms = 2,
                squareFootage = 1800,
                address = "Rruga Myslym Shyri 27, Tirana",
                imageUrl = "https://example.com/property.jpg",
                agentName = "Besmir Kola"
            ),
            isFavorite = false,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun PropertyCardFavoritePreview() {
    MaterialTheme {
        PropertyCard(
            property = PropertyCardData(
                id = "2",
                price = 450000.0,
                currency = "EUR",
                bedrooms = 4,
                bathrooms = 3,
                squareFootage = 2000,
                address = "456 European Avenue, Belgrade, Serbia",
                imageUrl = "https://example.com/apartment.jpg"
            ),
            isFavorite = true,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 180)
@Composable
private fun PropertyCardCompactPreview() {
    MaterialTheme {
        PropertyCardCompact(
            property = PropertyCardData(
                id = "3",
                price = 250000.0,
                currency = "EUR",
                bedrooms = 2,
                bathrooms = 1,
                squareFootage = 1200,
                address = "Downtown Apartment Complex",
                imageUrl = "https://example.com/compact.jpg"
            ),
            isFavorite = false,
            modifier = Modifier.padding(8.dp)
        )
    }
}