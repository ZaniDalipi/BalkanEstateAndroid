package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agencies")
data class AgencyEntity(
    @PrimaryKey val id: String,
    val name: String,
    val logoUrl: String = "",
    val address: String = "",
    val city: String = "",
    val country: String = "",
    val phone: String = "",
    val email: String = "",
    val website: String = "",
    val rating: Float = 0f,
    val reviewsCount: Int = 0,
    val agentsCount: Int = 0,
    val listingsCount: Int = 0,
    val description: String = "",
    val isVerified: Boolean = false
)
