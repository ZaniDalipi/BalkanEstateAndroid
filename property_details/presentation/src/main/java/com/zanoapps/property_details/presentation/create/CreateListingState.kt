package com.zanoapps.property_details.presentation.create

data class CreateListingState(
    val currentStep: Int = 0,
    val totalSteps: Int = 5,
    // Step 1: Basic Info
    val title: String = "",
    val description: String = "",
    val propertyType: String = "",
    val listingType: String = "Sale",
    // Step 2: Location
    val address: String = "",
    val city: String = "",
    val country: String = "Albania",
    val postalCode: String = "",
    // Step 3: Details
    val price: String = "",
    val currency: String = "EUR",
    val bedrooms: String = "",
    val bathrooms: String = "",
    val squareFootage: String = "",
    val yearBuilt: String = "",
    val parkingSpaces: String = "",
    val floorNumber: String = "",
    // Step 4: Amenities
    val selectedAmenities: Set<String> = emptySet(),
    val furnishedType: String = "",
    val heatingType: String = "",
    val coolingType: String = "",
    // Step 5: Photos
    val photoUris: List<String> = emptyList(),
    // State management
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val isFormValid: Boolean = false
)

val propertyTypes = listOf("Apartment", "House", "Villa", "Studio", "Penthouse", "Commercial", "Land", "Office")
val listingTypes = listOf("Sale", "Rent")
val currencies = listOf("EUR", "USD", "ALL", "RSD", "MKD")
val furnishedTypes = listOf("Unfurnished", "Semi-Furnished", "Fully Furnished")
val heatingTypes = listOf("Central", "Electric", "Gas", "Wood", "Solar", "None")
val availableAmenities = listOf(
    "Air Conditioning", "Balcony", "Garden", "Swimming Pool", "Garage",
    "Elevator", "Security System", "Fireplace", "Laundry Room", "Storage",
    "Pet Friendly", "Gym", "Sauna", "Terrace", "Smart Home",
    "Solar Panels", "Water Tank", "Generator", "CCTV", "Intercom"
)
val balkanCountries = listOf("Albania", "Kosovo", "North Macedonia", "Serbia", "Montenegro", "Bosnia & Herzegovina", "Croatia", "Slovenia", "Greece", "Bulgaria", "Romania")
