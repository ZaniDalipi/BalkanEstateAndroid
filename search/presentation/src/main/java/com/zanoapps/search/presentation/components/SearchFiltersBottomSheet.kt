package com.zanoapps.search.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SheetState
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.CheckIcon
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateActionButton
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateOutlinedActionButton
import java.text.NumberFormat
import java.util.Locale

data class SearchFiltersState(
    val minPrice: Int = 0,
    val maxPrice: Int = 1000000,
    val selectedPropertyTypes: Set<String> = emptySet(),
    val minBedrooms: Int = 0,
    val minBathrooms: Int = 0,
    val minSquareFootage: Int = 0,
    val maxSquareFootage: Int = 10000,
    val selectedListingType: String? = null, // "Sale" or "Rent"
    val selectedAmenities: Set<String> = emptySet()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFiltersBottomSheet(
    isVisible: Boolean,
    filters: SearchFiltersState,
    onDismiss: () -> Unit,
    onApplyFilters: (SearchFiltersState) -> Unit,
    onResetFilters: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    if (isVisible) {
        var localFilters by remember(filters) { mutableStateOf(filters) }

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filters",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Reset",
                        fontSize = 14.sp,
                        color = BalkanEstatePrimaryBlue,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable {
                            localFilters = SearchFiltersState()
                            onResetFilters()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Listing Type
                FilterSection(title = "Listing Type") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FilterChip(
                            text = "For Sale",
                            isSelected = localFilters.selectedListingType == "Sale",
                            onClick = {
                                localFilters = localFilters.copy(
                                    selectedListingType = if (localFilters.selectedListingType == "Sale") null else "Sale"
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            text = "For Rent",
                            isSelected = localFilters.selectedListingType == "Rent",
                            onClick = {
                                localFilters = localFilters.copy(
                                    selectedListingType = if (localFilters.selectedListingType == "Rent") null else "Rent"
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                // Property Type
                FilterSection(title = "Property Type") {
                    PropertyTypeSelector(
                        selectedTypes = localFilters.selectedPropertyTypes,
                        onTypeSelected = { type ->
                            val newTypes = if (localFilters.selectedPropertyTypes.contains(type)) {
                                localFilters.selectedPropertyTypes - type
                            } else {
                                localFilters.selectedPropertyTypes + type
                            }
                            localFilters = localFilters.copy(selectedPropertyTypes = newTypes)
                        }
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                // Price Range
                FilterSection(title = "Price Range") {
                    PriceRangeSelector(
                        minPrice = localFilters.minPrice,
                        maxPrice = localFilters.maxPrice,
                        onPriceChange = { min, max ->
                            localFilters = localFilters.copy(minPrice = min, maxPrice = max)
                        }
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                // Bedrooms
                FilterSection(title = "Bedrooms") {
                    NumberSelector(
                        value = localFilters.minBedrooms,
                        options = listOf("Any" to 0, "1+" to 1, "2+" to 2, "3+" to 3, "4+" to 4, "5+" to 5),
                        onValueChange = { localFilters = localFilters.copy(minBedrooms = it) }
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                // Bathrooms
                FilterSection(title = "Bathrooms") {
                    NumberSelector(
                        value = localFilters.minBathrooms,
                        options = listOf("Any" to 0, "1+" to 1, "2+" to 2, "3+" to 3, "4+" to 4),
                        onValueChange = { localFilters = localFilters.copy(minBathrooms = it) }
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                // Amenities
                FilterSection(title = "Amenities") {
                    AmenitiesSelector(
                        selectedAmenities = localFilters.selectedAmenities,
                        onAmenityToggle = { amenity ->
                            val newAmenities = if (localFilters.selectedAmenities.contains(amenity)) {
                                localFilters.selectedAmenities - amenity
                            } else {
                                localFilters.selectedAmenities + amenity
                            }
                            localFilters = localFilters.copy(selectedAmenities = newAmenities)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BalkanEstateOutlinedActionButton(
                        text = "Cancel",
                        isLoading = false,
                        modifier = Modifier.weight(1f),
                        onClick = onDismiss
                    )
                    BalkanEstateActionButton(
                        text = "Apply Filters",
                        isLoading = false,
                        modifier = Modifier.weight(1f),
                        onClick = { onApplyFilters(localFilters) }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(12.dp))
        content()
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
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) BalkanEstatePrimaryBlue else Color.White)
            .border(
                width = 1.dp,
                color = if (isSelected) BalkanEstatePrimaryBlue else Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Icon(
                    imageVector = CheckIcon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Text(
                text = text,
                color = if (isSelected) Color.White else Color.Black,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PropertyTypeSelector(
    selectedTypes: Set<String>,
    onTypeSelected: (String) -> Unit
) {
    val propertyTypes = listOf("Apartment", "House", "Villa", "Land", "Commercial", "Office")

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        propertyTypes.forEach { type ->
            FilterChip(
                text = type,
                isSelected = selectedTypes.contains(type),
                onClick = { onTypeSelected(type) }
            )
        }
    }
}

@Composable
private fun PriceRangeSelector(
    minPrice: Int,
    maxPrice: Int,
    onPriceChange: (Int, Int) -> Unit
) {
    var sliderPosition by remember(minPrice, maxPrice) {
        mutableStateOf(minPrice.toFloat()..maxPrice.toFloat())
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Min", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = formatCurrency(sliderPosition.start.toInt()),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Max", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = formatCurrency(sliderPosition.endInclusive.toInt()),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        RangeSlider(
            value = sliderPosition,
            onValueChange = { range ->
                sliderPosition = range
                onPriceChange(range.start.toInt(), range.endInclusive.toInt())
            },
            valueRange = 0f..2000000f,
            steps = 19,
            colors = SliderDefaults.colors(
                thumbColor = BalkanEstatePrimaryBlue,
                activeTrackColor = BalkanEstatePrimaryBlue,
                inactiveTrackColor = Color.LightGray
            )
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NumberSelector(
    value: Int,
    options: List<Pair<String, Int>>,
    onValueChange: (Int) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { (label, optionValue) ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (value == optionValue) BalkanEstatePrimaryBlue else Color.White)
                    .border(
                        width = 1.dp,
                        color = if (value == optionValue) BalkanEstatePrimaryBlue else Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onValueChange(optionValue) }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = if (value == optionValue) Color.White else Color.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AmenitiesSelector(
    selectedAmenities: Set<String>,
    onAmenityToggle: (String) -> Unit
) {
    val amenities = listOf(
        "Parking", "Pool", "Gym", "Garden", "Balcony", "Terrace",
        "Air Conditioning", "Heating", "Furnished", "Pet Friendly",
        "Security", "Elevator", "Storage", "Sea View"
    )

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        amenities.forEach { amenity ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (selectedAmenities.contains(amenity)) BalkanEstatePrimaryBlue.copy(alpha = 0.1f) else Color.White)
                    .border(
                        width = 1.dp,
                        color = if (selectedAmenities.contains(amenity)) BalkanEstatePrimaryBlue else Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onAmenityToggle(amenity) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = amenity,
                    color = if (selectedAmenities.contains(amenity)) BalkanEstatePrimaryBlue else Color.Black,
                    fontSize = 13.sp
                )
            }
        }
    }
}

private fun formatCurrency(value: Int): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    formatter.maximumFractionDigits = 0
    return formatter.format(value)
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun SearchFiltersBottomSheetPreview() {
    BalkanEstateTheme {
        SearchFiltersBottomSheet(
            isVisible = true,
            filters = SearchFiltersState(),
            onDismiss = {},
            onApplyFilters = {},
            onResetFilters = {}
        )
    }
}
