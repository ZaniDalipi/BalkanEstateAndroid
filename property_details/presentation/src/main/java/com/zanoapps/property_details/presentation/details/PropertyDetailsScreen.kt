package com.zanoapps.property_details.presentation.details

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.BathroomsIcon
import com.zanoapps.core.presentation.designsystem.BedroomsIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.NotAddedToFavIcon
import com.zanoapps.core.presentation.designsystem.ParkingSpotIcon
import com.zanoapps.core.presentation.designsystem.SquareMetersIcon
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat

@Composable
fun PropertyDetailsScreenRoot(
    propertyId: String,
    viewModel: PropertyDetailsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToProperty: (String) -> Unit
) {
    viewModel.onAction(PropertyDetailsAction.LoadProperty(propertyId))

    PropertyDetailsScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                PropertyDetailsAction.OnBackClick -> onNavigateBack()
                is PropertyDetailsAction.OnSimilarPropertyClick -> onNavigateToProperty(action.property.id)
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PropertyDetailsScreen(
    state: PropertyDetailsState,
    onAction: (PropertyDetailsAction) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Property Details") },
                navigationIcon = {
                    IconButton(onClick = { onAction(PropertyDetailsAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onAction(PropertyDetailsAction.OnShareClick) }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                    IconButton(onClick = { onAction(PropertyDetailsAction.OnFavoriteToggle) }) {
                        Icon(
                            imageVector = if (state.isFavorite) AddedToFavIcon else NotAddedToFavIcon,
                            contentDescription = if (state.isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (state.isFavorite) Color.Red else BalkanEstateGray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            state.property?.let { property ->
                ContactBottomBar(
                    price = property.price,
                    currency = property.currency,
                    onContactClick = { onAction(PropertyDetailsAction.OnContactAgentClick) },
                    onScheduleClick = { onAction(PropertyDetailsAction.OnScheduleVisitClick) }
                )
            }
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
            }
        } else {
            state.property?.let { property ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Image Gallery
                    PropertyImageGallery(
                        imageUrl = property.imageUrl,
                        currentIndex = state.currentImageIndex,
                        onImageClick = { onAction(PropertyDetailsAction.OnToggleImageFullscreen) }
                    )

                    // Property Info
                    PropertyInfoSection(property = property)

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    // Features
                    PropertyFeaturesSection(property = property)

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    // Description
                    PropertyDescriptionSection()

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    // Location
                    PropertyLocationSection(
                        property = property,
                        onViewMapClick = { onAction(PropertyDetailsAction.OnViewOnMapClick) }
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    // Agent Info
                    AgentInfoSection(
                        agentName = property.agentName,
                        onContactClick = { onAction(PropertyDetailsAction.OnContactAgentClick) }
                    )

                    // Similar Properties
                    if (state.similarProperties.isNotEmpty()) {
                        SimilarPropertiesSection(
                            properties = state.similarProperties,
                            onPropertyClick = { onAction(PropertyDetailsAction.OnSimilarPropertyClick(it)) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // Contact Agent Bottom Sheet
    if (state.isContactAgentDialogVisible) {
        ModalBottomSheet(
            onDismissRequest = { onAction(PropertyDetailsAction.OnDismissContactDialog) },
            sheetState = sheetState
        ) {
            ContactAgentBottomSheet(
                agentName = state.property?.agentName ?: "Agent",
                onCallClick = { onAction(PropertyDetailsAction.OnCallAgent("+355691234567")) },
                onEmailClick = { onAction(PropertyDetailsAction.OnEmailAgent("agent@balkanestate.com")) },
                onWhatsAppClick = { onAction(PropertyDetailsAction.OnWhatsAppAgent("+355691234567")) }
            )
        }
    }
}

@Composable
private fun PropertyImageGallery(
    imageUrl: String,
    currentIndex: Int,
    onImageClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 10f)
            .clickable { onImageClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Property image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Image counter badge
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "${currentIndex + 1}/5",
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun PropertyInfoSection(property: BalkanEstateProperty) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Property Type Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "${property.listingType} • ${property.propertyType}",
                color = BalkanEstatePrimaryBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Title
        Text(
            text = property.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Location
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = LocationIcon,
                contentDescription = null,
                tint = BalkanEstatePrimaryBlue,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${property.address}, ${property.city}",
                style = MaterialTheme.typography.bodyMedium,
                color = BalkanEstateGray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Price
        Text(
            text = formatPrice(property.price),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )
    }
}

@Composable
private fun PropertyFeaturesSection(property: BalkanEstateProperty) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Property Features",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FeatureItem(
                icon = BedroomsIcon,
                value = property.bedrooms.toString(),
                label = "Bedrooms"
            )
            FeatureItem(
                icon = BathroomsIcon,
                value = property.bathrooms.toString(),
                label = "Bathrooms"
            )
            FeatureItem(
                icon = SquareMetersIcon,
                value = "${property.squareFootage}",
                label = "m²"
            )
            FeatureItem(
                icon = ParkingSpotIcon,
                value = "1",
                label = "Parking"
            )
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
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BalkanEstatePrimaryBlue,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = label,
            color = BalkanEstateGray,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun PropertyDescriptionSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "This stunning property offers modern living in the heart of the city. " +
                    "Featuring spacious rooms, natural lighting, and premium finishes throughout. " +
                    "The open-plan living area is perfect for entertaining, while the bedrooms " +
                    "provide peaceful retreats. Located in a prime neighborhood with easy access " +
                    "to shops, restaurants, and public transportation.",
            style = MaterialTheme.typography.bodyMedium,
            color = BalkanEstateGray,
            lineHeight = 24.sp
        )
    }
}

@Composable
private fun PropertyLocationSection(
    property: BalkanEstateProperty,
    onViewMapClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Location",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Map placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
                .clickable { onViewMapClick() },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = LocationIcon,
                    contentDescription = null,
                    tint = BalkanEstatePrimaryBlue,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap to view on map",
                    color = BalkanEstatePrimaryBlue,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "${property.address}, ${property.city}, ${property.country}",
            style = MaterialTheme.typography.bodyMedium,
            color = BalkanEstateGray
        )
    }
}

@Composable
private fun AgentInfoSection(
    agentName: String,
    onContactClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Listed by",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(BalkanEstatePrimaryBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = agentName.take(1).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = agentName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Real Estate Agent",
                    color = BalkanEstateGray,
                    fontSize = 14.sp
                )
            }

            OutlinedButton(onClick = onContactClick) {
                Text("Contact")
            }
        }
    }
}

@Composable
private fun SimilarPropertiesSection(
    properties: List<BalkanEstateProperty>,
    onPropertyClick: (BalkanEstateProperty) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "Similar Properties",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(properties) { property ->
                SimilarPropertyCard(
                    property = property,
                    onClick = { onPropertyClick(property) }
                )
            }
        }
    }
}

@Composable
private fun SimilarPropertyCard(
    property: BalkanEstateProperty,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(property.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = formatPrice(property.price),
                    fontWeight = FontWeight.Bold,
                    color = BalkanEstatePrimaryBlue
                )
                Text(
                    text = property.title,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${property.bedrooms} bed • ${property.bathrooms} bath",
                    fontSize = 12.sp,
                    color = BalkanEstateGray
                )
            }
        }
    }
}

@Composable
private fun ContactBottomBar(
    price: Double,
    currency: String,
    onContactClick: () -> Unit,
    onScheduleClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Price",
                    color = BalkanEstateGray,
                    fontSize = 12.sp
                )
                Text(
                    text = formatPrice(price),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = BalkanEstatePrimaryBlue
                )
            }

            Button(
                onClick = onContactClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BalkanEstatePrimaryBlue
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Contact Agent")
            }
        }
    }
}

@Composable
private fun ContactAgentBottomSheet(
    agentName: String,
    onCallClick: () -> Unit,
    onEmailClick: () -> Unit,
    onWhatsAppClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Contact $agentName",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onCallClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Call, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Call Now")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onEmailClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = BalkanEstatePrimaryBlue
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Send Email", color = BalkanEstatePrimaryBlue)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onWhatsAppClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("WhatsApp", color = BalkanEstatePrimaryBlue)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

private fun formatPrice(price: Double): String {
    val formatter = NumberFormat.getNumberInstance()
    formatter.maximumFractionDigits = 0
    return "${formatter.format(price).replace(",", ".")} €"
}

@Preview(showBackground = true)
@Composable
private fun PropertyDetailsScreenPreview() {
    BalkanEstateTheme {
        PropertyDetailsScreen(
            state = PropertyDetailsState(
                property = BalkanEstateProperty(
                    id = "1",
                    title = "Modern 3BR Apartment in City Center",
                    price = 185000.0,
                    currency = "EUR",
                    imageUrl = "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2",
                    bedrooms = 3,
                    bathrooms = 2,
                    squareFootage = 120,
                    address = "123 Main Street, Downtown",
                    city = "Tirana",
                    country = "Albania",
                    latitude = 41.3275,
                    longitude = 19.8187,
                    propertyType = "Apartment",
                    listingType = "Sale",
                    agentName = "Besmir Kola",
                    isFeatured = true,
                    isUrgent = false
                ),
                isFavorite = false,
                isLoading = false
            ),
            onAction = {}
        )
    }
}
