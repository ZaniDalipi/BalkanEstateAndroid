package com.zanoapps.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PropertyDto(
    @SerialName("_id")
    val id: String,
    val title: String,
    val description: String? = null,
    val price: Double,
    val currency: String = "EUR",
    val imageUrl: String,
    val images: List<String> = emptyList(),
    val bedrooms: Int,
    val bathrooms: Int,
    val squareFootage: Int,
    val address: String,
    val city: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val propertyType: String,
    val listingType: String,
    val agentId: String? = null,
    val agentName: String,
    val agentPhone: String? = null,
    val agentEmail: String? = null,
    val agentImageUrl: String? = null,
    val isFeatured: Boolean = false,
    val isUrgent: Boolean = false,
    val amenities: List<String>? = null,
    val yearBuilt: Int? = null,
    val parkingSpaces: Int? = null,
    val createdAt: Long,
    val updatedAt: Long
)

@Serializable
data class PropertiesResponse(
    val success: Boolean,
    val data: List<PropertyDto>,
    val pagination: PaginationDto? = null
)

@Serializable
data class PropertyResponse(
    val success: Boolean,
    val data: PropertyDto
)

@Serializable
data class PaginationDto(
    val page: Int,
    val limit: Int,
    val total: Int,
    val totalPages: Int
)
