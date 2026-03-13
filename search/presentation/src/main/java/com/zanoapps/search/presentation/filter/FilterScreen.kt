package com.zanoapps.search.presentation.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BackIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import org.koin.androidx.compose.koinViewModel

@Composable
fun FilterScreenRoot(
    viewModel: FilterViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onFiltersApplied: () -> Unit
) {
    FilterScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                FilterAction.OnBackClick -> onNavigateBack()
                FilterAction.OnApplyFilters -> {
                    viewModel.onAction(action)
                    onFiltersApplied()
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterScreen(
    state: FilterState,
    onAction: (FilterAction) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8FAFC))) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onAction(FilterAction.OnBackClick) }) {
                    Icon(BackIcon, "Back", Modifier.size(20.dp))
                }
                Text("Filters", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            OutlinedButton(onClick = { onAction(FilterAction.OnClearFilters) }, shape = RoundedCornerShape(8.dp)) {
                Text("Clear All", fontSize = 13.sp)
            }
        }

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)) {
            // Listing Type
            FilterSection("Listing Type") {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Sale", "Rent").forEach { type ->
                        FilterChip(
                            selected = state.selectedListingTypes.contains(type),
                            onClick = { onAction(FilterAction.OnListingTypeToggle(type)) },
                            label = { Text(type) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BalkanEstatePrimaryBlue, selectedLabelColor = Color.White)
                        )
                    }
                }
            }

            // Property Type
            FilterSection("Property Type") {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Apartment", "House", "Villa", "Studio", "Penthouse", "Commercial", "Land", "Office").forEach { type ->
                        FilterChip(
                            selected = state.selectedPropertyTypes.contains(type),
                            onClick = { onAction(FilterAction.OnPropertyTypeToggle(type)) },
                            label = { Text(type, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BalkanEstatePrimaryBlue, selectedLabelColor = Color.White)
                        )
                    }
                }
            }

            // Price Range
            FilterSection("Price Range (\u20AC)") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = state.minPriceText, onValueChange = { onAction(FilterAction.OnMinPriceChanged(it)) }, label = { Text("Min") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue), singleLine = true)
                    OutlinedTextField(value = state.maxPriceText, onValueChange = { onAction(FilterAction.OnMaxPriceChanged(it)) }, label = { Text("Max") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue), singleLine = true)
                }
            }

            // Bedrooms
            FilterSection("Bedrooms") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(null, 1, 2, 3, 4, 5).forEach { beds ->
                        FilterChip(
                            selected = state.selectedBedrooms == beds,
                            onClick = { onAction(FilterAction.OnBedroomsSelected(beds)) },
                            label = { Text(beds?.toString() ?: "Any", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BalkanEstatePrimaryBlue, selectedLabelColor = Color.White)
                        )
                    }
                }
            }

            // Bathrooms
            FilterSection("Bathrooms") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(null, 1, 2, 3, 4).forEach { baths ->
                        FilterChip(
                            selected = state.selectedBathrooms == baths,
                            onClick = { onAction(FilterAction.OnBathroomsSelected(baths)) },
                            label = { Text(baths?.toString() ?: "Any", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BalkanEstatePrimaryBlue, selectedLabelColor = Color.White)
                        )
                    }
                }
            }

            // Area
            FilterSection("Area (m\u00B2)") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = state.minSqftText, onValueChange = { onAction(FilterAction.OnMinSqftChanged(it)) }, label = { Text("Min") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue), singleLine = true)
                    OutlinedTextField(value = state.maxSqftText, onValueChange = { onAction(FilterAction.OnMaxSqftChanged(it)) }, label = { Text("Max") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue), singleLine = true)
                }
            }

            // Furnished
            FilterSection("Furnished") {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(null to "Any", "Unfurnished" to "Unfurnished", "Semi" to "Semi-Furnished", "Fully" to "Fully Furnished").forEach { (value, label) ->
                        FilterChip(
                            selected = state.selectedFurnished == value,
                            onClick = { onAction(FilterAction.OnFurnishedSelected(value)) },
                            label = { Text(label, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BalkanEstatePrimaryBlue, selectedLabelColor = Color.White)
                        )
                    }
                }
            }

            // Amenities
            FilterSection("Amenities") {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Air Conditioning", "Balcony", "Garden", "Swimming Pool", "Garage", "Elevator", "Security System", "Fireplace", "Gym", "Pet Friendly").forEach { amenity ->
                        FilterChip(
                            selected = state.selectedAmenities.contains(amenity),
                            onClick = { onAction(FilterAction.OnAmenityToggle(amenity)) },
                            label = { Text(amenity, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BalkanEstatePrimaryBlue, selectedLabelColor = Color.White)
                        )
                    }
                }
            }

            // Pet Friendly
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Pet Friendly Only", fontWeight = FontWeight.Medium)
                Switch(checked = state.petFriendly, onCheckedChange = { onAction(FilterAction.OnPetFriendlyToggle(it)) }, colors = SwitchDefaults.colors(checkedTrackColor = BalkanEstatePrimaryBlue))
            }

            Spacer(Modifier.height(80.dp))
        }

        // Apply button
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(onClick = { onAction(FilterAction.OnClearFilters) }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("Reset") }
            Button(onClick = { onAction(FilterAction.OnApplyFilters) }, modifier = Modifier.weight(2f), colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue), shape = RoundedCornerShape(12.dp)) { Text("Apply Filters", fontWeight = FontWeight.SemiBold) }
        }
    }
}

@Composable
private fun FilterSection(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}
