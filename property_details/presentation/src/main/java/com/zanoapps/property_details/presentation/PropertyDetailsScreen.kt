package com.zanoapps.property_details.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.BathroomIcon
import com.zanoapps.core.presentation.designsystem.BedIcon
import com.zanoapps.core.presentation.designsystem.CalendarIcon
import com.zanoapps.core.presentation.designsystem.HeartFilledIcon
import com.zanoapps.core.presentation.designsystem.HeartOutlineIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.MailIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.PhoneIcon
import com.zanoapps.core.presentation.designsystem.Poppins
import com.zanoapps.core.presentation.designsystem.SquareFootIcon
import com.zanoapps.core.presentation.designsystem.VerifiedIcon
import com.zanoapps.core.presentation.designsystem.components.MockData

@Composable
fun PropertyDetailsScreenRoot(
    property: BalkanEstateProperty,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    onContactAgentClick: () -> Unit,
    onCallAgentClick: () -> Unit,
    onScheduleViewingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PropertyDetailsScreen(
        property = property,
        isFavorite = isFavorite,
        onBackClick = onBackClick,
        onFavoriteClick = onFavoriteClick,
        onShareClick = onShareClick,
        onContactAgentClick = onContactAgentClick,
        onCallAgentClick = onCallAgentClick,
        onScheduleViewingClick = onScheduleViewingClick,
        modifier = modifier
    )
}

@Composable
fun PropertyDetailsScreen(
    property: BalkanEstateProperty,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    onContactAgentClick: () -> Unit,
    onCallAgentClick: () -> Unit,
    onScheduleViewingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Image Section with overlay controls
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(Color.LightGray)
            ) {
                // Placeholder for property image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BalkanEstatePrimaryBlue.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Property Image",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Top action bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.9f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.DarkGray
                        )
                    }

                    Row {
                        IconButton(
                            onClick = onFavoriteClick,
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.9f), CircleShape)
                        ) {
                            Icon(
                                imageVector = if (isFavorite) HeartFilledIcon else HeartOutlineIcon,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) Color.Red else Color.DarkGray
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = onShareClick,
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.9f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.DarkGray
                            )
                        }
                    }
                }

                // Image counter badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "1/${property.imageUrls.size.coerceAtLeast(1)} photos",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            // Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Property Info Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Property Type Badge
                        Box(
                            modifier = Modifier
                                .background(
                                    BalkanEstatePrimaryBlue.copy(alpha = 0.1f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = property.propertyType.uppercase(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BalkanEstatePrimaryBlue
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Title
                        Text(
                            text = property.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                            fontFamily = Poppins
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Location
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
                                text = "${property.location}, ${property.country}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Price
                        Text(
                            text = "€${"%,.0f".format(property.price)}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = BalkanEstatePrimaryBlue,
                            fontFamily = Poppins
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                        Spacer(modifier = Modifier.height(20.dp))

                        // Property Features
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            PropertyFeature(
                                icon = BedIcon,
                                value = property.beds.toString(),
                                label = "Beds"
                            )
                            PropertyFeature(
                                icon = BathroomIcon,
                                value = property.baths.toString(),
                                label = "Baths"
                            )
                            PropertyFeature(
                                icon = SquareFootIcon,
                                value = "${property.sqm} m²",
                                label = "Area"
                            )
                        }
                    }
                }

                // Description Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Description",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = property.description.ifEmpty {
                                "Beautiful ${property.propertyType.lowercase()} located in ${property.location}, ${property.country}. " +
                                "This property features ${property.beds} bedrooms and ${property.baths} bathrooms, " +
                                "with a total area of ${property.sqm} square meters. " +
                                "Perfect for families looking for a comfortable home in a great location."
                            },
                            fontSize = 14.sp,
                            color = Color.Gray,
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Amenities Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Amenities",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Sample amenities
                        val amenities = listOf(
                            "Parking", "Elevator", "Balcony", "Central Heating",
                            "Air Conditioning", "Security System", "Garden", "Pool"
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            amenities.chunked(2).forEach { row ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    row.forEach { amenity ->
                                        AmenityChip(
                                            text = amenity,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    if (row.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Agent Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Listed by",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Agent Avatar
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = PersonIcon,
                                    contentDescription = null,
                                    tint = BalkanEstatePrimaryBlue,
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "John Doe",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.DarkGray
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = VerifiedIcon,
                                        contentDescription = "Verified",
                                        tint = BalkanEstateGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "Balkan Properties Group",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = onCallAgentClick,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = BalkanEstatePrimaryBlue
                                )
                            ) {
                                Icon(
                                    imageVector = PhoneIcon,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Call",
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Button(
                                onClick = onContactAgentClick,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BalkanEstatePrimaryBlue
                                )
                            ) {
                                Icon(
                                    imageVector = MailIcon,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Message",
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Schedule Viewing Button
                Button(
                    onClick = onScheduleViewingClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BalkanEstateGreen
                    )
                ) {
                    Icon(
                        imageVector = CalendarIcon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Schedule a Viewing",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun PropertyFeature(
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
            tint = BalkanEstatePrimaryBlue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun AmenityChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Color(0xFFF0F0F0),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PropertyDetailsScreenPreview() {
    BalkanEstateTheme {
        PropertyDetailsScreen(
            property = MockData.getMockProperties().first(),
            isFavorite = false,
            onBackClick = {},
            onFavoriteClick = {},
            onShareClick = {},
            onContactAgentClick = {},
            onCallAgentClick = {},
            onScheduleViewingClick = {}
        )
    }
}
