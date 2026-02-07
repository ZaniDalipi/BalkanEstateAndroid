package com.zanoapps.search.presentation.search

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.Poppins

data class PropertyFilters(
    val propertyTypes: List<String> = emptyList(),
    val priceRange: ClosedFloatingPointRange<Float> = 0f..1000000f,
    val bedroomsMin: Int? = null,
    val bathroomsMin: Int? = null,
    val areaRange: ClosedFloatingPointRange<Float> = 0f..500f,
    val amenities: List<String> = emptyList(),
    val listingType: String? = null // "buy" or "rent"
)

@Composable
fun FiltersScreenRoot(
    currentFilters: PropertyFilters,
    onApplyFilters: (PropertyFilters) -> Unit,
    onClearFilters: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    FiltersScreen(
        currentFilters = currentFilters,
        onApplyFilters = onApplyFilters,
        onClearFilters = onClearFilters,
        onClose = onClose,
        modifier = modifier
    )
}

@Composable
fun FiltersScreen(
    currentFilters: PropertyFilters,
    onApplyFilters: (PropertyFilters) -> Unit,
    onClearFilters: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var filters by remember { mutableStateOf(currentFilters) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onClose) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.DarkGray
                            )
                        }
                        Text(
                            text = "Filters",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                            fontFamily = Poppins
                        )
                    }

                    Text(
                        text = "Clear all",
                        fontSize = 14.sp,
                        color = BalkanEstatePrimaryBlue,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable {
                            filters = PropertyFilters()
                            onClearFilters()
                        }
                    )
                }
            }

            // Filter Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Listing Type
                FilterSection(title = "Listing Type") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FilterChip(
                            text = "Buy",
                            isSelected = filters.listingType == "buy",
                            onClick = {
                                filters = filters.copy(
                                    listingType = if (filters.listingType == "buy") null else "buy"
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            text = "Rent",
                            isSelected = filters.listingType == "rent",
                            onClick = {
                                filters = filters.copy(
                                    listingType = if (filters.listingType == "rent") null else "rent"
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Property Type
                FilterSection(title = "Property Type") {
                    val propertyTypes = listOf("Apartment", "House", "Villa", "Studio", "Commercial", "Land")
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        propertyTypes.chunked(3).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                row.forEach { type ->
                                    FilterChip(
                                        text = type,
                                        isSelected = filters.propertyTypes.contains(type),
                                        onClick = {
                                            val newTypes = if (filters.propertyTypes.contains(type)) {
                                                filters.propertyTypes - type
                                            } else {
                                                filters.propertyTypes + type
                                            }
                                            filters = filters.copy(propertyTypes = newTypes)
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                repeat(3 - row.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                // Price Range
                FilterSection(title = "Price Range") {
                    PriceRangeSlider(
                        range = filters.priceRange,
                        onRangeChange = { filters = filters.copy(priceRange = it) }
                    )
                }

                // Bedrooms
                FilterSection(title = "Bedrooms") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Any", "1+", "2+", "3+", "4+", "5+").forEachIndexed { index, label ->
                            val minValue = if (index == 0) null else index
                            FilterChip(
                                text = label,
                                isSelected = filters.bedroomsMin == minValue,
                                onClick = {
                                    filters = filters.copy(bedroomsMin = minValue)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Bathrooms
                FilterSection(title = "Bathrooms") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Any", "1+", "2+", "3+", "4+").forEachIndexed { index, label ->
                            val minValue = if (index == 0) null else index
                            FilterChip(
                                text = label,
                                isSelected = filters.bathroomsMin == minValue,
                                onClick = {
                                    filters = filters.copy(bathroomsMin = minValue)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Area Range
                FilterSection(title = "Area (m²)") {
                    AreaRangeSlider(
                        range = filters.areaRange,
                        onRangeChange = { filters = filters.copy(areaRange = it) }
                    )
                }

                // Amenities
                FilterSection(title = "Amenities") {
                    val amenities = listOf(
                        "Parking", "Elevator", "Balcony", "Garden",
                        "Pool", "Gym", "Security", "Furnished"
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        amenities.chunked(4).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                row.forEach { amenity ->
                                    FilterChip(
                                        text = amenity,
                                        isSelected = filters.amenities.contains(amenity),
                                        onClick = {
                                            val newAmenities = if (filters.amenities.contains(amenity)) {
                                                filters.amenities - amenity
                                            } else {
                                                filters.amenities + amenity
                                            }
                                            filters = filters.copy(amenities = newAmenities)
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                repeat(4 - row.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }

            // Apply Button
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
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onClose,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Cancel",
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Button(
                        onClick = { onApplyFilters(filters) },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BalkanEstatePrimaryBlue
                        )
                    ) {
                        Text(
                            text = "Apply Filters",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
private fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = if (isSelected) BalkanEstatePrimaryBlue else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) BalkanEstatePrimaryBlue else Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            color = if (isSelected) Color.White else Color.DarkGray,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun PriceRangeSlider(
    range: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "€${"%,.0f".format(range.start)}",
                fontSize = 14.sp,
                color = BalkanEstatePrimaryBlue,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "€${"%,.0f".format(range.endInclusive)}",
                fontSize = 14.sp,
                color = BalkanEstatePrimaryBlue,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        RangeSlider(
            value = range,
            onValueChange = onRangeChange,
            valueRange = 0f..2000000f,
            steps = 19,
            colors = SliderDefaults.colors(
                thumbColor = BalkanEstatePrimaryBlue,
                activeTrackColor = BalkanEstatePrimaryBlue,
                inactiveTrackColor = Color.LightGray
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "€0",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = "€2,000,000+",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun AreaRangeSlider(
    range: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${range.start.toInt()} m²",
                fontSize = 14.sp,
                color = BalkanEstatePrimaryBlue,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${range.endInclusive.toInt()} m²",
                fontSize = 14.sp,
                color = BalkanEstatePrimaryBlue,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        RangeSlider(
            value = range,
            onValueChange = onRangeChange,
            valueRange = 0f..1000f,
            steps = 19,
            colors = SliderDefaults.colors(
                thumbColor = BalkanEstatePrimaryBlue,
                activeTrackColor = BalkanEstatePrimaryBlue,
                inactiveTrackColor = Color.LightGray
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "0 m²",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = "1000+ m²",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FiltersScreenPreview() {
    BalkanEstateTheme {
        FiltersScreen(
            currentFilters = PropertyFilters(),
            onApplyFilters = {},
            onClearFilters = {},
            onClose = {}
        )
    }
}
