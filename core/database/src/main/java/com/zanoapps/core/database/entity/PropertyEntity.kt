package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "properties")
data class PropertyEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String?,
    val price: Double,
    val currency: String,
    val imageUrl: String,
    val images: String, // JSON array of image URLs
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
    val agentId: String?,
    val agentName: String,
    val agentPhone: String?,
    val agentEmail: String?,
    val agentImageUrl: String?,
    val isFeatured: Boolean = false,
    val isUrgent: Boolean = false,
    val amenities: String?, // JSON array
    val yearBuilt: Int?,
    val parkingSpaces: Int?,
    val createdAt: Long,
    val updatedAt: Long,
    val syncedAt: Long = System.currentTimeMillis()
)
