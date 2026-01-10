package com.zanoapps.search.presentation.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.domain.enums.Amenity
import com.zanoapps.core.domain.enums.FurnishedType
import com.zanoapps.core.domain.enums.ListingType
import com.zanoapps.core.domain.enums.PropertyType
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun FiltersScreenRoot(
    viewModel: FiltersViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onFiltersApplied: (FiltersState) -> Unit
) {
    FiltersScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                FiltersAction.OnBackClick -> onNavigateBack()
                FiltersAction.OnApplyFilters -> {
                    viewModel.onAction(action)
                    onFiltersApplied(viewModel.state)
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FiltersScreen(
    state: FiltersState,
    onAction: (FiltersAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filters") },
                navigationIcon = {
                    IconButton(onClick = { onAction(FiltersAction.OnBackClick) }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                actions = {
                    TextButton(onClick = { onAction(FiltersAction.OnResetFilters) }) {
                        Text("Reset", color = BalkanEstatePrimaryBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            FilterBottomBar(
                activeFiltersCount = state.activeFiltersCount,
                onApply = { onAction(FiltersAction.OnApplyFilters) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8FAFC))
                .verticalScroll(rememberScrollState())
        ) {
            // Listing Type
            FilterSection(title = "Listing Type") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ListingType.entries.take(2).forEach { type ->
                        FilterChipItem(
                            selected = state.selectedListingType == type,
                            label = type.name.lowercase().replaceFirstChar { it.uppercase() },
                            onClick = {
                                onAction(FiltersAction.OnListingTypeSelected(
                                    if (state.selectedListingType == type) null else type
                                ))
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Property Type
            FilterSection(title = "Property Type") {
                PropertyTypeGrid(
                    selectedTypes = state.selectedPropertyTypes,
                    onTypeToggled = { onAction(FiltersAction.OnPropertyTypeToggled(it)) }
                )
            }

            // Price Range
            FilterSection(title = "Price Range") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = state.minPrice,
                        onValueChange = { onAction(FiltersAction.OnMinPriceChanged(it)) },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Min") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        prefix = { Text("€") }
                    )
                    Text("-", color = Color.Gray)
                    OutlinedTextField(
                        value = state.maxPrice,
                        onValueChange = { onAction(FiltersAction.OnMaxPriceChanged(it)) },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Max") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        prefix = { Text("€") }
                    )
                }
            }

            // Bedrooms & Bathrooms
            FilterSection(title = "Bedrooms & Bathrooms") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Min Bedrooms",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CounterRow(
                            value = state.minBedrooms,
                            onValueChange = { onAction(FiltersAction.OnMinBedroomsChanged(it)) },
                            minValue = 0,
                            maxValue = 10
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Min Bathrooms",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CounterRow(
                            value = state.minBathrooms,
                            onValueChange = { onAction(FiltersAction.OnMinBathroomsChanged(it)) },
                            minValue = 0,
                            maxValue = 10
                        )
                    }
                }
            }

            // Size
            FilterSection(title = "Size (m²)") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = state.minSize,
                        onValueChange = { onAction(FiltersAction.OnMinSizeChanged(it)) },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Min") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        suffix = { Text("m²") }
                    )
                    Text("-", color = Color.Gray)
                    OutlinedTextField(
                        value = state.maxSize,
                        onValueChange = { onAction(FiltersAction.OnMaxSizeChanged(it)) },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Max") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        suffix = { Text("m²") }
                    )
                }
            }

            // Furnished
            FilterSection(title = "Furnished") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FurnishedType.entries.forEach { type ->
                        FilterChipItem(
                            selected = state.furnishedType == type,
                            label = type.name.lowercase().replaceFirstChar { it.uppercase() },
                            onClick = {
                                onAction(FiltersAction.OnFurnishedTypeChanged(
                                    if (state.furnishedType == type) null else type
                                ))
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Amenities
            FilterSection(title = "Amenities") {
                AmenitiesGrid(
                    selectedAmenities = state.selectedAmenities,
                    onAmenityToggled = { onAction(FiltersAction.OnAmenityToggled(it)) }
                )
            }

            // Additional Filters
            FilterSection(title = "Additional Filters") {
                Column {
                    CheckboxItem(
                        checked = state.onlyWithPhotos,
                        label = "Only with photos",
                        onCheckedChange = { onAction(FiltersAction.OnToggleOnlyWithPhotos) }
                    )
                    CheckboxItem(
                        checked = state.onlyFeatured,
                        label = "Only featured properties",
                        onCheckedChange = { onAction(FiltersAction.OnToggleOnlyFeatured) }
                    )
                    CheckboxItem(
                        checked = state.onlyNew,
                        label = "Only new listings (last 7 days)",
                        onCheckedChange = { onAction(FiltersAction.OnToggleOnlyNew) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        content()
    }
    HorizontalDivider(color = BalkanEstateGray.copy(alpha = 0.2f))
}

@Composable
private fun FilterChipItem(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) BalkanEstatePrimaryBlue else Color.White
        ),
        border = if (!selected) {
            androidx.compose.foundation.BorderStroke(1.dp, BalkanEstateGray.copy(alpha = 0.3f))
        } else null
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = if (selected) Color.White else Color.DarkGray,
                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PropertyTypeGrid(
    selectedTypes: Set<PropertyType>,
    onTypeToggled: (PropertyType) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PropertyType.entries.take(8).forEach { type ->
            FilterChip(
                selected = selectedTypes.contains(type),
                onClick = { onTypeToggled(type) },
                label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) },
                leadingIcon = if (selectedTypes.contains(type)) {
                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = BalkanEstatePrimaryBlue,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AmenitiesGrid(
    selectedAmenities: Set<Amenity>,
    onAmenityToggled: (Amenity) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Amenity.entries.take(10).forEach { amenity ->
            FilterChip(
                selected = selectedAmenities.contains(amenity),
                onClick = { onAmenityToggled(amenity) },
                label = { Text(amenity.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }) },
                leadingIcon = if (selectedAmenities.contains(amenity)) {
                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = BalkanEstatePrimaryBlue,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

@Composable
private fun CounterRow(
    value: Int,
    onValueChange: (Int) -> Unit,
    minValue: Int,
    maxValue: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (value > minValue) BalkanEstatePrimaryBlue else BalkanEstateGray.copy(alpha = 0.3f))
                .clickable(enabled = value > minValue) { onValueChange(value - 1) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "-",
                color = if (value > minValue) Color.White else Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Text(
            text = if (value == 0) "Any" else "$value+",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            modifier = Modifier.width(40.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (value < maxValue) BalkanEstatePrimaryBlue else BalkanEstateGray.copy(alpha = 0.3f))
                .clickable(enabled = value < maxValue) { onValueChange(value + 1) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                color = if (value < maxValue) Color.White else Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun CheckboxItem(
    checked: Boolean,
    label: String,
    onCheckedChange: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckedChange() },
            colors = CheckboxDefaults.colors(
                checkedColor = BalkanEstatePrimaryBlue
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label)
    }
}

@Composable
private fun FilterBottomBar(
    activeFiltersCount: Int,
    onApply: () -> Unit
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
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (activeFiltersCount > 0) {
                Text(
                    text = "$activeFiltersCount filters active",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onApply,
                colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Show Results")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FiltersScreenPreview() {
    BalkanEstateTheme {
        FiltersScreen(
            state = FiltersState(),
            onAction = {}
        )
    }
}
