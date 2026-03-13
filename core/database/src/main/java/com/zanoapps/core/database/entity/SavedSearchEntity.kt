package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_searches")
data class SavedSearchEntity(
    @PrimaryKey val id: String,
    val name: String,
    val query: String = "",
    val location: String = "",
    val propertyType: String = "",
    val priceRange: String = "",
    val bedrooms: String = "",
    val notificationsEnabled: Boolean = true,
    val matchCount: Int = 0,
    val newCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
