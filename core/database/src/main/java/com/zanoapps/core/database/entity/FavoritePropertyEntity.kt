package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_properties")
data class FavoritePropertyEntity(
    @PrimaryKey
    val propertyId: String,
    val userId: String,
    val saveAt: Long = System.currentTimeMillis()
)
