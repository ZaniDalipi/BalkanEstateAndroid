package com.zanoapps.property_details.presentation.property_detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.designsystem.AddedToFavIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.BathroomsIcon
import com.zanoapps.core.presentation.designsystem.BedroomsIcon
import com.zanoapps.core.presentation.designsystem.EmailIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.NotAddedToFavIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.SharePropertyIcon
import com.zanoapps.core.presentation.designsystem.SquareMetersIcon
import com.zanoapps.core.presentation.designsystem.VirtualTourIcon
import com.zanoapps.property_details.presentation.R
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PropertyDetailScreenRoot(
    viewModel: PropertyDetailViewModel = koinViewModel(),
    property: BalkanEstateProperty,
    onBackClick: () -> Unit,
    onContactAgent: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(property) {
        viewModel.setProperty(property)
    }

    LaunchedEffect(key1 = true) {
        viewModel.events.collect { event ->
            when (event) {
                is PropertyDetailEvent.Error -> {
                    Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
                }
                PropertyDetailEvent.PropertyAddedToFavorites -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.added_to_favorites),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                PropertyDetailEvent.PropertyRemovedFromFavorites -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.removed_from_favorites),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                PropertyDetailEvent.PropertyShared -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.property_shared),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    PropertyDetailScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                PropertyDetailAction.OnBackClick -> onBackClick()
                PropertyDetailAction.OnContactAgentClick -> onContactAgent()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PropertyDetailScreen(
    state: PropertyDetailState,
    onAction: (PropertyDetailAction) -> Unit
) {
    val property = state.property

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { onAction(PropertyDetailAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onAction(PropertyDetailAction.OnShareClick) }) {
                        Icon(
                            imageVector = SharePropertyIcon,
                            contentDescription = stringResource(R.string.share_property)
                        )
                    }
                    IconButton(onClick = { onAction(PropertyDetailAction.OnFavoriteClick) }) {
                        Icon(
                            imageVector = if (state.isFavorite) AddedToFavIcon else NotAddedToFavIcon,
                            contentDescription = "Favorite",
                            tint = if (state.isFavorite) Color.Red else Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        if (state.isLoading || property == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Property Image
                PropertyImageSection(
                    imageUrl = property.imageUrl,
                    isFeatured = property.isFeatured,
                    isUrgent = property.isUrgent
                )

                // Property Info
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    // Price and Listing Type
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatPrice(property.price, property.currency),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = BalkanEstatePrimaryBlue
                        )

                        ListingTypeBadge(listingType = property.listingType)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Title
                    Text(
                        text = property.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
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
                            text = "${property.address}, ${property.city}, ${property.country}",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Property Features
                PropertyFeaturesCard(property = property)

                Spacer(modifier = Modifier.height(8.dp))

                // Quick Actions
                QuickActionsSection(onAction = onAction)

                Spacer(modifier = Modifier.height(8.dp))

                // Agent Info
                AgentInfoCard(
                    agentName = property.agentName,
                    onContactClick = { onAction(PropertyDetailAction.OnContactAgentClick) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Contact Button
                Button(
                    onClick = { onAction(PropertyDetailAction.OnContactAgentClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BalkanEstatePrimaryBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.contact_agent),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun PropertyImageSection(
    imageUrl: String,
    isFeatured: Boolean,
    isUrgent: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(4f / 3f)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Property Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Badges
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isFeatured) {
                PropertyBadge(
                    text = stringResource(R.string.featured),
                    backgroundColor = BalkanEstatePrimaryBlue
                )
            }
            if (isUrgent) {
                PropertyBadge(
                    text = stringResource(R.string.urgent),
                    backgroundColor = Color(0xFFEF4444)
                )
            }
        }
    }
}

@Composable
private fun PropertyBadge(
    text: String,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ListingTypeBadge(listingType: String) {
    val (text, color) = when (listingType.lowercase()) {
        "sale" -> stringResource(R.string.for_sale) to Color(0xFF10B981)
        "rent" -> stringResource(R.string.for_rent) to Color(0xFFF59E0B)
        else -> listingType to Color.Gray
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun PropertyFeaturesCard(property: BalkanEstateProperty) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FeatureItem(
                icon = BedroomsIcon,
                value = "${property.bedrooms}",
                label = stringResource(R.string.bedrooms)
            )
            FeatureItem(
                icon = BathroomsIcon,
                value = "${property.bathrooms}",
                label = stringResource(R.string.bathrooms)
            )
            FeatureItem(
                icon = SquareMetersIcon,
                value = "${property.squareFootage}",
                label = stringResource(R.string.square_feet)
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
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BalkanEstatePrimaryBlue,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun QuickActionsSection(
    onAction: (PropertyDetailAction) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickActionItem(
                icon = VirtualTourIcon,
                label = stringResource(R.string.virtual_tour),
                onClick = { onAction(PropertyDetailAction.OnViewVirtualTourClick) }
            )
            QuickActionItem(
                icon = LocationIcon,
                label = stringResource(R.string.view_on_map),
                onClick = { onAction(PropertyDetailAction.OnViewMapClick) }
            )
            QuickActionItem(
                icon = SharePropertyIcon,
                label = stringResource(R.string.share_property),
                onClick = { onAction(PropertyDetailAction.OnShareClick) }
            )
        }
    }
}

@Composable
private fun QuickActionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
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
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun AgentInfoCard(
    agentName: String,
    onContactClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Agent Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = PersonIcon,
                    contentDescription = null,
                    tint = BalkanEstatePrimaryBlue,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = agentName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    text = stringResource(R.string.agent_info),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            OutlinedButton(
                onClick = onContactClick,
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = EmailIcon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = stringResource(R.string.contact_agent))
            }
        }
    }
}

private fun formatPrice(price: Double, currency: String): String {
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    return "$currency ${formatter.format(price.toLong())}"
}

@Preview(showBackground = true)
@Composable
private fun PropertyDetailScreenPreview() {
    BalkanEstateTheme {
        PropertyDetailScreen(
            state = PropertyDetailState(
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
                isFavorite = false
            ),
            onAction = {}
        )
    }
}
