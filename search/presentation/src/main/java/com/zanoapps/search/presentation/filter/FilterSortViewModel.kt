package com.zanoapps.search.presentation.filter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.enums.Amenity
import com.zanoapps.core.domain.enums.FurnishedType
import com.zanoapps.core.domain.enums.ListingType
import com.zanoapps.core.domain.enums.ParkingType
import com.zanoapps.core.domain.enums.PetPolicy
import com.zanoapps.core.domain.enums.PropertyType
import com.zanoapps.core.domain.enums.SortOption
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.FilterValidation
import com.zanoapps.search.domain.model.SearchFilters
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing filter and sort functionality.
 * Handles all filter state, validation, and actions.
 */
class FilterSortViewModel(
    private val initialFilters: SearchFilters = SearchFilters(),
    private val initialSortOption: SortOption = SortOption.NEWEST,
    private val properties: List<BalkanEstateProperty> = emptyList()
) : ViewModel() {

    var state by mutableStateOf(FilterState.fromSearchFilters(initialFilters, initialSortOption))
        private set

    private val eventChannel = Channel<FilterEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        updateMatchingResultsCount()
    }

    fun onAction(action: FilterAction) {
        when (action) {
            // Query
            is FilterAction.OnQueryChanged -> {
                state = state.copy(query = action.query)
                updateMatchingResultsCount()
            }

            // Price range
            is FilterAction.OnMinPriceChanged -> {
                val validatedPrice = validateAndFormatPrice(action.price)
                state = state.copy(
                    minPrice = validatedPrice,
                    priceRangeError = validatePriceRange(validatedPrice, state.maxPrice)
                )
                updateMatchingResultsCount()
            }
            is FilterAction.OnMaxPriceChanged -> {
                val validatedPrice = validateAndFormatPrice(action.price)
                state = state.copy(
                    maxPrice = validatedPrice,
                    priceRangeError = validatePriceRange(state.minPrice, validatedPrice)
                )
                updateMatchingResultsCount()
            }

            // Property types
            is FilterAction.OnPropertyTypeToggled -> {
                val newSelection = state.selectedPropertyTypes.toMutableSet()
                if (newSelection.contains(action.propertyType)) {
                    newSelection.remove(action.propertyType)
                } else {
                    newSelection.add(action.propertyType)
                }
                state = state.copy(selectedPropertyTypes = newSelection)
                updateMatchingResultsCount()
            }
            FilterAction.OnClearPropertyTypes -> {
                state = state.copy(selectedPropertyTypes = emptySet())
                updateMatchingResultsCount()
            }
            FilterAction.OnSelectAllPropertyTypes -> {
                state = state.copy(selectedPropertyTypes = PropertyType.entries.toSet())
                updateMatchingResultsCount()
            }

            // Listing types
            is FilterAction.OnListingTypeToggled -> {
                val newSelection = state.selectedListingTypes.toMutableSet()
                if (newSelection.contains(action.listingType)) {
                    newSelection.remove(action.listingType)
                } else {
                    newSelection.add(action.listingType)
                }
                state = state.copy(selectedListingTypes = newSelection)
                updateMatchingResultsCount()
            }
            FilterAction.OnClearListingTypes -> {
                state = state.copy(selectedListingTypes = emptySet())
                updateMatchingResultsCount()
            }

            // Bedrooms
            is FilterAction.OnMinBedroomsChanged -> {
                state = state.copy(minBedrooms = action.bedrooms)
                updateMatchingResultsCount()
            }
            is FilterAction.OnMaxBedroomsChanged -> {
                state = state.copy(maxBedrooms = action.bedrooms)
                updateMatchingResultsCount()
            }

            // Bathrooms
            is FilterAction.OnMinBathroomsChanged -> {
                state = state.copy(minBathrooms = action.bathrooms)
                updateMatchingResultsCount()
            }
            is FilterAction.OnMaxBathroomsChanged -> {
                state = state.copy(maxBathrooms = action.bathrooms)
                updateMatchingResultsCount()
            }

            // Square footage
            is FilterAction.OnMinSquareFootageChanged -> {
                val validatedSqft = validateAndFormatSquareFootage(action.sqft)
                state = state.copy(
                    minSquareFootage = validatedSqft,
                    squareFootageError = validateSquareFootageRange(validatedSqft, state.maxSquareFootage)
                )
                updateMatchingResultsCount()
            }
            is FilterAction.OnMaxSquareFootageChanged -> {
                val validatedSqft = validateAndFormatSquareFootage(action.sqft)
                state = state.copy(
                    maxSquareFootage = validatedSqft,
                    squareFootageError = validateSquareFootageRange(state.minSquareFootage, validatedSqft)
                )
                updateMatchingResultsCount()
            }

            // Amenities
            is FilterAction.OnAmenityToggled -> {
                val newSelection = state.selectedAmenities.toMutableSet()
                if (newSelection.contains(action.amenity)) {
                    newSelection.remove(action.amenity)
                } else {
                    newSelection.add(action.amenity)
                }
                state = state.copy(selectedAmenities = newSelection)
                updateMatchingResultsCount()
            }
            FilterAction.OnClearAmenities -> {
                state = state.copy(selectedAmenities = emptySet())
                updateMatchingResultsCount()
            }
            FilterAction.OnShowAmenitiesSheet -> {
                state = state.copy(isAmenitiesBottomSheetVisible = true)
            }
            FilterAction.OnHideAmenitiesSheet -> {
                state = state.copy(isAmenitiesBottomSheetVisible = false)
            }

            // Furnished types
            is FilterAction.OnFurnishedTypeToggled -> {
                val newSelection = state.selectedFurnishedTypes.toMutableSet()
                if (newSelection.contains(action.furnishedType)) {
                    newSelection.remove(action.furnishedType)
                } else {
                    newSelection.add(action.furnishedType)
                }
                state = state.copy(selectedFurnishedTypes = newSelection)
                updateMatchingResultsCount()
            }
            FilterAction.OnClearFurnishedTypes -> {
                state = state.copy(selectedFurnishedTypes = emptySet())
                updateMatchingResultsCount()
            }

            // Pet policies
            is FilterAction.OnPetPolicyToggled -> {
                val newSelection = state.selectedPetPolicies.toMutableSet()
                if (newSelection.contains(action.petPolicy)) {
                    newSelection.remove(action.petPolicy)
                } else {
                    newSelection.add(action.petPolicy)
                }
                state = state.copy(selectedPetPolicies = newSelection)
                updateMatchingResultsCount()
            }
            FilterAction.OnClearPetPolicies -> {
                state = state.copy(selectedPetPolicies = emptySet())
                updateMatchingResultsCount()
            }

            // Parking types
            is FilterAction.OnParkingTypeToggled -> {
                val newSelection = state.selectedParkingTypes.toMutableSet()
                if (newSelection.contains(action.parkingType)) {
                    newSelection.remove(action.parkingType)
                } else {
                    newSelection.add(action.parkingType)
                }
                state = state.copy(selectedParkingTypes = newSelection)
                updateMatchingResultsCount()
            }
            FilterAction.OnClearParkingTypes -> {
                state = state.copy(selectedParkingTypes = emptySet())
                updateMatchingResultsCount()
            }

            // Section toggles
            is FilterAction.OnSectionToggled -> {
                val newExpandedSections = state.expandedSections.toMutableSet()
                if (newExpandedSections.contains(action.section)) {
                    newExpandedSections.remove(action.section)
                } else {
                    newExpandedSections.add(action.section)
                }
                state = state.copy(expandedSections = newExpandedSections)
            }
            FilterAction.OnExpandAllSections -> {
                state = state.copy(expandedSections = FilterSection.entries.toSet())
            }
            FilterAction.OnCollapseAllSections -> {
                state = state.copy(expandedSections = emptySet())
            }

            // Sort
            is FilterAction.OnSortOptionChanged -> {
                state = state.copy(sortOption = action.sortOption)
            }
            FilterAction.OnShowSortSheet -> {
                state = state.copy(isSortBottomSheetVisible = true)
            }
            FilterAction.OnHideSortSheet -> {
                state = state.copy(isSortBottomSheetVisible = false)
            }

            // Chip removal
            is FilterAction.OnRemoveFilterChip -> {
                removeFilterChip(action.chipData)
                updateMatchingResultsCount()
            }

            // Apply/Reset
            FilterAction.OnApplyFilters -> {
                if (state.priceRangeError == null && state.squareFootageError == null) {
                    viewModelScope.launch {
                        eventChannel.send(
                            FilterEvent.FiltersApplied(
                                filters = state.toSearchFilters(),
                                sortOption = state.sortOption
                            )
                        )
                    }
                }
            }
            FilterAction.OnResetFilters -> {
                state = FilterState(sortOption = state.sortOption)
                updateMatchingResultsCount()
                viewModelScope.launch {
                    eventChannel.send(FilterEvent.FiltersReset)
                }
            }
            FilterAction.OnResetSort -> {
                state = state.copy(sortOption = SortOption.NEWEST)
            }
            FilterAction.OnResetAll -> {
                state = FilterState()
                updateMatchingResultsCount()
                viewModelScope.launch {
                    eventChannel.send(FilterEvent.FiltersReset)
                }
            }

            // Navigation
            FilterAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(FilterEvent.NavigateBack)
                }
            }
            FilterAction.OnShowResults -> {
                onAction(FilterAction.OnApplyFilters)
            }

            // Initialize
            is FilterAction.OnInitialize -> {
                state = FilterState.fromSearchFilters(action.filters, action.sortOption)
                updateMatchingResultsCount()
            }
        }
    }

    /**
     * Validates and formats a price input string
     */
    private fun validateAndFormatPrice(price: String): String {
        if (price.isBlank()) return ""
        // Allow only digits and one decimal point
        val filtered = price.filter { it.isDigit() || it == '.' }
        // Ensure only one decimal point
        val parts = filtered.split('.')
        return if (parts.size > 2) {
            parts[0] + "." + parts.drop(1).joinToString("")
        } else {
            filtered
        }
    }

    /**
     * Validates price range and returns error message if invalid
     */
    private fun validatePriceRange(minPrice: String, maxPrice: String): String? {
        val min = minPrice.toDoubleOrNull()
        val max = maxPrice.toDoubleOrNull()

        return when (val result = FilterValidation.validatePriceRange(min, max)) {
            is FilterValidation.ValidationResult.Invalid -> result.message
            FilterValidation.ValidationResult.Valid -> null
        }
    }

    /**
     * Validates and formats a square footage input string
     */
    private fun validateAndFormatSquareFootage(sqft: String): String {
        if (sqft.isBlank()) return ""
        return sqft.filter { it.isDigit() }
    }

    /**
     * Validates square footage range and returns error message if invalid
     */
    private fun validateSquareFootageRange(minSqft: String, maxSqft: String): String? {
        val min = minSqft.toIntOrNull()
        val max = maxSqft.toIntOrNull()

        return when (val result = FilterValidation.validateSquareFootageRange(min, max)) {
            is FilterValidation.ValidationResult.Invalid -> result.message
            FilterValidation.ValidationResult.Valid -> null
        }
    }

    /**
     * Removes a filter based on chip data
     */
    private fun removeFilterChip(chipData: FilterChipData) {
        when (chipData) {
            is FilterChipData.ListingTypeChip -> {
                state = state.copy(
                    selectedListingTypes = state.selectedListingTypes - chipData.listingType
                )
            }
            is FilterChipData.PropertyTypeChip -> {
                state = state.copy(
                    selectedPropertyTypes = state.selectedPropertyTypes - chipData.propertyType
                )
            }
            is FilterChipData.PriceRangeChip -> {
                state = state.copy(minPrice = "", maxPrice = "", priceRangeError = null)
            }
            is FilterChipData.BedroomsChip -> {
                state = state.copy(minBedrooms = null, maxBedrooms = null)
            }
            is FilterChipData.BathroomsChip -> {
                state = state.copy(minBathrooms = null, maxBathrooms = null)
            }
            is FilterChipData.SquareFootageChip -> {
                state = state.copy(minSquareFootage = "", maxSquareFootage = "", squareFootageError = null)
            }
            is FilterChipData.AmenityChip -> {
                state = state.copy(
                    selectedAmenities = state.selectedAmenities - chipData.amenity
                )
            }
            is FilterChipData.MoreAmenitiesChip -> {
                // Show amenities sheet when clicking on "more" chip
                state = state.copy(isAmenitiesBottomSheetVisible = true)
            }
            is FilterChipData.FurnishedTypeChip -> {
                state = state.copy(
                    selectedFurnishedTypes = state.selectedFurnishedTypes - chipData.furnishedType
                )
            }
            is FilterChipData.PetPolicyChip -> {
                state = state.copy(
                    selectedPetPolicies = state.selectedPetPolicies - chipData.petPolicy
                )
            }
            is FilterChipData.ParkingTypeChip -> {
                state = state.copy(
                    selectedParkingTypes = state.selectedParkingTypes - chipData.parkingType
                )
            }
        }
    }

    /**
     * Updates the matching results count based on current filters
     */
    private fun updateMatchingResultsCount() {
        val filters = state.toSearchFilters()
        val count = applyFiltersToProperties(properties, filters).size
        state = state.copy(matchingResultsCount = count)
    }

    /**
     * Updates the properties list and recalculates matching count
     */
    fun updateProperties(newProperties: List<BalkanEstateProperty>) {
        updateMatchingResultsCount()
    }

    companion object {
        /**
         * Applies filters to a list of properties and returns filtered results
         */
        fun applyFiltersToProperties(
            properties: List<BalkanEstateProperty>,
            filters: SearchFilters
        ): List<BalkanEstateProperty> {
            return properties.filter { property ->
                // Query filter
                val matchesQuery = if (filters.query.isBlank()) true else {
                    val query = filters.query.lowercase()
                    property.title.lowercase().contains(query) ||
                            property.address.lowercase().contains(query) ||
                            property.city.lowercase().contains(query)
                }

                // Price filter
                val matchesMinPrice = filters.minPrice?.let { property.price >= it } ?: true
                val matchesMaxPrice = filters.maxPrice?.let { property.price <= it } ?: true

                // Property type filter
                val matchesPropertyType = if (filters.propertyTypes.isEmpty()) true else {
                    filters.propertyTypes.any { it.displayName == property.propertyType }
                }

                // Listing type filter
                val matchesListingType = if (filters.listingTypes.isEmpty()) true else {
                    filters.listingTypes.any { it.displayName == property.listingType }
                }

                // Bedrooms filter
                val matchesBedrooms = filters.bedrooms?.let { property.bedrooms >= it } ?: true

                // Bathrooms filter
                val matchesBathrooms = filters.bathrooms?.let { property.bathrooms >= it } ?: true

                // Square footage filter
                val matchesMinSqft = filters.minSquareFootage?.let { property.squareFootage >= it } ?: true
                val matchesMaxSqft = filters.maxSquareFootage?.let { property.squareFootage <= it } ?: true

                // Combine all filters
                matchesQuery &&
                        matchesMinPrice &&
                        matchesMaxPrice &&
                        matchesPropertyType &&
                        matchesListingType &&
                        matchesBedrooms &&
                        matchesBathrooms &&
                        matchesMinSqft &&
                        matchesMaxSqft
            }
        }

        /**
         * Applies sorting to a list of properties
         */
        fun applySortToProperties(
            properties: List<BalkanEstateProperty>,
            sortOption: SortOption
        ): List<BalkanEstateProperty> {
            return when (sortOption) {
                SortOption.PRICE_LOW_TO_HIGH -> properties.sortedBy { it.price }
                SortOption.PRICE_HIGH_TO_LOW -> properties.sortedByDescending { it.price }
                SortOption.NEWEST -> properties // Would sort by date if available
                SortOption.OLDEST -> properties.reversed()
                SortOption.BEDROOMS -> properties.sortedByDescending { it.bedrooms }
                SortOption.SQUARE_FOOTAGE -> properties.sortedByDescending { it.squareFootage }
                SortOption.FEATURED -> properties.sortedByDescending { it.isFeatured }
            }
        }
    }
}
