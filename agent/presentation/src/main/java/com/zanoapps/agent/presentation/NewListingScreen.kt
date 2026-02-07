package com.zanoapps.agent.presentation

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.CameraIcon
import com.zanoapps.core.presentation.designsystem.Poppins

data class NewListingState(
    val title: String = "",
    val description: String = "",
    val propertyType: String = "",
    val price: String = "",
    val bedrooms: String = "",
    val bathrooms: String = "",
    val area: String = "",
    val address: String = "",
    val city: String = "",
    val country: String = "",
    val selectedAmenities: List<String> = emptyList(),
    val images: List<String> = emptyList()
)

@Composable
fun NewListingScreenRoot(
    onBackClick: () -> Unit,
    onSubmit: (NewListingState) -> Unit,
    modifier: Modifier = Modifier
) {
    NewListingScreen(
        onBackClick = onBackClick,
        onSubmit = onSubmit,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewListingScreen(
    onBackClick: () -> Unit,
    onSubmit: (NewListingState) -> Unit,
    modifier: Modifier = Modifier
) {
    var state by remember { mutableStateOf(NewListingState()) }

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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.DarkGray
                        )
                    }
                    Text(
                        text = "Create New Listing",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        fontFamily = Poppins
                    )
                }
            }

            // Form Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Photos Section
                FormSection(title = "Photos") {
                    PhotosUploadSection(
                        images = state.images,
                        onAddPhotoClick = { /* Handle add photo */ }
                    )
                }

                // Basic Info Section
                FormSection(title = "Basic Information") {
                    StyledTextField(
                        value = state.title,
                        onValueChange = { state = state.copy(title = it) },
                        label = "Property Title",
                        placeholder = "e.g., Modern Apartment in City Center"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    StyledTextField(
                        value = state.description,
                        onValueChange = { state = state.copy(description = it) },
                        label = "Description",
                        placeholder = "Describe your property...",
                        minLines = 4,
                        maxLines = 6
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PropertyTypeDropdown(
                        selected = state.propertyType,
                        onSelect = { state = state.copy(propertyType = it) }
                    )
                }

                // Price Section
                FormSection(title = "Pricing") {
                    StyledTextField(
                        value = state.price,
                        onValueChange = { state = state.copy(price = it) },
                        label = "Price (EUR)",
                        placeholder = "e.g., 150000",
                        keyboardType = KeyboardType.Number
                    )
                }

                // Details Section
                FormSection(title = "Property Details") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StyledTextField(
                            value = state.bedrooms,
                            onValueChange = { state = state.copy(bedrooms = it) },
                            label = "Bedrooms",
                            placeholder = "0",
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.weight(1f)
                        )

                        StyledTextField(
                            value = state.bathrooms,
                            onValueChange = { state = state.copy(bathrooms = it) },
                            label = "Bathrooms",
                            placeholder = "0",
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    StyledTextField(
                        value = state.area,
                        onValueChange = { state = state.copy(area = it) },
                        label = "Area (m²)",
                        placeholder = "e.g., 120",
                        keyboardType = KeyboardType.Number
                    )
                }

                // Location Section
                FormSection(title = "Location") {
                    StyledTextField(
                        value = state.address,
                        onValueChange = { state = state.copy(address = it) },
                        label = "Address",
                        placeholder = "Street address"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StyledTextField(
                            value = state.city,
                            onValueChange = { state = state.copy(city = it) },
                            label = "City",
                            placeholder = "City",
                            modifier = Modifier.weight(1f)
                        )

                        CountryDropdown(
                            selected = state.country,
                            onSelect = { state = state.copy(country = it) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Amenities Section
                FormSection(title = "Amenities") {
                    AmenitiesSelector(
                        selected = state.selectedAmenities,
                        onToggle = { amenity ->
                            val newList = if (state.selectedAmenities.contains(amenity)) {
                                state.selectedAmenities - amenity
                            } else {
                                state.selectedAmenities + amenity
                            }
                            state = state.copy(selectedAmenities = newList)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Submit Button
                Button(
                    onClick = { onSubmit(state) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BalkanEstatePrimaryBlue
                    )
                ) {
                    Text(
                        text = "Publish Listing",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun FormSection(
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
private fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1,
    maxLines: Int = 1
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.Gray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BalkanEstatePrimaryBlue,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            minLines = minLines,
            maxLines = maxLines
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PropertyTypeDropdown(
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val types = listOf("Apartment", "House", "Villa", "Studio", "Commercial", "Land")

    Column {
        Text(
            text = "Property Type",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selected.ifEmpty { "Select type" },
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BalkanEstatePrimaryBlue,
                    unfocusedBorderColor = Color.LightGray
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                types.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            onSelect(type)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountryDropdown(
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val countries = listOf(
        "Albania", "Serbia", "North Macedonia", "Kosovo",
        "Montenegro", "Bosnia", "Croatia", "Slovenia", "Bulgaria", "Greece"
    )

    Column(modifier = modifier) {
        Text(
            text = "Country",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selected.ifEmpty { "Select" },
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BalkanEstatePrimaryBlue,
                    unfocusedBorderColor = Color.LightGray
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                countries.forEach { country ->
                    DropdownMenuItem(
                        text = { Text(country) },
                        onClick = {
                            onSelect(country)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PhotosUploadSection(
    images: List<String>,
    onAddPhotoClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Add Photo Button
        Box(
            modifier = Modifier
                .size(100.dp)
                .border(
                    width = 2.dp,
                    color = BalkanEstatePrimaryBlue.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp)
                )
                .background(
                    BalkanEstatePrimaryBlue.copy(alpha = 0.05f),
                    RoundedCornerShape(12.dp)
                )
                .clickable { onAddPhotoClick() },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = CameraIcon,
                    contentDescription = "Add Photo",
                    tint = BalkanEstatePrimaryBlue,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Add Photo",
                    fontSize = 12.sp,
                    color = BalkanEstatePrimaryBlue,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Placeholder for images
        repeat(3) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun AmenitiesSelector(
    selected: List<String>,
    onToggle: (String) -> Unit
) {
    val amenities = listOf(
        "Parking", "Elevator", "Balcony", "Garden",
        "Pool", "Gym", "Security", "Central Heating",
        "Air Conditioning", "Furnished", "Storage", "Garage"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        amenities.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { amenity ->
                    AmenityChip(
                        text = amenity,
                        isSelected = selected.contains(amenity),
                        onClick = { onToggle(amenity) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill remaining space if row is not complete
                repeat(3 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun AmenityChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = if (isSelected) BalkanEstatePrimaryBlue else Color(0xFFF0F0F0),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (isSelected) Color.White else Color.DarkGray,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NewListingScreenPreview() {
    BalkanEstateTheme {
        NewListingScreen(
            onBackClick = {},
            onSubmit = {}
        )
    }
}
