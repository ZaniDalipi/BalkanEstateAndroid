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
 * Represents the complete state for the filter screen.
 * Manages all filter options, their selections, and UI state.
 */
data class FilterState(
    // Filter values
    val query: String = "",
    val minPrice: String = "",
    val maxPrice: String = "",
    val selectedPropertyTypes: Set<PropertyType> = emptySet(),
    val selectedListingTypes: Set<ListingType> = emptySet(),
    val minBedrooms: Int? = null,
    val maxBedrooms: Int? = null,
    val minBathrooms: Int? = null,
    val maxBathrooms: Int? = null,
    val minSquareFootage: String = "",
    val maxSquareFootage: String = "",
    val selectedAmenities: Set<Amenity> = emptySet(),
    val selectedFurnishedTypes: Set<FurnishedType> = emptySet(),
    val selectedPetPolicies: Set<PetPolicy> = emptySet(),
    val selectedParkingTypes: Set<ParkingType> = emptySet(),

    // Sort
    val sortOption: SortOption = SortOption.NEWEST,

    // UI State
    val expandedSections: Set<FilterSection> = setOf(FilterSection.LISTING_TYPE, FilterSection.PROPERTY_TYPE),
    val isLoading: Boolean = false,
    val isSortBottomSheetVisible: Boolean = false,
    val isAmenitiesBottomSheetVisible: Boolean = false,

    // Validation
    val priceRangeError: String? = null,
    val squareFootageError: String? = null,

    // Results count (for showing "Show X results" button)
    val matchingResultsCount: Int = 0
) {
    /**
     * Checks if any filters are currently active
     */
    val hasActiveFilters: Boolean
        get() = query.isNotBlank() ||
                minPrice.isNotBlank() ||
                maxPrice.isNotBlank() ||
                selectedPropertyTypes.isNotEmpty() ||
                selectedListingTypes.isNotEmpty() ||
                minBedrooms != null ||
                maxBedrooms != null ||
                minBathrooms != null ||
                maxBathrooms != null ||
                minSquareFootage.isNotBlank() ||
                maxSquareFootage.isNotBlank() ||
                selectedAmenities.isNotEmpty() ||
                selectedFurnishedTypes.isNotEmpty() ||
                selectedPetPolicies.isNotEmpty() ||
                selectedParkingTypes.isNotEmpty()

    /**
     * Gets a count of active filter categories
     */
    val activeFilterCount: Int
        get() {
            var count = 0
            if (query.isNotBlank()) count++
            if (minPrice.isNotBlank() || maxPrice.isNotBlank()) count++
            if (selectedPropertyTypes.isNotEmpty()) count++
            if (selectedListingTypes.isNotEmpty()) count++
            if (minBedrooms != null || maxBedrooms != null) count++
            if (minBathrooms != null || maxBathrooms != null) count++
            if (minSquareFootage.isNotBlank() || maxSquareFootage.isNotBlank()) count++
            if (selectedAmenities.isNotEmpty()) count++
            if (selectedFurnishedTypes.isNotEmpty()) count++
            if (selectedPetPolicies.isNotEmpty()) count++
            if (selectedParkingTypes.isNotEmpty()) count++
            return count
        }

    /**
     * Gets a list of active filter chips for display
     */
    val activeFilterChips: List<FilterChipData>
        get() = buildList {
            selectedListingTypes.forEach {
                add(FilterChipData.ListingTypeChip(it))
            }
            selectedPropertyTypes.forEach {
                add(FilterChipData.PropertyTypeChip(it))
            }
            if (minPrice.isNotBlank() || maxPrice.isNotBlank()) {
                add(FilterChipData.PriceRangeChip(minPrice, maxPrice))
            }
            minBedrooms?.let { min ->
                add(FilterChipData.BedroomsChip(min, maxBedrooms))
            }
            minBathrooms?.let { min ->
                add(FilterChipData.BathroomsChip(min, maxBathrooms))
            }
            if (minSquareFootage.isNotBlank() || maxSquareFootage.isNotBlank()) {
                add(FilterChipData.SquareFootageChip(minSquareFootage, maxSquareFootage))
            }
            selectedAmenities.take(3).forEach {
                add(FilterChipData.AmenityChip(it))
            }
            if (selectedAmenities.size > 3) {
                add(FilterChipData.MoreAmenitiesChip(selectedAmenities.size - 3))
            }
            selectedFurnishedTypes.forEach {
                add(FilterChipData.FurnishedTypeChip(it))
            }
            selectedPetPolicies.forEach {
                add(FilterChipData.PetPolicyChip(it))
            }
            selectedParkingTypes.forEach {
                add(FilterChipData.ParkingTypeChip(it))
            }
        }

    /**
     * Converts the current state to SearchFilters for use in search
     */
    fun toSearchFilters(): SearchFilters {
        return SearchFilters(
            query = query,
            minPrice = minPrice.toDoubleOrNull(),
            maxPrice = maxPrice.toDoubleOrNull(),
            propertyTypes = selectedPropertyTypes,
            bedrooms = minBedrooms,
            bathrooms = minBathrooms,
            minSquareFootage = minSquareFootage.toIntOrNull(),
            maxSquareFootage = maxSquareFootage.toIntOrNull(),
            listingTypes = selectedListingTypes,
            amenities = selectedAmenities,
            furnishedType = selectedFurnishedTypes,
            petPolicy = selectedPetPolicies,
            parkingType = selectedParkingTypes
        )
    }

    companion object {
        /**
         * Creates a FilterState from existing SearchFilters
         */
        fun fromSearchFilters(filters: SearchFilters, sortOption: SortOption = SortOption.NEWEST): FilterState {
            return FilterState(
                query = filters.query,
                minPrice = filters.minPrice?.toString() ?: "",
                maxPrice = filters.maxPrice?.toString() ?: "",
                selectedPropertyTypes = filters.propertyTypes,
                selectedListingTypes = filters.listingTypes,
                minBedrooms = filters.bedrooms,
                minSquareFootage = filters.minSquareFootage?.toString() ?: "",
                maxSquareFootage = filters.maxSquareFootage?.toString() ?: "",
                selectedAmenities = filters.amenities,
                selectedFurnishedTypes = filters.furnishedType,
                selectedPetPolicies = filters.petPolicy,
                selectedParkingTypes = filters.parkingType,
                sortOption = sortOption
            )
        }
    }
}

/**
 * Enum representing filter sections that can be expanded/collapsed
 */
enum class FilterSection {
    LISTING_TYPE,
    PROPERTY_TYPE,
    PRICE_RANGE,
    BEDROOMS_BATHROOMS,
    SQUARE_FOOTAGE,
    AMENITIES,
    FURNISHED,
    PET_POLICY,
    PARKING
}

/**
 * Sealed class representing different types of filter chips
 */
sealed class FilterChipData {
    abstract val displayText: String
    abstract val id: String

    data class ListingTypeChip(val listingType: ListingType) : FilterChipData() {
        override val displayText: String = listingType.displayName
        override val id: String = "listing_${listingType.name}"
    }

    data class PropertyTypeChip(val propertyType: PropertyType) : FilterChipData() {
        override val displayText: String = propertyType.displayName
        override val id: String = "property_${propertyType.name}"
    }

    data class PriceRangeChip(val minPrice: String, val maxPrice: String) : FilterChipData() {
        override val displayText: String = when {
            minPrice.isNotBlank() && maxPrice.isNotBlank() -> "€$minPrice - €$maxPrice"
            minPrice.isNotBlank() -> "€$minPrice+"
            maxPrice.isNotBlank() -> "Up to €$maxPrice"
            else -> "Any price"
        }
        override val id: String = "price_range"
    }

    data class BedroomsChip(val min: Int, val max: Int?) : FilterChipData() {
        override val displayText: String = when {
            max != null && min != max -> "$min-$max beds"
            else -> "$min+ beds"
        }
        override val id: String = "bedrooms"
    }

    data class BathroomsChip(val min: Int, val max: Int?) : FilterChipData() {
        override val displayText: String = when {
            max != null && min != max -> "$min-$max baths"
            else -> "$min+ baths"
        }
        override val id: String = "bathrooms"
    }

    data class SquareFootageChip(val minSqft: String, val maxSqft: String) : FilterChipData() {
        override val displayText: String = when {
            minSqft.isNotBlank() && maxSqft.isNotBlank() -> "$minSqft - $maxSqft m²"
            minSqft.isNotBlank() -> "$minSqft+ m²"
            maxSqft.isNotBlank() -> "Up to $maxSqft m²"
            else -> "Any size"
        }
        override val id: String = "sqft"
    }

    data class AmenityChip(val amenity: Amenity) : FilterChipData() {
        override val displayText: String = amenity.displayName
        override val id: String = "amenity_${amenity.name}"
    }

    data class MoreAmenitiesChip(val count: Int) : FilterChipData() {
        override val displayText: String = "+$count more"
        override val id: String = "amenities_more"
    }

    data class FurnishedTypeChip(val furnishedType: FurnishedType) : FilterChipData() {
        override val displayText: String = furnishedType.displayName
        override val id: String = "furnished_${furnishedType.name}"
    }

    data class PetPolicyChip(val petPolicy: PetPolicy) : FilterChipData() {
        override val displayText: String = petPolicy.displayName
        override val id: String = "pet_${petPolicy.name}"
    }

    data class ParkingTypeChip(val parkingType: ParkingType) : FilterChipData() {
        override val displayText: String = parkingType.displayName
        override val id: String = "parking_${parkingType.name}"
    }
}
