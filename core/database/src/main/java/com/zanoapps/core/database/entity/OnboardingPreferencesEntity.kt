package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "onboarding_preferences")
data class OnboardingPreferencesEntity(
    @PrimaryKey
    val userId: String,

    // Client type: BUYER or SELLER
    val clientIntent: String? = null,

    // Buyer preferences
    val lifeSituation: String? = null,
    val propertyIntent: String? = null,
    val amenities: String? = null, // JSON array of amenity names

    // Seller preferences
    val propertyTypeSeller: String? = null,
    val sellingTime: String? = null,
    val mainGoal: String? = null,

    // Metadata
    val isOnboardingCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
