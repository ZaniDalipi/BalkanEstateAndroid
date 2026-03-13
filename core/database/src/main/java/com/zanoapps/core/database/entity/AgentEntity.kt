package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agents")
data class AgentEntity(
    @PrimaryKey val id: String,
    val name: String,
    val avatarUrl: String = "",
    val agency: String = "",
    val specialization: String = "",
    val location: String = "",
    val phone: String = "",
    val email: String = "",
    val rating: Float = 0f,
    val reviewsCount: Int = 0,
    val listingsCount: Int = 0,
    val soldCount: Int = 0,
    val yearsExperience: Int = 0,
    val languages: String = "", // comma-separated
    val isVerified: Boolean = false,
    val isPremium: Boolean = false,
    val bio: String = ""
)
