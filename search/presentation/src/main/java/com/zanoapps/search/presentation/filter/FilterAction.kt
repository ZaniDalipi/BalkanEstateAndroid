package com.zanoapps.search.presentation.filter

import com.zanoapps.core.domain.enums.Amenity
import com.zanoapps.core.domain.enums.FurnishedType
import com.zanoapps.core.domain.enums.ListingType
import com.zanoapps.core.domain.enums.ParkingType
import com.zanoapps.core.domain.enums.PetPolicy
import com.zanoapps.core.domain.enums.PropertyType
import com.zanoapps.core.domain.enums.SortOption
import com.zanoapps.search.domain.model.SearchFilters

/**
 * Sealed interface representing all possible actions in the Filter/Sort screen.
 * Follows the MVI pattern for unidirectional data flow.
 */
sealed interface FilterAction {

    // Query actions
    data class OnQueryChanged(val query: String) : FilterAction

    // Price range actions
    data class OnMinPriceChanged(val price: String) : FilterAction
    data class OnMaxPriceChanged(val price: String) : FilterAction

    // Property type actions
    data class OnPropertyTypeToggled(val propertyType: PropertyType) : FilterAction
    data object OnClearPropertyTypes : FilterAction
    data object OnSelectAllPropertyTypes : FilterAction

    // Listing type actions
    data class OnListingTypeToggled(val listingType: ListingType) : FilterAction
    data object OnClearListingTypes : FilterAction

    // Bedroom/Bathroom actions
    data class OnMinBedroomsChanged(val bedrooms: Int?) : FilterAction
    data class OnMaxBedroomsChanged(val bedrooms: Int?) : FilterAction
    data class OnMinBathroomsChanged(val bathrooms: Int?) : FilterAction
    data class OnMaxBathroomsChanged(val bathrooms: Int?) : FilterAction

    // Square footage actions
    data class OnMinSquareFootageChanged(val sqft: String) : FilterAction
    data class OnMaxSquareFootageChanged(val sqft: String) : FilterAction

    // Amenities actions
    data class OnAmenityToggled(val amenity: Amenity) : FilterAction
    data object OnClearAmenities : FilterAction
    data object OnShowAmenitiesSheet : FilterAction
    data object OnHideAmenitiesSheet : FilterAction

    // Furnished type actions
    data class OnFurnishedTypeToggled(val furnishedType: FurnishedType) : FilterAction
    data object OnClearFurnishedTypes : FilterAction

    // Pet policy actions
    data class OnPetPolicyToggled(val petPolicy: PetPolicy) : FilterAction
    data object OnClearPetPolicies : FilterAction

    // Parking type actions
    data class OnParkingTypeToggled(val parkingType: ParkingType) : FilterAction
    data object OnClearParkingTypes : FilterAction

    // Section expand/collapse actions
    data class OnSectionToggled(val section: FilterSection) : FilterAction
    data object OnExpandAllSections : FilterAction
    data object OnCollapseAllSections : FilterAction

    // Sort actions
    data class OnSortOptionChanged(val sortOption: SortOption) : FilterAction
    data object OnShowSortSheet : FilterAction
    data object OnHideSortSheet : FilterAction

    // Chip removal actions
    data class OnRemoveFilterChip(val chipData: FilterChipData) : FilterAction

    // Apply/Reset actions
    data object OnApplyFilters : FilterAction
    data object OnResetFilters : FilterAction
    data object OnResetSort : FilterAction
    data object OnResetAll : FilterAction

    // Navigation actions
    data object OnBackClick : FilterAction
    data object OnShowResults : FilterAction

    // Initialize with existing filters
    data class OnInitialize(val filters: SearchFilters, val sortOption: SortOption) : FilterAction
}

/**
 * Sealed interface for one-time events from the filter screen.
 * These are consumed by the UI and not persisted in state.
 */
sealed interface FilterEvent {
    data class FiltersApplied(val filters: SearchFilters, val sortOption: SortOption) : FilterEvent
    data object NavigateBack : FilterEvent
    data class ShowError(val message: String) : FilterEvent
    data object FiltersReset : FilterEvent
}
