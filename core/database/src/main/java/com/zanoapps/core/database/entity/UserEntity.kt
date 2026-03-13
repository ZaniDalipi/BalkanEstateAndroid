package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String = "",
    val avatarUrl: String = "",
    val bio: String = "",
    val location: String = "",
    val memberSince: String = "",
    val isAgent: Boolean = false,
    val isPremium: Boolean = false,
    val accountType: String = "BUYER",
    val listingsCount: Int = 0,
    val savedCount: Int = 0,
    val reviewsCount: Int = 0,
    val token: String = ""
)
