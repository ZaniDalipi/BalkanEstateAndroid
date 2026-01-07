package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agents")
data class AgentEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val imageUrl: String?,
    val bio: String?,
    val agency: String?,
    val licenseNumber: String?,
    val specializations: String?, // JSON array
    val languages: String?, // JSON array
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val listingsCount: Int = 0,
    val soldCount: Int = 0,
    val yearsOfExperience: Int = 0,
    val isVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val syncedAt: Long = System.currentTimeMillis()
)
