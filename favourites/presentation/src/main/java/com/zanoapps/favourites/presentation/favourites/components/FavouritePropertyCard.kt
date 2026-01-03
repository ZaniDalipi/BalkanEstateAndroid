package com.zanoapps.favourites.presentation.favourites.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.BathroomsIcon
import com.zanoapps.core.presentation.designsystem.BedroomsIcon
import com.zanoapps.core.presentation.designsystem.CrossIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.SquareMetersIcon
import com.zanoapps.favourites.presentation.R
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FavouritePropertyCard(
    property: BalkanEstateProperty,
    onPropertyClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onPropertyClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Property Image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(property.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = property.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Property Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Title and Remove Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = property.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = onRemoveClick,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = CrossIcon,
                            contentDescription = stringResource(R.string.remove_from_favourites),
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Price
                Text(
                    text = formatPrice(property.price, property.currency),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BalkanEstatePrimaryBlue
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Location
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = LocationIcon,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${property.city}, ${property.country}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Features
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PropertyFeature(
                        icon = {
                            Icon(
                                imageVector = BedroomsIcon,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        value = "${property.bedrooms}"
                    )
                    PropertyFeature(
                        icon = {
                            Icon(
                                imageVector = BathroomsIcon,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        value = "${property.bathrooms}"
                    )
                    PropertyFeature(
                        icon = {
                            Icon(
                                imageVector = SquareMetersIcon,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        value = "${property.squareFootage}"
                    )
                }
            }
        }
    }
}

@Composable
private fun PropertyFeature(
    icon: @Composable () -> Unit,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

private fun formatPrice(price: Double, currency: String): String {
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    return "$currency${formatter.format(price.toLong())}"
}

@Preview(showBackground = true)
@Composable
private fun FavouritePropertyCardPreview() {
    BalkanEstateTheme {
        FavouritePropertyCard(
            property = BalkanEstateProperty(
                id = "1",
                title = "Modern Apartment in City Center",
                price = 250000.0,
                currency = "$",
                imageUrl = "https://example.com/image.jpg",
                bedrooms = 3,
                bathrooms = 2,
                squareFootage = 1500,
                address = "123 Main Street",
                city = "Tirana",
                country = "Albania",
                latitude = 41.3275,
                longitude = 19.8187,
                propertyType = "Apartment",
                listingType = "Sale",
                agentName = "John Doe",
                isFeatured = true,
                isUrgent = false
            ),
            onPropertyClick = {},
            onRemoveClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
