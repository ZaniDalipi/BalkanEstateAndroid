package com.zanoapps.property_details.domain.model

import com.zanoapps.property_details.domain.enums.Amenity
import com.zanoapps.property_details.domain.enums.Appliance
import com.zanoapps.property_details.domain.enums.CoolingType
import com.zanoapps.property_details.domain.enums.FurnishedType
import com.zanoapps.property_details.domain.enums.HeatingType
import com.zanoapps.property_details.domain.enums.LeaseLength
import com.zanoapps.property_details.domain.enums.ListingType
import com.zanoapps.property_details.domain.enums.OwnershipType
import com.zanoapps.property_details.domain.enums.ParkingType
import com.zanoapps.property_details.domain.enums.PetPolicy
import com.zanoapps.property_details.domain.enums.PropertyStatus
import com.zanoapps.property_details.domain.enums.PropertySubType
import com.zanoapps.property_details.domain.enums.PropertyType
import com.zanoapps.property_details.domain.enums.ZoningType
import java.time.LocalDateTime

data class PropertyState(
    val id: String = "",
    val title: String,
    val description: String,
    val price: Double,
    val currency: String,
    val listingType: ListingType,
    val propertyType: PropertyType,
    val propertySubType: PropertySubType?,

    // location attributes
    val address: String,
    val city: String,
    val country: String,
    val postalCode: String?,
    val latitude: Double?,
    val longitude: Double?,
    val neighbourhood: String?,

    // Property Details attributes
    val bedrooms: Int,
    val bathrooms: Int,
    val halfBathrooms: Int = 0,
    val squareFootage: Int,
    val lotSize: Int?,
    val yearBuilt: Int?,
    val floors: Int?,
    val parking: ParkingType?,
    val parkingSpaces: Int = 0,

    // Features and Amenities
    val heating: HeatingType?,
    val cooling: CoolingType?,
    val furnished: FurnishedType,
    val petPolicy: PetPolicy,
    val amenities: List<Amenity> = emptyList(),
    val appliances: List<Appliance> = emptyList(),

    // Media
    val images: List<PropertyImageState> = emptyList(),
    val virtualTour: String?,
    val floorPlan: String?,

    // Financial
    val monthlyRent: Double? = null,
    val securityDeposit: Double? = null,
    val utilitiesIncluded: Boolean = false,
    val hoa: Double? = null,
    val propertyTax: Double? = null,
    val insurance: Double? = null,

    // Agent & Contact
    val agentId: String,
    val agentName: String,
    val agentPhone: String,
    val agentEmail: String,

    // Status
    val status: PropertyStatus,
    val availableFrom: LocalDateTime?,
    val leaseLength: LeaseLength?,

    // Timestamps
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,

    // Additional
    val featured: Boolean = false,
    val urgent: Boolean = false,
    val views: Int = 0,
    val favorites: Int = 0,


    val ownershipType: OwnershipType? = null, // freehold, lasehold etc
    val zoningType: ZoningType? = null, // Residential, comercial etc
   // val propertyDocuments: List<PropertyDocument> = emptyList(),

    val energyPerformance: Int? = null,
    //val energyCertificate: EnergyCertificate = null,
   // val solarPanels: Boolean = false,
   // val electricCharging: Boolean =  false









)
