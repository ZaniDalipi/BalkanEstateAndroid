package com.zanoapps.property_details.domain.model

import com.zanoapps.property_details.domain.enums.Amenity
import com.zanoapps.property_details.domain.enums.Appliance
import com.zanoapps.property_details.domain.enums.CoolingType
import com.zanoapps.property_details.domain.enums.FurnishedType
import com.zanoapps.property_details.domain.enums.HeatingType
import com.zanoapps.property_details.domain.enums.LeaseLength
import com.zanoapps.property_details.domain.enums.ListingType
import com.zanoapps.property_details.domain.enums.ParkingType
import com.zanoapps.property_details.domain.enums.PetPolicy
import com.zanoapps.property_details.domain.enums.PropertySubType
import com.zanoapps.property_details.domain.enums.PropertyType
import java.time.LocalDateTime

data class CreatePropertyForm(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val currency: String = "EUR",
    val listingType: ListingType? = null,
    val propertyType: PropertyType? = null,
    val propertySubType: PropertySubType? = null,


    val address: String = "",
    val city: String = "",
    val country: String = "Albania",
    val postalCode: String = "",
    val neighborhood: String = "",

    val bedrooms: Int = 1,
    val bathrooms: Int = 1,
    val halfBathrooms: Int = 0,
    val squareFootage: String = "",
    val lotSize: String = "",
    val yearBuilt: String = "",
    val floors: Int = 1,
    val parkingType: ParkingType? = null,
    val parkingSpaces: Int = 0,

    val heating: HeatingType? = null,
    val cooling: CoolingType? = null,
    val furnished: FurnishedType = FurnishedType.UNFURNISHED,
    val petPolicy: PetPolicy = PetPolicy.NEGOTIABLE,
    val selectedAmenities: Set<Amenity> = emptySet(),
    val selectedAppliances: Set<Appliance> = emptySet(),

    // Media
    val selectedImages: List<PropertyImageState> = emptyList(),
    val virtualTourUrl: String = "",
    val floorPlanUrl: String = "",

    // Financial
    val monthlyRent: String = "",
    val securityDeposit: String = "",
    val utilitiesIncluded: Boolean = false,
    val hoaFees: String = "",
    val propertyTax: String = "",
    val insurance: String = "",

    // Rental specific
    val availableFromDate: LocalDateTime? = null,
    val leaseLength: LeaseLength? = null,

    // Agent info
    val agentName: String = "",
    val agentPhone: String = "",
    val agentEmail: String = "",

    // Additional options
    val featured: Boolean = false,
    val urgent: Boolean = false
)

