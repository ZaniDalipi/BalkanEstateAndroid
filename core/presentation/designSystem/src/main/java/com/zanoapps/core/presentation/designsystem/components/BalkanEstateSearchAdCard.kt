package com.zanoapps.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.designsystem.AddedToFavIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.BathroomsIcon
import com.zanoapps.core.presentation.designsystem.BedroomsIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.NotAddedToFavIcon
import com.zanoapps.core.presentation.designsystem.ParkingSpotIcon
import com.zanoapps.core.presentation.designsystem.SquareMetersIcon
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

data class PropertyCardData(
    val id: String,
    val price: Double,
    val currency: String,
    val bedrooms: Int,
    val bathrooms: Int,
    val squareFootage: Int,
    val address: String,
    val imageUrl: String,
    val agentName: String? = null,
    val agentImageUrl: String? = null,
    val agentType: String? = null,
    val parking: Int = 0,
    val isNew: Boolean = false,
    val propertyType: String = "",
    val title: String = "",
    val city: String = "",
    val country: String = ""
)

@Composable
fun PropertyCard(
    property: BalkanEstateProperty,
    isFavorite: Boolean = false,
    isNew: Boolean = true,
    onPropertyClick: (BalkanEstateProperty) -> Unit = {},
    onFavoriteClick: (BalkanEstateProperty) -> Unit = {},
    onViewDetailsClick: (BalkanEstateProperty) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onPropertyClick(property) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 6.dp
        )
    ) {
        Column {
            // Image section with badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 10f)
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

                // NEW badge (green with dot)
                if (isNew) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(BalkanEstateGreen)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "NEW",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Favorite button (white circle)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { onFavoriteClick(property) },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) AddedToFavIcon else NotAddedToFavIcon,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (isFavorite) BalkanEstateRed else BalkanEstateGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Property type badge (bottom left)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = property.propertyType,
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Price badge (bottom right, blue)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(BalkanEstatePrimaryBlue)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = formatPriceEuropean(property.price),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Title
                Text(
                    text = property.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Location with icon
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = LocationIcon,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${property.city} , ${property.country}",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Features row with bordered boxes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PropertyFeatureBox(
                        icon = BedroomsIcon,
                        value = property.bedrooms.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    PropertyFeatureBox(
                        icon = BathroomsIcon,
                        value = property.bathrooms.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    PropertyFeatureBox(
                        icon = ParkingSpotIcon,
                        value = "1", // Default parking
                        modifier = Modifier.weight(1f)
                    )
                    PropertyFeatureBox(
                        icon = SquareMetersIcon,
                        value = property.squareFootage.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Agent info row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Agent avatar
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://via.placeholder.com/32")
                                .crossfade(true)
                                .build(),
                            contentDescription = "Agent avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Agent name
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = property.agentName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }

                    // Agent type badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF5F5F5))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Private Seller",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PropertyFeatureBox(
    icon: ImageVector,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE8F4EA),
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color(0xFFF8FBF9))
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BalkanEstateGray,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }
}

// European price format: 4.500.000 €
private fun formatPriceEuropean(price: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale.GERMANY)
    formatter.maximumFractionDigits = 0
    return "${formatter.format(price)} €"
}

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
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 6.dp
        )
    ) {
        Column {
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

                if (property.isNew) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(BalkanEstateGreen)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "NEW",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { onFavoriteClick(property) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) AddedToFavIcon else NotAddedToFavIcon,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (isFavorite) BalkanEstateRed else BalkanEstateGray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Property type badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = property.propertyType,
                        color = Color.Black,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Price badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(BalkanEstatePrimaryBlue)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = formatPriceEuropean(property.price),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = property.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = LocationIcon,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${property.city}, ${property.country}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
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
            property = BalkanEstateProperty(
                id = "1",
                title = "Villë luksoze",
                price = 4500000.0,
                currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914",
                bedrooms = 4,
                bathrooms = 4,
                squareFootage = 350,
                address = "Rruga e Kavajës",
                city = "Tirana",
                country = "Albania",
                latitude = 41.3275,
                longitude = 19.8187,
                propertyType = "Villa",
                listingType = "Sale",
                agentName = "zanoin",
                isFeatured = true,
                isUrgent = false
            ),
            isFavorite = false,
            isNew = true,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun PropertyCardFavoritePreview() {
    MaterialTheme {
        PropertyCard(
            property = BalkanEstateProperty(
                id = "2",
                title = "Amazing House In Struga",
                price = 25000.0,
                currency = "EUR",
                imageUrl = "https://images.unsplash.com/photo-1512917774080-9991f1c4c750",
                bedrooms = 3,
                bathrooms = 2,
                squareFootage = 120,
                address = "Lake Side Road",
                city = "Struga",
                country = "North Macedonia",
                latitude = 41.1780,
                longitude = 20.6830,
                propertyType = "House",
                listingType = "Sale",
                agentName = "Agent Name",
                isFeatured = false,
                isUrgent = false
            ),
            isFavorite = true,
            isNew = true,
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
                squareFootage = 120,
                address = "Downtown",
                imageUrl = "https://example.com/compact.jpg",
                isNew = true,
                propertyType = "Apartment",
                title = "Modern Apartment",
                city = "Tirana",
                country = "Albania"
            ),
            isFavorite = false,
            modifier = Modifier.padding(8.dp)
        )
    }
}
