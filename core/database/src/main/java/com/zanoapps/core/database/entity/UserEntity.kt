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
    val createdAt: Long,
    val updatedAt: Long,
    val syncedAt: Long = System.currentTimeMillis()
)
