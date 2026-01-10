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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import com.zanoapps.core.presentation.designsystem.CameraIcon
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateListingScreenRoot(
    viewModel: CreateListingViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onListingCreated: () -> Unit
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateListingScreen(
    state: CreateListingState,
    onAction: (CreateListingAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Listing") },
                navigationIcon = {
                    IconButton(onClick = { onAction(CreateListingAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { onAction(CreateListingAction.OnSaveDraft) }) {
                        Text("Save Draft", color = BalkanEstatePrimaryBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8FAFC))
        ) {
            // Progress indicator
            ProgressSection(
                currentStep = state.currentStep,
                totalSteps = state.totalSteps
            )

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
                    3 -> AmenitiesAndPhotosStep(state, onAction)
                }
            }

            // Navigation buttons
            NavigationButtons(
                currentStep = state.currentStep,
                totalSteps = state.totalSteps,
                isSubmitting = state.isSubmitting,
                onPrevious = { onAction(CreateListingAction.OnPreviousStep) },
                onNext = { onAction(CreateListingAction.OnNextStep) },
                onSubmit = { onAction(CreateListingAction.OnSubmit) }
            )
        }
    }
}

@Composable
private fun ProgressSection(currentStep: Int, totalSteps: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Step ${currentStep + 1} of $totalSteps",
                color = BalkanEstatePrimaryBlue,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = getStepTitle(currentStep),
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { (currentStep + 1).toFloat() / totalSteps },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = BalkanEstatePrimaryBlue,
            trackColor = BalkanEstateGray.copy(alpha = 0.3f)
        )
    }
}

private fun getStepTitle(step: Int): String {
    return when (step) {
        0 -> "Basic Information"
        1 -> "Location"
        2 -> "Property Details"
        3 -> "Amenities & Photos"
        else -> ""
    }
}

@Composable
private fun BasicInfoStep(
    state: CreateListingState,
    onAction: (CreateListingAction) -> Unit
) {
    Column {
        SectionTitle("What type of listing is this?")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ListingType.entries.take(2).forEach { type ->
                SelectableCard(
                    selected = state.listingType == type,
                    label = type.name.lowercase().replaceFirstChar { it.uppercase() },
                    onClick = { onAction(CreateListingAction.OnListingTypeSelected(type)) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (state.validationErrors.containsKey("listingType")) {
            ErrorText(state.validationErrors["listingType"]!!)
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Property Type")

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PropertyType.entries.take(6).chunked(3).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { type ->
                        SelectableCard(
                            selected = state.propertyType == type,
                            label = type.name.lowercase().replaceFirstChar { it.uppercase() },
                            onClick = { onAction(CreateListingAction.OnPropertyTypeSelected(type)) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        if (state.validationErrors.containsKey("propertyType")) {
            ErrorText(state.validationErrors["propertyType"]!!)
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Title")

        OutlinedTextField(
            value = state.title,
            onValueChange = { onAction(CreateListingAction.OnTitleChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., Modern 3BR Apartment in City Center") },
            isError = state.validationErrors.containsKey("title"),
            supportingText = state.validationErrors["title"]?.let { { Text(it, color = Color.Red) } }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("Description")

        OutlinedTextField(
            value = state.description,
            onValueChange = { onAction(CreateListingAction.OnDescriptionChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            placeholder = { Text("Describe your property...") },
            maxLines = 6
        )
    }
}

@Composable
private fun LocationStep(
    state: CreateListingState,
    onAction: (CreateListingAction) -> Unit
) {
    Column {
        SectionTitle("Address")

        OutlinedTextField(
            value = state.address,
            onValueChange = { onAction(CreateListingAction.OnAddressChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Street address") },
            isError = state.validationErrors.containsKey("address"),
            supportingText = state.validationErrors["address"]?.let { { Text(it, color = Color.Red) } }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.city,
                onValueChange = { onAction(CreateListingAction.OnCityChanged(it)) },
                modifier = Modifier.weight(1f),
                placeholder = { Text("City") },
                isError = state.validationErrors.containsKey("city")
            )

            OutlinedTextField(
                value = state.postalCode,
                onValueChange = { onAction(CreateListingAction.OnPostalCodeChanged(it)) },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Postal Code") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("Country")

        OutlinedTextField(
            value = state.country,
            onValueChange = { onAction(CreateListingAction.OnCountryChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Country") }
        )
    }
}

@Composable
private fun DetailsStep(
    state: CreateListingState,
    onAction: (CreateListingAction) -> Unit
) {
    Column {
        SectionTitle("Price")

        OutlinedTextField(
            value = state.price,
            onValueChange = { onAction(CreateListingAction.OnPriceChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            suffix = { Text("€") },
            isError = state.validationErrors.containsKey("price"),
            supportingText = state.validationErrors["price"]?.let { { Text(it, color = Color.Red) } }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                SectionTitle("Bedrooms")
                CounterSelector(
                    value = state.bedrooms,
                    onValueChange = { onAction(CreateListingAction.OnBedroomsChanged(it)) },
                    minValue = 0,
                    maxValue = 10
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                SectionTitle("Bathrooms")
                CounterSelector(
                    value = state.bathrooms,
                    onValueChange = { onAction(CreateListingAction.OnBathroomsChanged(it)) },
                    minValue = 1,
                    maxValue = 10
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Size (m²)")

        OutlinedTextField(
            value = state.squareFootage,
            onValueChange = { onAction(CreateListingAction.OnSquareFootageChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter size") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            suffix = { Text("m²") },
            isError = state.validationErrors.containsKey("squareFootage"),
            supportingText = state.validationErrors["squareFootage"]?.let { { Text(it, color = Color.Red) } }
        )

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Furnished")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FurnishedType.entries.forEach { type ->
                SelectableCard(
                    selected = state.furnished == type,
                    label = type.name.lowercase().replaceFirstChar { it.uppercase() },
                    onClick = { onAction(CreateListingAction.OnFurnishedChanged(type)) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AmenitiesAndPhotosStep(
    state: CreateListingState,
    onAction: (CreateListingAction) -> Unit
) {
    Column {
        SectionTitle("Photos")

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable { /* Open image picker */ },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(
                width = 2.dp,
                color = BalkanEstatePrimaryBlue.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = CameraIcon,
                    contentDescription = null,
                    tint = BalkanEstatePrimaryBlue,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Add Photos",
                    color = BalkanEstatePrimaryBlue,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Upload up to 10 photos",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Amenities")

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Amenity.entries.take(12).forEach { amenity ->
                FilterChip(
                    selected = state.selectedAmenities.contains(amenity),
                    onClick = { onAction(CreateListingAction.OnAmenityToggled(amenity)) },
                    label = { Text(amenity.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }) },
                    leadingIcon = if (state.selectedAmenities.contains(amenity)) {
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
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
private fun ErrorText(message: String) {
    Text(
        text = message,
        color = Color.Red,
        fontSize = 12.sp,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
private fun SelectableCard(
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

@Composable
private fun CounterSelector(
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
                .size(40.dp)
                .clip(CircleShape)
                .background(if (value > minValue) BalkanEstatePrimaryBlue else BalkanEstateGray.copy(alpha = 0.3f))
                .clickable(enabled = value > minValue) { onValueChange(value - 1) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "-",
                color = if (value > minValue) Color.White else Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Text(
            text = value.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.width(40.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (value < maxValue) BalkanEstatePrimaryBlue else BalkanEstateGray.copy(alpha = 0.3f))
                .clickable(enabled = value < maxValue) { onValueChange(value + 1) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                color = if (value < maxValue) Color.White else Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
private fun NavigationButtons(
    currentStep: Int,
    totalSteps: Int,
    isSubmitting: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSubmit: () -> Unit
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
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (currentStep > 0) {
                OutlinedButton(
                    onClick = onPrevious,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Previous")
                }
            }

            Button(
                onClick = if (currentStep == totalSteps - 1) onSubmit else onNext,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
                shape = RoundedCornerShape(8.dp),
                enabled = !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(if (currentStep == totalSteps - 1) "Publish Listing" else "Next")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateListingScreenPreview() {
    BalkanEstateTheme {
        CreateListingScreen(
            state = CreateListingState(),
            onAction = {}
        )
    }
}
