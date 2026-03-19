package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "properties")
data class PropertyEntity(
    @PrimaryKey val id: String,
    val title: String,
    val price: Double,
    val currency: String,
    val imageUrl: String,
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
    val agentName: String,
    val isFeatured: Boolean = false,
    val isUrgent: Boolean = false,
    val description: String = "",
    val yearBuilt: Int = 0,
    val floorNumber: Int = 0,
    val totalFloors: Int = 0,
    val furnished: String = "",
    val parking: String = "",
    val agentPhone: String = "",
    val agentEmail: String = "",
    val agentAvatarUrl: String = "",
    val agentId: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
