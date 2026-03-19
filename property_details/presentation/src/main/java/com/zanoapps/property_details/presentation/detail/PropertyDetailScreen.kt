package com.zanoapps.property_details.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.designsystem.BackIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BathroomsIcon
import com.zanoapps.core.presentation.designsystem.BedroomsIcon
import com.zanoapps.core.presentation.designsystem.EmailIcon
import com.zanoapps.core.presentation.designsystem.LoanHomeBottomBarIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.NotAddedToFavIcon
import com.zanoapps.core.presentation.designsystem.AddedToFavIcon
import com.zanoapps.core.presentation.designsystem.ParkingSpotIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.SharePropertyIcon
import com.zanoapps.core.presentation.designsystem.SquareMetersIcon
import com.zanoapps.core.presentation.designsystem.VirtualTourIcon
import com.zanoapps.core.presentation.designsystem.YearBuildIcon
import com.zanoapps.core.presentation.designsystem.components.PropertyCard
import androidx.compose.ui.tooling.preview.Preview
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat

@Composable
fun PropertyDetailScreenRoot(
    propertyId: String,
    viewModel: PropertyDetailViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToProperty: (String) -> Unit,
    onNavigateToMortgageCalculator: () -> Unit = {},
    onNavigateToAgentDetail: (String) -> Unit = {}
) {
    LaunchedEffect(propertyId) {
        viewModel.onAction(PropertyDetailAction.OnLoadProperty(propertyId))
    }

    PropertyDetailScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                PropertyDetailAction.OnBackClick -> onNavigateBack()
                is PropertyDetailAction.OnSimilarPropertyClick -> onNavigateToProperty(action.property.id)
                PropertyDetailAction.OnMortgageCalculatorClick -> onNavigateToMortgageCalculator()
                PropertyDetailAction.OnAgentCardClick -> {
                    val agentId = viewModel.state.property?.agentId
                    if (!agentId.isNullOrBlank()) {
                        onNavigateToAgentDetail(agentId)
                    }
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun PropertyDetailScreen(
    state: PropertyDetailState,
    onAction: (PropertyDetailAction) -> Unit
) {
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
        }
        return
    }

    val property = state.property ?: return

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF8FAFC))
        ) {
            // Image Header
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
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Top bar overlay
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onAction(PropertyDetailAction.OnBackClick) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f))
                    ) {
                        Icon(
                            imageVector = BackIcon,
                            contentDescription = "Back",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = { onAction(PropertyDetailAction.OnShareProperty) },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.9f))
                        ) {
                            Icon(
                                imageVector = SharePropertyIcon,
                                contentDescription = "Share",
                                tint = Color.DarkGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(
                            onClick = { onAction(PropertyDetailAction.OnToggleFavorite) },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.9f))
                        ) {
                            Icon(
                                imageVector = if (state.isFavorite) AddedToFavIcon else NotAddedToFavIcon,
                                contentDescription = "Favorite",
                                tint = if (state.isFavorite) Color.Red else BalkanEstateGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Listing type badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (property.listingType == "Sale") BalkanEstateGreen
                            else BalkanEstateOrange
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "For ${property.listingType}",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Price and Title Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = formatPrice(property.price),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = BalkanEstatePrimaryBlue
                )
                if (property.listingType == "Rent") {
                    Text(
                        text = "/month",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BalkanEstateGray
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = property.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = LocationIcon,
                        contentDescription = null,
                        tint = BalkanEstatePrimaryBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${property.address}, ${property.city}, ${property.country}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BalkanEstateGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Key Features
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (property.bedrooms > 0) {
                        FeatureItem(
                            icon = BedroomsIcon,
                            value = property.bedrooms.toString(),
                            label = "Beds"
                        )
                    }
                    if (property.bathrooms > 0) {
                        FeatureItem(
                            icon = BathroomsIcon,
                            value = property.bathrooms.toString(),
                            label = "Baths"
                        )
                    }
                    FeatureItem(
                        icon = SquareMetersIcon,
                        value = "${NumberFormat.getInstance().format(property.squareFootage)}",
                        label = "m\u00B2"
                    )
                    if (property.parking.isNotBlank()) {
                        FeatureItem(
                            icon = ParkingSpotIcon,
                            value = property.parking.split(" ").firstOrNull()?.takeIf { it.all { c -> c.isDigit() } } ?: "Yes",
                            label = "Parking"
                        )
                    }
                    if (property.yearBuilt > 0) {
                        FeatureItem(
                            icon = YearBuildIcon,
                            value = property.yearBuilt.toString(),
                            label = "Year"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = property.description.ifBlank {
                            "This beautiful ${property.propertyType.lowercase()} features ${property.bedrooms} bedrooms and ${property.bathrooms} bathrooms spread across ${NumberFormat.getInstance().format(property.squareFootage)} m\u00B2 of living space. Located in the heart of ${property.city}, this property offers modern amenities and easy access to local attractions."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = BalkanEstateGray,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Amenities
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Amenities & Features",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val amenities = listOf(
                        "Air Conditioning", "Central Heating", "Balcony",
                        "Garden", "Swimming Pool", "Garage",
                        "Elevator", "Security System", "Fireplace",
                        "Laundry Room", "Storage", "Pet Friendly"
                    )

                    val displayedAmenities = if (state.showAllAmenities) amenities
                    else amenities.take(6)

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        displayedAmenities.forEach { amenity ->
                            AmenityChip(amenity)
                        }
                    }

                    if (amenities.size > 6) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (state.showAllAmenities) "Show less" else "Show all ${amenities.size} amenities",
                            color = BalkanEstatePrimaryBlue,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable {
                                onAction(PropertyDetailAction.OnToggleShowAllAmenities)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Property Details Table
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Property Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailRow("Property Type", property.propertyType)
                    DetailRow("Listing Type", property.listingType)
                    DetailRow("City", property.city)
                    DetailRow("Country", property.country)
                    if (property.bedrooms > 0) DetailRow("Bedrooms", property.bedrooms.toString())
                    if (property.bathrooms > 0) DetailRow("Bathrooms", property.bathrooms.toString())
                    DetailRow("Area", "${NumberFormat.getInstance().format(property.squareFootage)} m\u00B2")
                    if (property.furnished.isNotBlank()) DetailRow("Furnished", property.furnished)
                    if (property.parking.isNotBlank()) DetailRow("Parking", property.parking)
                    if (property.yearBuilt > 0) DetailRow("Year Built", property.yearBuilt.toString())
                    if (property.floorNumber > 0) DetailRow("Floor", "${property.floorNumber} / ${property.totalFloors}")
                    DetailRow("Currency", property.currency)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Agent Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .clickable { onAction(PropertyDetailAction.OnAgentCardClick) }
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Listed by",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(BalkanEstatePrimaryBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = property.agentName.take(1),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = property.agentName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Licensed Real Estate Agent",
                                style = MaterialTheme.typography.bodySmall,
                                color = BalkanEstateGray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onAction(PropertyDetailAction.OnCallAgent(property.agentPhone)) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Call", fontSize = 13.sp)
                        }
                        OutlinedButton(
                            onClick = { onAction(PropertyDetailAction.OnEmailAgent(property.agentEmail)) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = EmailIcon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Email", fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Virtual Tour & Actions
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Explore",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ActionCard(
                            icon = VirtualTourIcon,
                            label = "Virtual Tour",
                            onClick = { onAction(PropertyDetailAction.OnVirtualTourClick) },
                            modifier = Modifier.weight(1f)
                        )
                        ActionCard(
                            icon = LocationIcon,
                            label = "Directions",
                            onClick = { onAction(PropertyDetailAction.OnGetDirectionsClick) },
                            modifier = Modifier.weight(1f)
                        )
                        ActionCard(
                            icon = PersonIcon,
                            label = "Schedule Tour",
                            onClick = { onAction(PropertyDetailAction.OnScheduleTourClick) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ActionCard(
                        icon = LoanHomeBottomBarIcon,
                        label = "Mortgage Calculator",
                        onClick = { onAction(PropertyDetailAction.OnMortgageCalculatorClick) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Similar Properties
            if (state.similarProperties.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Similar Properties",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(end = 16.dp)
                    ) {
                        items(
                            items = state.similarProperties,
                            key = { it.id }
                        ) { similarProperty ->
                            PropertyCard(
                                property = similarProperty,
                                onPropertyClick = {
                                    onAction(PropertyDetailAction.OnSimilarPropertyClick(it))
                                },
                                onViewDetailsClick = {
                                    onAction(PropertyDetailAction.OnSimilarPropertyClick(it))
                                },
                                modifier = Modifier.width(300.dp)
                            )
                        }
                    }
                }
            }

            // Bottom spacing for the contact button
            Spacer(modifier = Modifier.height(80.dp))
        }

        // Fixed bottom contact button
        Button(
            onClick = { onAction(PropertyDetailAction.OnContactAgentClick) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = EmailIcon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Contact Agent",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Contact Agent Bottom Sheet
        if (state.isContactAgentSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { onAction(PropertyDetailAction.OnDismissContactSheet) },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                ContactAgentSheet(
                    state = state,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
private fun ContactAgentSheet(
    state: PropertyDetailState,
    onAction: (PropertyDetailAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Contact Agent",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
        Text(
            text = state.property?.agentName ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = BalkanEstateGray
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.contactName,
            onValueChange = { onAction(PropertyDetailAction.OnContactNameChanged(it)) },
            label = { Text("Your Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BalkanEstatePrimaryBlue
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.contactEmail,
            onValueChange = { onAction(PropertyDetailAction.OnContactEmailChanged(it)) },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BalkanEstatePrimaryBlue
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.contactPhone,
            onValueChange = { onAction(PropertyDetailAction.OnContactPhoneChanged(it)) },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BalkanEstatePrimaryBlue
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.contactMessage,
            onValueChange = { onAction(PropertyDetailAction.OnContactMessageChanged(it)) },
            label = { Text("Message") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BalkanEstatePrimaryBlue
            ),
            maxLines = 5
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onAction(PropertyDetailAction.OnSendMessage) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
            shape = RoundedCornerShape(12.dp),
            enabled = !state.isSendingMessage
        ) {
            if (state.isSendingMessage) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Send Message",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun FeatureItem(
    icon: ImageVector,
    value: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BalkanEstatePrimaryBlue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.DarkGray
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = BalkanEstateGray
        )
    }
}

@Composable
private fun AmenityChip(amenity: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, BalkanEstatePrimaryBlue.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            .background(BalkanEstatePrimaryBlue.copy(alpha = 0.05f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = amenity,
            fontSize = 12.sp,
            color = BalkanEstatePrimaryBlue,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = BalkanEstateGray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
    }
    HorizontalDivider(
        color = Color(0xFFF1F5F9),
        thickness = 1.dp
    )
}

@Composable
private fun ActionCard(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BalkanEstatePrimaryBlue.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BalkanEstatePrimaryBlue,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = BalkanEstatePrimaryBlue,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun formatPrice(price: Double): String {
    val formatter = NumberFormat.getNumberInstance()
    formatter.maximumFractionDigits = 0
    return "${formatter.format(price).replace(",", ".")} \u20AC"
}

@Preview
@Composable
private fun PropertyDetailScreenPreview() {
    BalkanEstateTheme {
        PropertyDetailScreen(
            state = PropertyDetailState(
                property = BalkanEstateProperty(
                    id = "1",
                    title = "Modern Apartment in Skopje",
                    price = 120000.0,
                    currency = "EUR",
                    imageUrl = "",
                    bedrooms = 3,
                    bathrooms = 2,
                    squareFootage = 95,
                    address = "ul. Makedonija 10",
                    city = "Skopje",
                    country = "North Macedonia",
                    latitude = 41.9973,
                    longitude = 21.4280,
                    propertyType = "Apartment",
                    listingType = "Sale",
                    agentName = "Marko Petrovic",
                    agentPhone = "+389 70 123 456",
                    agentEmail = "marko@example.com"
                )
            ),
            onAction = {}
        )
    }
}
