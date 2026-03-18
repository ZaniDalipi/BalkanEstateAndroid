package com.zanoapps.favourites.presentation.compare

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.designsystem.BackIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateBackground
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.CompareIcon
import com.zanoapps.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun PropertyComparisonScreenRoot(
    viewModel: PropertyComparisonViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToPropertyDetail: (String) -> Unit
) {
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            PropertyComparisonEvent.NavigateBack -> onNavigateBack()
            is PropertyComparisonEvent.NavigateToPropertyDetail -> onNavigateToPropertyDetail(event.propertyId)
            is PropertyComparisonEvent.Error -> Unit
        }
    }

    PropertyComparisonScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun PropertyComparisonScreen(
    state: PropertyComparisonState,
    onAction: (PropertyComparisonAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BalkanEstateBackground)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onAction(PropertyComparisonAction.OnBackClick) }) {
                Icon(
                    imageVector = BackIcon,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = CompareIcon,
                contentDescription = null,
                tint = BalkanEstatePrimaryBlue,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Compare Properties",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        }

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = BalkanEstatePrimaryBlue,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "Loading properties...",
                            color = BalkanEstateGray,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            state.errorMessage != null && state.properties.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.errorMessage,
                        color = BalkanEstateRed,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }

            else -> {
                // Warning banner if partial error
                if (state.errorMessage != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .background(
                                BalkanEstateRed.copy(alpha = 0.1f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = state.errorMessage,
                            color = BalkanEstateRed,
                            fontSize = 13.sp
                        )
                    }
                }

                ComparisonTable(
                    properties = state.properties,
                    onViewDetails = { propertyId ->
                        onAction(PropertyComparisonAction.OnViewDetailsClick(propertyId))
                    }
                )
            }
        }
    }
}

@Composable
private fun ComparisonTable(
    properties: List<BalkanEstateProperty>,
    onViewDetails: (String) -> Unit
) {
    val labelWidth = 110.dp
    val columnWidth = 160.dp

    val bestPrice = properties.minOfOrNull { it.price }
    val bestBedrooms = properties.maxOfOrNull { it.bedrooms }
    val bestBathrooms = properties.maxOfOrNull { it.bathrooms }
    val bestArea = properties.maxOfOrNull { it.squareFootage }
    val bestYearBuilt = properties.maxOfOrNull { it.yearBuilt }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            // Labels column
            Column(modifier = Modifier.width(labelWidth)) {
                ComparisonLabel("", height = 140) // Image placeholder row
                ComparisonLabel("Title", height = 56)
                ComparisonLabel("Price", height = 48)
                ComparisonLabel("Location", height = 56)
                ComparisonLabel("Bedrooms", height = 48)
                ComparisonLabel("Bathrooms", height = 48)
                ComparisonLabel("Area (m\u00B2)", height = 48)
                ComparisonLabel("Year Built", height = 48)
                ComparisonLabel("Floor", height = 48)
                ComparisonLabel("Furnished", height = 48)
                ComparisonLabel("Parking", height = 48)
                ComparisonLabel("Property Type", height = 48)
                ComparisonLabel("Listing Type", height = 48)
                ComparisonLabel("", height = 56) // View Details row
            }

            // Property columns
            properties.forEach { property ->
                Column(
                    modifier = Modifier
                        .width(columnWidth)
                        .padding(start = 8.dp)
                ) {
                    // Image placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            tint = BalkanEstateGray,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    // Title
                    ComparisonValueCell(
                        value = property.title,
                        height = 56,
                        maxLines = 2
                    )

                    // Price
                    ComparisonValueCell(
                        value = "${formatPrice(property.price)} ${property.currency}",
                        height = 48,
                        highlight = property.price == bestPrice,
                        highlightColor = BalkanEstateGreen
                    )

                    // Location
                    ComparisonValueCell(
                        value = "${property.city}, ${property.country}",
                        height = 56,
                        maxLines = 2
                    )

                    // Bedrooms
                    ComparisonValueCell(
                        value = "${property.bedrooms}",
                        height = 48,
                        highlight = property.bedrooms == bestBedrooms,
                        highlightColor = BalkanEstateGreen
                    )

                    // Bathrooms
                    ComparisonValueCell(
                        value = "${property.bathrooms}",
                        height = 48,
                        highlight = property.bathrooms == bestBathrooms,
                        highlightColor = BalkanEstateGreen
                    )

                    // Area
                    ComparisonValueCell(
                        value = "${property.squareFootage}",
                        height = 48,
                        highlight = property.squareFootage == bestArea,
                        highlightColor = BalkanEstateGreen
                    )

                    // Year Built
                    ComparisonValueCell(
                        value = if (property.yearBuilt > 0) "${property.yearBuilt}" else "-",
                        height = 48,
                        highlight = property.yearBuilt == bestYearBuilt && property.yearBuilt > 0,
                        highlightColor = BalkanEstateGreen
                    )

                    // Floor
                    ComparisonValueCell(
                        value = if (property.totalFloors > 0) "${property.floorNumber}/${property.totalFloors}" else "-",
                        height = 48
                    )

                    // Furnished
                    ComparisonValueCell(
                        value = property.furnished.ifEmpty { "-" },
                        height = 48
                    )

                    // Parking
                    ComparisonValueCell(
                        value = property.parking.ifEmpty { "-" },
                        height = 48
                    )

                    // Property Type
                    ComparisonValueCell(
                        value = property.propertyType,
                        height = 48
                    )

                    // Listing Type
                    ComparisonValueCell(
                        value = property.listingType,
                        height = 48
                    )

                    // View Details button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(vertical = 4.dp)
                    ) {
                        Button(
                            onClick = { onViewDetails(property.id) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BalkanEstatePrimaryBlue
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "View Details",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ComparisonLabel(
    label: String,
    height: Int
) {
    Box(
        modifier = Modifier
            .height(height.dp)
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = BalkanEstateGray
        )
    }
}

@Composable
private fun ComparisonValueCell(
    value: String,
    height: Int,
    maxLines: Int = 1,
    highlight: Boolean = false,
    highlightColor: Color = Color.Transparent
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .padding(vertical = 4.dp)
            .then(
                if (highlight) {
                    Modifier
                        .background(
                            highlightColor.copy(alpha = 0.1f),
                            RoundedCornerShape(8.dp)
                        )
                        .border(1.dp, highlightColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                } else {
                    Modifier
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                }
            )
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
            color = if (highlight) highlightColor else Color.DarkGray,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun formatPrice(price: Double): String {
    return if (price >= 1_000_000) {
        String.format("%.1fM", price / 1_000_000)
    } else if (price >= 1_000) {
        String.format("%.0fK", price / 1_000)
    } else {
        String.format("%.0f", price)
    }
}

@Preview(showBackground = true, widthDp = 420, heightDp = 900)
@Composable
private fun PropertyComparisonScreenPreview() {
    val sampleProperty1 = BalkanEstateProperty(
        id = "1",
        title = "Modern Apartment in Belgrade",
        price = 120000.0,
        currency = "EUR",
        imageUrl = "",
        bedrooms = 3,
        bathrooms = 2,
        squareFootage = 95,
        address = "Knez Mihailova 10",
        city = "Belgrade",
        country = "Serbia",
        latitude = 44.8176,
        longitude = 20.4633,
        propertyType = "Apartment",
        listingType = "Sale",
        agentName = "Marko Petrovic",
        isFeatured = true,
        isUrgent = false,
        description = "Beautiful modern apartment",
        yearBuilt = 2020,
        floorNumber = 3,
        totalFloors = 8,
        furnished = "Fully",
        parking = "Garage",
        agentPhone = "+381641234567",
        agentEmail = "marko@example.com",
        agentAvatarUrl = ""
    )
    val sampleProperty2 = sampleProperty1.copy(
        id = "2",
        title = "Luxury Villa in Podgorica",
        price = 250000.0,
        bedrooms = 5,
        bathrooms = 3,
        squareFootage = 220,
        city = "Podgorica",
        country = "Montenegro",
        propertyType = "Villa",
        yearBuilt = 2018,
        floorNumber = 1,
        totalFloors = 2,
        furnished = "Semi",
        parking = "Driveway"
    )

    BalkanEstateTheme {
        PropertyComparisonScreen(
            state = PropertyComparisonState(
                properties = listOf(sampleProperty1, sampleProperty2),
                isLoading = false
            ),
            onAction = {}
        )
    }
}
