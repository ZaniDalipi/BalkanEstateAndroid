package com.zanoapps.property_details.presentation.create

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BackIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.CameraIcon
import com.zanoapps.core.presentation.designsystem.KeyboardArrowDownIcon
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateListingScreenRoot(
    viewModel: CreateListingViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    CreateListingScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                CreateListingAction.OnBackClick -> onNavigateBack()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CreateListingScreen(
    state: CreateListingState,
    onAction: (CreateListingAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onAction(CreateListingAction.OnBackClick) }) {
                Icon(BackIcon, "Back", Modifier.size(20.dp))
            }
            Text(
                text = "Create New Listing",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        }

        // Progress
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Step ${state.currentStep + 1} of ${state.totalSteps}",
                fontSize = 12.sp,
                color = BalkanEstateGray
            )
            Spacer(Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { (state.currentStep + 1).toFloat() / state.totalSteps },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = BalkanEstatePrimaryBlue,
                trackColor = Color(0xFFE2E8F0)
            )
            Spacer(Modifier.height(4.dp))
            val stepNames = listOf("Basic Info", "Location", "Details", "Amenities", "Photos")
            Text(stepNames[state.currentStep], fontWeight = FontWeight.SemiBold, color = BalkanEstatePrimaryBlue)
        }

        // Step content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            when (state.currentStep) {
                0 -> BasicInfoStep(state, onAction)
                1 -> LocationStep(state, onAction)
                2 -> DetailsStep(state, onAction)
                3 -> AmenitiesStep(state, onAction)
                4 -> PhotosStep(state, onAction)
            }
        }

        // Navigation buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.currentStep > 0) {
                OutlinedButton(
                    onClick = { onAction(CreateListingAction.OnPreviousStep) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Previous") }
            }
            if (state.currentStep < state.totalSteps - 1) {
                Button(
                    onClick = { onAction(CreateListingAction.OnNextStep) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Next") }
            } else {
                Button(
                    onClick = { onAction(CreateListingAction.OnSubmitListing) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !state.isSubmitting
                ) {
                    if (state.isSubmitting) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Submit Listing", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BasicInfoStep(state: CreateListingState, onAction: (CreateListingAction) -> Unit) {
    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp)) {
            Text("Basic Information", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = state.title, onValueChange = { onAction(CreateListingAction.OnTitleChanged(it)) }, label = { Text("Property Title") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue))
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = state.description, onValueChange = { onAction(CreateListingAction.OnDescriptionChanged(it)) }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth().height(120.dp), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue), maxLines = 5)
            Spacer(Modifier.height(8.dp))
            DropdownSelector("Property Type", state.propertyType, propertyTypes) { onAction(CreateListingAction.OnPropertyTypeChanged(it)) }
            Spacer(Modifier.height(8.dp))
            Text("Listing Type", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = BalkanEstateGray)
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listingTypes.forEach { type ->
                    FilterChip(
                        selected = state.listingType == type,
                        onClick = { onAction(CreateListingAction.OnListingTypeChanged(type)) },
                        label = { Text(type) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BalkanEstatePrimaryBlue, selectedLabelColor = Color.White)
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationStep(state: CreateListingState, onAction: (CreateListingAction) -> Unit) {
    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp)) {
            Text("Location", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = state.address, onValueChange = { onAction(CreateListingAction.OnAddressChanged(it)) }, label = { Text("Street Address") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue))
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = state.city, onValueChange = { onAction(CreateListingAction.OnCityChanged(it)) }, label = { Text("City") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue))
            Spacer(Modifier.height(8.dp))
            DropdownSelector("Country", state.country, balkanCountries) { onAction(CreateListingAction.OnCountryChanged(it)) }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = state.postalCode, onValueChange = { onAction(CreateListingAction.OnPostalCodeChanged(it)) }, label = { Text("Postal Code") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue))
        }
    }
}

@Composable
private fun DetailsStep(state: CreateListingState, onAction: (CreateListingAction) -> Unit) {
    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp)) {
            Text("Property Details", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = state.price, onValueChange = { onAction(CreateListingAction.OnPriceChanged(it)) }, label = { Text("Price") }, modifier = Modifier.weight(2f), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue))
                Box(Modifier.weight(1f)) { DropdownSelector("", state.currency, currencies) { onAction(CreateListingAction.OnCurrencyChanged(it)) } }
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = state.bedrooms, onValueChange = { onAction(CreateListingAction.OnBedroomsChanged(it)) }, label = { Text("Beds") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue))
                OutlinedTextField(value = state.bathrooms, onValueChange = { onAction(CreateListingAction.OnBathroomsChanged(it)) }, label = { Text("Baths") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue))
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = state.squareFootage, onValueChange = { onAction(CreateListingAction.OnSquareFootageChanged(it)) }, label = { Text("Area (m\u00B2)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue))
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = state.yearBuilt, onValueChange = { onAction(CreateListingAction.OnYearBuiltChanged(it)) }, label = { Text("Year Built") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue))
                OutlinedTextField(value = state.parkingSpaces, onValueChange = { onAction(CreateListingAction.OnParkingChanged(it)) }, label = { Text("Parking") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue))
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = state.floorNumber, onValueChange = { onAction(CreateListingAction.OnFloorChanged(it)) }, label = { Text("Floor Number") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AmenitiesStep(state: CreateListingState, onAction: (CreateListingAction) -> Unit) {
    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp)) {
            Text("Amenities & Features", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            DropdownSelector("Furnished", state.furnishedType, furnishedTypes) { onAction(CreateListingAction.OnFurnishedTypeChanged(it)) }
            Spacer(Modifier.height(8.dp))
            DropdownSelector("Heating", state.heatingType, heatingTypes) { onAction(CreateListingAction.OnHeatingTypeChanged(it)) }
            Spacer(Modifier.height(12.dp))
            Text("Select Amenities", fontWeight = FontWeight.Medium, color = BalkanEstateGray)
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                availableAmenities.forEach { amenity ->
                    FilterChip(
                        selected = state.selectedAmenities.contains(amenity),
                        onClick = { onAction(CreateListingAction.OnAmenityToggle(amenity)) },
                        label = { Text(amenity, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BalkanEstatePrimaryBlue, selectedLabelColor = Color.White)
                    )
                }
            }
        }
    }
}

@Composable
private fun PhotosStep(state: CreateListingState, onAction: (CreateListingAction) -> Unit) {
    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp)) {
            Text("Photos", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Text("Add photos of your property (up to 20)", fontSize = 13.sp, color = BalkanEstateGray)
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(2.dp, BalkanEstatePrimaryBlue.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .background(BalkanEstatePrimaryBlue.copy(alpha = 0.03f))
                    .clickable { onAction(CreateListingAction.OnPhotoAdded("photo_${System.currentTimeMillis()}")) },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(CameraIcon, null, Modifier.size(40.dp), tint = BalkanEstatePrimaryBlue)
                    Spacer(Modifier.height(8.dp))
                    Text("Tap to add photos", color = BalkanEstatePrimaryBlue, fontWeight = FontWeight.Medium)
                    Text("JPG, PNG up to 10MB each", fontSize = 12.sp, color = BalkanEstateGray)
                }
            }
            if (state.photoUris.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text("${state.photoUris.size} photos added", fontWeight = FontWeight.Medium, color = BalkanEstatePrimaryBlue)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownSelector(
    label: String,
    selectedValue: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selectedValue.ifEmpty { label.ifEmpty { "Select..." } },
            onValueChange = {},
            readOnly = true,
            label = if (label.isNotEmpty()) { { Text(label) } } else null,
            trailingIcon = { Icon(KeyboardArrowDownIcon, null, Modifier.size(20.dp)) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { onSelect(option); expanded = false }
                )
            }
        }
    }
}
