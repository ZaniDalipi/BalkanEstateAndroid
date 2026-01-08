package com.zanoapps.search.presentation.filter

import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.domain.enums.Amenity
import com.zanoapps.core.domain.enums.AmenityCategory
import com.zanoapps.core.domain.enums.FurnishedType
import com.zanoapps.core.domain.enums.ListingType
import com.zanoapps.core.domain.enums.ParkingType
import com.zanoapps.core.domain.enums.PetPolicy
import com.zanoapps.core.domain.enums.PropertyType
import com.zanoapps.core.domain.enums.SortOption
import com.zanoapps.core.presentation.designsystem.BackIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateBackground
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.BathroomsIcon
import com.zanoapps.core.presentation.designsystem.BedroomsIcon
import com.zanoapps.core.presentation.designsystem.Poppins
import com.zanoapps.core.presentation.designsystem.SquareMetersIcon
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateActionButton
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateExpandableSection
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateFilterChip
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateNumericRangeInput
import com.zanoapps.core.presentation.designsystem.components.BalkanEstatePriceRangeInput
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateRoomSelector
import com.zanoapps.core.presentation.designsystem.components.FilterChipGroup
import com.zanoapps.core.presentation.designsystem.components.RoomOptions
import com.zanoapps.search.presentation.filter.components.SortBottomSheet
import com.zanoapps.search.presentation.filter.components.SortSelector
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Main filter screen composable
 */
@Composable
fun FilterScreenRoot(
    state: FilterState,
    onAction: (FilterAction) -> Unit,
    events: Flow<FilterEvent>,
    onNavigateBack: () -> Unit,
    onFiltersApplied: (filters: com.zanoapps.search.domain.model.SearchFilters, sortOption: SortOption) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is FilterEvent.FiltersApplied -> {
                    onFiltersApplied(event.filters, event.sortOption)
                }
                FilterEvent.NavigateBack -> {
                    onNavigateBack()
                }
                is FilterEvent.ShowError -> {
                    // Handle error (could show a snackbar)
                }
                FilterEvent.FiltersReset -> {
                    // Filters were reset
                }
            }
        }
    }

    FilterScreen(
        state = state,
        onAction = onAction,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    state: FilterState,
    onAction: (FilterAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BalkanEstateBackground,
        topBar = {
            FilterTopBar(
                activeFilterCount = state.activeFilterCount,
                onBackClick = { onAction(FilterAction.OnBackClick) },
                onResetClick = { onAction(FilterAction.OnResetAll) }
            )
        },
        bottomBar = {
            FilterBottomBar(
                matchingResultsCount = state.matchingResultsCount,
                hasActiveFilters = state.hasActiveFilters,
                onShowResults = { onAction(FilterAction.OnShowResults) },
                onReset = { onAction(FilterAction.OnResetFilters) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Sort selector
            SortSection(
                currentSortOption = state.sortOption,
                onShowSortSheet = { onAction(FilterAction.OnShowSortSheet) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Listing Type Section
            BalkanEstateExpandableSection(
                title = "Listing Type",
                isExpanded = state.expandedSections.contains(FilterSection.LISTING_TYPE),
                onToggle = { onAction(FilterAction.OnSectionToggled(FilterSection.LISTING_TYPE)) },
                badgeCount = state.selectedListingTypes.size,
                showClearButton = true,
                onClear = { onAction(FilterAction.OnClearListingTypes) }
            ) {
                ListingTypeFilter(
                    selectedTypes = state.selectedListingTypes,
                    onToggle = { onAction(FilterAction.OnListingTypeToggled(it)) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Property Type Section
            BalkanEstateExpandableSection(
                title = "Property Type",
                isExpanded = state.expandedSections.contains(FilterSection.PROPERTY_TYPE),
                onToggle = { onAction(FilterAction.OnSectionToggled(FilterSection.PROPERTY_TYPE)) },
                badgeCount = state.selectedPropertyTypes.size,
                showClearButton = true,
                onClear = { onAction(FilterAction.OnClearPropertyTypes) }
            ) {
                PropertyTypeFilter(
                    selectedTypes = state.selectedPropertyTypes,
                    onToggle = { onAction(FilterAction.OnPropertyTypeToggled(it)) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Price Range Section
            BalkanEstateExpandableSection(
                title = "Price Range",
                isExpanded = state.expandedSections.contains(FilterSection.PRICE_RANGE),
                onToggle = { onAction(FilterAction.OnSectionToggled(FilterSection.PRICE_RANGE)) },
                badgeCount = if (state.minPrice.isNotBlank() || state.maxPrice.isNotBlank()) 1 else 0
            ) {
                BalkanEstatePriceRangeInput(
                    minPrice = state.minPrice,
                    maxPrice = state.maxPrice,
                    onMinPriceChanged = { onAction(FilterAction.OnMinPriceChanged(it)) },
                    onMaxPriceChanged = { onAction(FilterAction.OnMaxPriceChanged(it)) },
                    errorMessage = state.priceRangeError
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bedrooms & Bathrooms Section
            BalkanEstateExpandableSection(
                title = "Bedrooms & Bathrooms",
                isExpanded = state.expandedSections.contains(FilterSection.BEDROOMS_BATHROOMS),
                onToggle = { onAction(FilterAction.OnSectionToggled(FilterSection.BEDROOMS_BATHROOMS)) },
                badgeCount = listOfNotNull(state.minBedrooms, state.minBathrooms).size
            ) {
                Column {
                    BalkanEstateRoomSelector(
                        label = "Bedrooms",
                        options = RoomOptions.bedroomOptions,
                        selectedValue = state.minBedrooms,
                        onValueSelected = { onAction(FilterAction.OnMinBedroomsChanged(it)) },
                        icon = BedroomsIcon
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    BalkanEstateRoomSelector(
                        label = "Bathrooms",
                        options = RoomOptions.bathroomOptions,
                        selectedValue = state.minBathrooms,
                        onValueSelected = { onAction(FilterAction.OnMinBathroomsChanged(it)) },
                        icon = BathroomsIcon
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Square Footage Section
            BalkanEstateExpandableSection(
                title = "Size (m²)",
                isExpanded = state.expandedSections.contains(FilterSection.SQUARE_FOOTAGE),
                onToggle = { onAction(FilterAction.OnSectionToggled(FilterSection.SQUARE_FOOTAGE)) },
                badgeCount = if (state.minSquareFootage.isNotBlank() || state.maxSquareFootage.isNotBlank()) 1 else 0,
                leadingIcon = SquareMetersIcon
            ) {
                BalkanEstateNumericRangeInput(
                    minValue = state.minSquareFootage,
                    maxValue = state.maxSquareFootage,
                    onMinValueChanged = { onAction(FilterAction.OnMinSquareFootageChanged(it)) },
                    onMaxValueChanged = { onAction(FilterAction.OnMaxSquareFootageChanged(it)) },
                    unit = "m²",
                    errorMessage = state.squareFootageError
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Amenities Section
            BalkanEstateExpandableSection(
                title = "Amenities",
                isExpanded = state.expandedSections.contains(FilterSection.AMENITIES),
                onToggle = { onAction(FilterAction.OnSectionToggled(FilterSection.AMENITIES)) },
                badgeCount = state.selectedAmenities.size,
                showClearButton = true,
                onClear = { onAction(FilterAction.OnClearAmenities) }
            ) {
                AmenitiesFilter(
                    selectedAmenities = state.selectedAmenities,
                    onToggle = { onAction(FilterAction.OnAmenityToggled(it)) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Furnished Type Section
            BalkanEstateExpandableSection(
                title = "Furnished",
                isExpanded = state.expandedSections.contains(FilterSection.FURNISHED),
                onToggle = { onAction(FilterAction.OnSectionToggled(FilterSection.FURNISHED)) },
                badgeCount = state.selectedFurnishedTypes.size,
                showClearButton = true,
                onClear = { onAction(FilterAction.OnClearFurnishedTypes) }
            ) {
                FurnishedTypeFilter(
                    selectedTypes = state.selectedFurnishedTypes,
                    onToggle = { onAction(FilterAction.OnFurnishedTypeToggled(it)) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Pet Policy Section
            BalkanEstateExpandableSection(
                title = "Pet Policy",
                isExpanded = state.expandedSections.contains(FilterSection.PET_POLICY),
                onToggle = { onAction(FilterAction.OnSectionToggled(FilterSection.PET_POLICY)) },
                badgeCount = state.selectedPetPolicies.size,
                showClearButton = true,
                onClear = { onAction(FilterAction.OnClearPetPolicies) }
            ) {
                PetPolicyFilter(
                    selectedPolicies = state.selectedPetPolicies,
                    onToggle = { onAction(FilterAction.OnPetPolicyToggled(it)) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Parking Section
            BalkanEstateExpandableSection(
                title = "Parking",
                isExpanded = state.expandedSections.contains(FilterSection.PARKING),
                onToggle = { onAction(FilterAction.OnSectionToggled(FilterSection.PARKING)) },
                badgeCount = state.selectedParkingTypes.size,
                showClearButton = true,
                onClear = { onAction(FilterAction.OnClearParkingTypes) }
            ) {
                ParkingTypeFilter(
                    selectedTypes = state.selectedParkingTypes,
                    onToggle = { onAction(FilterAction.OnParkingTypeToggled(it)) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Sort bottom sheet
    SortBottomSheet(
        isVisible = state.isSortBottomSheetVisible,
        currentSortOption = state.sortOption,
        onSortOptionSelected = { onAction(FilterAction.OnSortOptionChanged(it)) },
        onDismiss = { onAction(FilterAction.OnHideSortSheet) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterTopBar(
    activeFilterCount: Int,
    onBackClick: () -> Unit,
    onResetClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Filters",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins
                )
                if (activeFilterCount > 0) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = activeFilterCount.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = BackIcon,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        actions = {
            if (activeFilterCount > 0) {
                TextButton(onClick = onResetClick) {
                    Text(
                        text = "Reset All",
                        fontSize = 14.sp,
                        fontFamily = Poppins,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BalkanEstateBackground
        )
    )
}

@Composable
private fun FilterBottomBar(
    matchingResultsCount: Int,
    hasActiveFilters: Boolean,
    onShowResults: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
            thickness = 1.dp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (hasActiveFilters) {
                TextButton(
                    onClick = onReset,
                    modifier = Modifier.weight(0.4f)
                ) {
                    Text(
                        text = "Clear Filters",
                        fontSize = 14.sp,
                        fontFamily = Poppins,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            BalkanEstateActionButton(
                text = if (matchingResultsCount > 0) {
                    "Show $matchingResultsCount Results"
                } else {
                    "Show Results"
                },
                isLoading = false,
                enabled = true,
                onClick = onShowResults,
                modifier = Modifier.weight(if (hasActiveFilters) 0.6f else 1f)
            )
        }
    }
}

@Composable
private fun SortSection(
    currentSortOption: SortOption,
    onShowSortSheet: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Sort & Filter",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Poppins,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        SortSelector(
            currentSortOption = currentSortOption,
            onClick = onShowSortSheet
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ListingTypeFilter(
    selectedTypes: Set<ListingType>,
    onToggle: (ListingType) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ListingType.entries.forEach { type ->
            BalkanEstateFilterChip(
                text = type.displayName,
                isSelected = selectedTypes.contains(type),
                onClick = { onToggle(type) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PropertyTypeFilter(
    selectedTypes: Set<PropertyType>,
    onToggle: (PropertyType) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PropertyType.entries.forEach { type ->
            BalkanEstateFilterChip(
                text = type.displayName,
                isSelected = selectedTypes.contains(type),
                onClick = { onToggle(type) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AmenitiesFilter(
    selectedAmenities: Set<Amenity>,
    onToggle: (Amenity) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Show popular amenities first
        Text(
            text = "Popular",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Poppins,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Amenity.getPopularAmenities().forEach { amenity ->
                BalkanEstateFilterChip(
                    text = amenity.displayName,
                    isSelected = selectedAmenities.contains(amenity),
                    onClick = { onToggle(amenity) }
                )
            }
        }

        // Show by category
        AmenityCategory.entries.forEach { category ->
            val amenitiesInCategory = Amenity.getByCategory(category)
                .filter { it !in Amenity.getPopularAmenities() }

            if (amenitiesInCategory.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = category.displayName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Poppins,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    amenitiesInCategory.forEach { amenity ->
                        BalkanEstateFilterChip(
                            text = amenity.displayName,
                            isSelected = selectedAmenities.contains(amenity),
                            onClick = { onToggle(amenity) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FurnishedTypeFilter(
    selectedTypes: Set<FurnishedType>,
    onToggle: (FurnishedType) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FurnishedType.entries.forEach { type ->
            BalkanEstateFilterChip(
                text = type.displayName,
                isSelected = selectedTypes.contains(type),
                onClick = { onToggle(type) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PetPolicyFilter(
    selectedPolicies: Set<PetPolicy>,
    onToggle: (PetPolicy) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PetPolicy.entries.forEach { policy ->
            BalkanEstateFilterChip(
                text = policy.displayName,
                isSelected = selectedPolicies.contains(policy),
                onClick = { onToggle(policy) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ParkingTypeFilter(
    selectedTypes: Set<ParkingType>,
    onToggle: (ParkingType) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ParkingType.entries.forEach { type ->
            BalkanEstateFilterChip(
                text = type.displayName,
                isSelected = selectedTypes.contains(type),
                onClick = { onToggle(type) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterScreenPreview() {
    BalkanEstateTheme {
        FilterScreen(
            state = FilterState(
                selectedListingTypes = setOf(ListingType.SALE),
                selectedPropertyTypes = setOf(PropertyType.APARTMENT, PropertyType.HOUSE),
                minBedrooms = 2,
                expandedSections = setOf(
                    FilterSection.LISTING_TYPE,
                    FilterSection.PROPERTY_TYPE,
                    FilterSection.BEDROOMS_BATHROOMS
                )
            ),
            onAction = {}
        )
    }
}
