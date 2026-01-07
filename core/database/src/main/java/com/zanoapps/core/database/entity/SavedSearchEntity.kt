package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_searches")
data class SavedSearchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val name: String,
    val country: String?,
    val city: String?,
    val listingType: String?,
    val propertyTypes: String?, // JSON array
    val minPrice: Double?,
    val maxPrice: Double?,
    val minArea: Double?,
    val maxArea: Double?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val amenities: String?, // JSON array
    val notificationsEnabled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastUsedAt: Long = System.currentTimeMillis()
)
