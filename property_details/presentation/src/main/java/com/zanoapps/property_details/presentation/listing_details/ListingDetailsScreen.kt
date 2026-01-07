package com.zanoapps.property_details.presentation.listing_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zanoapps.core.domain.enums.Amenity
import com.zanoapps.core.domain.enums.FurnishedType
import com.zanoapps.core.domain.enums.ListingType
import com.zanoapps.core.domain.enums.ParkingType
import com.zanoapps.core.domain.enums.PetPolicy
import com.zanoapps.core.domain.enums.PropertyStatus
import com.zanoapps.core.domain.enums.PropertyType
import com.zanoapps.core.presentation.designsystem.AddedToFavIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.BathroomsIcon
import com.zanoapps.core.presentation.designsystem.BedroomsIcon
import com.zanoapps.core.presentation.designsystem.KeyboardArrowDownIcon
import com.zanoapps.core.presentation.designsystem.KeyboardArrowUpIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.NotAddedToFavIcon
import com.zanoapps.core.presentation.designsystem.ParkingSpotIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.SquareMetersIcon
import com.zanoapps.core.presentation.designsystem.YearBuildIcon
import com.zanoapps.property_details.domain.model.PropertyImageState
import com.zanoapps.property_details.domain.model.PropertyState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat
import java.time.LocalDateTime

@Composable
fun ListingDetailsScreenRoot(
    viewModel: ListingDetailsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ListingDetailsEvent.NavigateBack -> onNavigateBack()
                is ListingDetailsEvent.Error -> {
                    // Handle error - show snackbar
                }
                ListingDetailsEvent.ContactFormSubmitted -> {
                    // Show success message
                }
                ListingDetailsEvent.TourRequestSubmitted -> {
                    // Show success message
                }
                ListingDetailsEvent.AddedToFavorites -> {
                    // Show success message
                }
                ListingDetailsEvent.RemovedFromFavorites -> {
                    // Show success message
                }
                is ListingDetailsEvent.ShareProperty -> {
                    // Handle share
                }
                ListingDetailsEvent.LinkCopied -> {
                    // Show copied message
                }
                is ListingDetailsEvent.OpenDialer -> {
                    // Open phone dialer
                }
                is ListingDetailsEvent.OpenMap -> {
                    // Open map
                }
                is ListingDetailsEvent.OpenEmail -> {
                    // Open email
                }
            }
        }
    }

    ListingDetailsScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListingDetailsScreen(
    state: ListingDetailsState,
    onAction: (ListingDetailsAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { onAction(ListingDetailsAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onAction(ListingDetailsAction.OnShareClick) }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { onAction(ListingDetailsAction.OnFavoriteToggle) }) {
                        Icon(
                            imageVector = if (state.isFavorite) AddedToFavIcon else NotAddedToFavIcon,
                            contentDescription = if (state.isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (state.isFavorite) Color.Red else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            BottomActionBar(
                onContactAgent = { onAction(ListingDetailsAction.OnContactAgentClick) },
                onScheduleTour = { onAction(ListingDetailsAction.OnScheduleTourClick) },
                onCallAgent = { onAction(ListingDetailsAction.OnCallAgentClick) }
            )
        },
        containerColor = Color(0xFFF8FAFC)
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
        } else if (state.property != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Image Gallery
                item {
                    ImageGallerySection(
                        images = state.allImages,
                        selectedIndex = state.selectedImageIndex,
                        onImageSelected = { index ->
                            onAction(ListingDetailsAction.OnImageSelected(index))
                        },
                        onOpenGallery = { onAction(ListingDetailsAction.OnOpenImageGallery) }
                    )
                }

                // Property Header
                item {
                    PropertyHeaderSection(
                        property = state.property,
                        isFavorite = state.isFavorite
                    )
                }

                // Quick Features
                item {
                    QuickFeaturesSection(property = state.property)
                }

                // Overview Section
                item {
                    ExpandableSection(
                        title = "Overview",
                        section = DetailSection.OVERVIEW,
                        isExpanded = state.expandedSections.contains(DetailSection.OVERVIEW),
                        onToggle = { onAction(ListingDetailsAction.OnToggleSection(DetailSection.OVERVIEW)) }
                    ) {
                        OverviewContent(property = state.property)
                    }
                }

                // Features Section
                item {
                    ExpandableSection(
                        title = "Features & Details",
                        section = DetailSection.FEATURES,
                        isExpanded = state.expandedSections.contains(DetailSection.FEATURES),
                        onToggle = { onAction(ListingDetailsAction.OnToggleSection(DetailSection.FEATURES)) }
                    ) {
                        FeaturesContent(property = state.property)
                    }
                }

                // Amenities Section
                item {
                    ExpandableSection(
                        title = "Amenities",
                        section = DetailSection.AMENITIES,
                        isExpanded = state.expandedSections.contains(DetailSection.AMENITIES),
                        onToggle = { onAction(ListingDetailsAction.OnToggleSection(DetailSection.AMENITIES)) }
                    ) {
                        AmenitiesContent(amenities = state.property.amenities)
                    }
                }

                // Financial Section (for Sale listings)
                if (state.property.listingType == ListingType.SALE) {
                    item {
                        ExpandableSection(
                            title = "Financial Details",
                            section = DetailSection.FINANCIAL,
                            isExpanded = state.expandedSections.contains(DetailSection.FINANCIAL),
                            onToggle = { onAction(ListingDetailsAction.OnToggleSection(DetailSection.FINANCIAL)) }
                        ) {
                            FinancialContent(property = state.property)
                        }
                    }
                }

                // Location Section
                item {
                    ExpandableSection(
                        title = "Location",
                        section = DetailSection.LOCATION,
                        isExpanded = state.expandedSections.contains(DetailSection.LOCATION),
                        onToggle = { onAction(ListingDetailsAction.OnToggleSection(DetailSection.LOCATION)) }
                    ) {
                        LocationContent(
                            property = state.property,
                            onViewOnMap = { onAction(ListingDetailsAction.OnViewOnMapClick) }
                        )
                    }
                }

                // Agent Section
                item {
                    ExpandableSection(
                        title = "Listed By",
                        section = DetailSection.AGENT,
                        isExpanded = state.expandedSections.contains(DetailSection.AGENT),
                        onToggle = { onAction(ListingDetailsAction.OnToggleSection(DetailSection.AGENT)) }
                    ) {
                        AgentContent(
                            property = state.property,
                            onCallAgent = { onAction(ListingDetailsAction.OnCallAgentClick) },
                            onContactAgent = { onAction(ListingDetailsAction.OnContactAgentClick) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ImageGallerySection(
    images: List<PropertyImageState>,
    selectedIndex: Int,
    onImageSelected: (Int) -> Unit,
    onOpenGallery: () -> Unit
) {
    Column {
        // Main Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f)
                .clickable { onOpenGallery() }
        ) {
            val currentImage = images.getOrNull(selectedIndex)
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(currentImage?.url)
                    .crossfade(true)
                    .build(),
                contentDescription = currentImage?.caption ?: "Property image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Image counter badge
            if (images.size > 1) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp),
                    shape = RoundedCornerShape(4.dp),
                    color = Color.Black.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = "${selectedIndex + 1}/${images.size}",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Thumbnail Strip
        if (images.size > 1) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                itemsIndexed(images) { index, image ->
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onImageSelected(index) }
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(image.url)
                                .crossfade(true)
                                .build(),
                            contentDescription = image.caption,
                            modifier = Modifier
                                .fillMaxSize()
                                .then(
                                    if (index == selectedIndex) {
                                        Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                BalkanEstatePrimaryBlue.copy(alpha = 0.3f)
                                            )
                                    } else Modifier
                                ),
                            contentScale = ContentScale.Crop
                        )
                        if (index == selectedIndex) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.Transparent)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(BalkanEstatePrimaryBlue.copy(alpha = 0.2f))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PropertyHeaderSection(
    property: PropertyState,
    isFavorite: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Listing Type Badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = when (property.listingType) {
                    ListingType.SALE -> BalkanEstatePrimaryBlue
                    ListingType.RENT -> BalkanEstateGreen
                    else -> BalkanEstateGray
                }
            ) {
                Text(
                    text = property.listingType.name.uppercase(),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            if (property.featured) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFF59E0B)
                ) {
                    Text(
                        text = "FEATURED",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Text(
            text = property.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Location
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = LocationIcon,
                contentDescription = null,
                tint = BalkanEstateGray,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${property.address}, ${property.city}",
                style = MaterialTheme.typography.bodyMedium,
                color = BalkanEstateGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Price
        Text(
            text = formatPrice(property.price, property.currency),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )

        if (property.listingType == ListingType.RENT && property.monthlyRent != null) {
            Text(
                text = "per month",
                style = MaterialTheme.typography.bodySmall,
                color = BalkanEstateGray
            )
        }
    }
}

@Composable
private fun QuickFeaturesSection(property: PropertyState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickFeatureItem(
                icon = BedroomsIcon,
                value = property.bedrooms.toString(),
                label = "Beds"
            )
            QuickFeatureItem(
                icon = BathroomsIcon,
                value = property.bathrooms.toString(),
                label = "Baths"
            )
            QuickFeatureItem(
                icon = SquareMetersIcon,
                value = formatNumber(property.squareFootage),
                label = "m²"
            )
            if (property.parkingSpaces > 0) {
                QuickFeatureItem(
                    icon = ParkingSpotIcon,
                    value = property.parkingSpaces.toString(),
                    label = "Parking"
                )
            }
            if (property.yearBuilt != null) {
                QuickFeatureItem(
                    icon = YearBuildIcon,
                    value = property.yearBuilt.toString(),
                    label = "Built"
                )
            }
        }
    }
}

@Composable
private fun QuickFeatureItem(
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
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = BalkanEstateGray
        )
    }
}

@Composable
private fun ExpandableSection(
    title: String,
    section: DetailSection,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = if (isExpanded) KeyboardArrowUpIcon else KeyboardArrowDownIcon,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = BalkanEstateGray
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                ) {
                    HorizontalDivider(color = Color(0xFFE5E7EB))
                    Spacer(modifier = Modifier.height(12.dp))
                    content()
                }
            }
        }
    }
}

@Composable
private fun OverviewContent(property: PropertyState) {
    Text(
        text = property.description,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 24.sp
    )
}

@Composable
private fun FeaturesContent(property: PropertyState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FeatureRow(label = "Property Type", value = property.propertyType.name.replace("_", " "))
        FeatureRow(label = "Bedrooms", value = "${property.bedrooms}")
        FeatureRow(label = "Bathrooms", value = "${property.bathrooms}")
        if (property.halfBathrooms > 0) {
            FeatureRow(label = "Half Bathrooms", value = "${property.halfBathrooms}")
        }
        FeatureRow(label = "Square Footage", value = "${formatNumber(property.squareFootage)} m²")
        property.lotSize?.let {
            FeatureRow(label = "Lot Size", value = "${formatNumber(it)} m²")
        }
        property.floors?.let {
            FeatureRow(label = "Floors", value = "$it")
        }
        property.yearBuilt?.let {
            FeatureRow(label = "Year Built", value = "$it")
        }
        property.heating?.let {
            FeatureRow(label = "Heating", value = it.name.replace("_", " "))
        }
        property.cooling?.let {
            FeatureRow(label = "Cooling", value = it.name.replace("_", " "))
        }
        FeatureRow(label = "Furnished", value = property.furnished.name.replace("_", " "))
        property.parking?.let {
            FeatureRow(label = "Parking", value = it.name.replace("_", " "))
        }
        if (property.parkingSpaces > 0) {
            FeatureRow(label = "Parking Spaces", value = "${property.parkingSpaces}")
        }
        FeatureRow(label = "Pet Policy", value = property.petPolicy.name.replace("_", " "))
    }
}

@Composable
private fun FeatureRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AmenitiesContent(amenities: List<Amenity>) {
    if (amenities.isEmpty()) {
        Text(
            text = "No amenities listed",
            style = MaterialTheme.typography.bodyMedium,
            color = BalkanEstateGray
        )
    } else {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            amenities.forEach { amenity ->
                AmenityChip(amenity = amenity)
            }
        }
    }
}

@Composable
private fun AmenityChip(amenity: Amenity) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = BalkanEstatePrimaryBlue.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(BalkanEstatePrimaryBlue)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = amenity.name.replace("_", " ").lowercase()
                    .replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodySmall,
                color = BalkanEstatePrimaryBlue,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun FinancialContent(property: PropertyState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FeatureRow(
            label = "Asking Price",
            value = formatPrice(property.price, property.currency)
        )
        property.hoa?.let {
            FeatureRow(label = "HOA Fees", value = "${formatPrice(it, property.currency)}/month")
        }
        property.propertyTax?.let {
            FeatureRow(label = "Property Tax", value = "${formatPrice(it, property.currency)}/year")
        }
        property.insurance?.let {
            FeatureRow(label = "Insurance", value = "${formatPrice(it, property.currency)}/year")
        }
    }
}

@Composable
private fun LocationContent(
    property: PropertyState,
    onViewOnMap: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FeatureRow(label = "Address", value = property.address)
        FeatureRow(label = "City", value = property.city)
        FeatureRow(label = "Country", value = property.country)
        property.postalCode?.let {
            FeatureRow(label = "Postal Code", value = it)
        }
        property.neighbourhood?.let {
            FeatureRow(label = "Neighbourhood", value = it)
        }

        if (property.latitude != null && property.longitude != null) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onViewOnMap,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = LocationIcon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("View on Map")
            }
        }
    }
}

@Composable
private fun AgentContent(
    property: PropertyState,
    onCallAgent: () -> Unit,
    onContactAgent: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
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
                Icon(
                    imageVector = PersonIcon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = property.agentName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Real Estate Agent",
                    style = MaterialTheme.typography.bodySmall,
                    color = BalkanEstateGray
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onCallAgent,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Call")
            }
            Button(
                onClick = onContactAgent,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BalkanEstatePrimaryBlue
                )
            ) {
                Text("Contact")
            }
        }
    }
}

@Composable
private fun BottomActionBar(
    onContactAgent: () -> Unit,
    onScheduleTour: () -> Unit,
    onCallAgent: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onCallAgent,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Call")
            }
            Button(
                onClick = onScheduleTour,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BalkanEstateGreen
                )
            ) {
                Text("Tour")
            }
            Button(
                onClick = onContactAgent,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BalkanEstatePrimaryBlue
                )
            ) {
                Text("Contact")
            }
        }
    }
}

// Utility functions
private fun formatPrice(price: Double, currency: String): String {
    val formatter = NumberFormat.getNumberInstance()
    formatter.maximumFractionDigits = 0
    return when (currency.uppercase()) {
        "EUR" -> "€${formatter.format(price).replace(",", ".")}"
        "USD" -> "$${formatter.format(price).replace(",", ".")}"
        else -> "${formatter.format(price).replace(",", ".")} $currency"
    }
}

private fun formatNumber(number: Int): String {
    return NumberFormat.getInstance().format(number)
}

@Preview(showBackground = true)
@Composable
private fun ListingDetailsScreenPreview() {
    BalkanEstateTheme {
        ListingDetailsScreen(
            state = ListingDetailsState(
                property = PropertyState(
                    id = "1",
                    title = "Luxury Modern Villa with Sea View",
                    description = "Experience luxury living in this stunning modern villa.",
                    price = 450000.0,
                    currency = "EUR",
                    listingType = ListingType.SALE,
                    propertyType = PropertyType.VILLA,
                    propertySubType = null,
                    address = "123 Coastal Drive",
                    city = "Tirana",
                    country = "Albania",
                    postalCode = "1001",
                    latitude = 41.3275,
                    longitude = 19.8187,
                    neighbourhood = "Bay View",
                    bedrooms = 4,
                    bathrooms = 3,
                    squareFootage = 2500,
                    lotSize = 5000,
                    yearBuilt = 2022,
                    floors = 2,
                    parking = ParkingType.GARAGE,
                    parkingSpaces = 2,
                    heating = null,
                    cooling = null,
                    furnished = FurnishedType.FULLY_FURNISHED,
                    petPolicy = PetPolicy.ALLOWED,
                    amenities = listOf(Amenity.POOL, Amenity.GYM, Amenity.GARDEN),
                    appliances = emptyList(),
                    images = listOf(
                        PropertyImageState("1", "https://example.com/1.jpg", "Main", 0, true)
                    ),
                    virtualTour = null,
                    floorPlan = null,
                    agentId = "1",
                    agentName = "Besmir Kola",
                    agentPhone = "+355 69 123 4567",
                    agentEmail = "agent@example.com",
                    status = PropertyStatus.ACTIVE,
                    availableFrom = LocalDateTime.now(),
                    leaseLength = null,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                    featured = true,
                    urgent = false,
                    views = 1234,
                    favorites = 56
                ),
                isLoading = false,
                isFavorite = false
            ),
            onAction = {}
        )
    }
}
