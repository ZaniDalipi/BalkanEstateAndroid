package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String?,
    val bio: String?,
    val profileImageUrl: String?,
    val role: String, // BUYER, SELLER, AGENT
    val isAgent: Boolean = false,
    val savedSearchesCount: Int = 0,
    val favouritesCount: Int = 0,
    val listingsCount: Int = 0,
    val memberSince: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val syncedAt: Long = System.currentTimeMillis()
)
