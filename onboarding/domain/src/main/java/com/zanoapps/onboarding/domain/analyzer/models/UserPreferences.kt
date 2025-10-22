package com.zanoapps.onboarding.domain.analyzer.models

import com.zanoapps.onboarding.domain.enums.ClientIntent
import com.zanoapps.onboarding.domain.enums.buyer.Amenity
import com.zanoapps.onboarding.domain.enums.buyer.LifeSituation
import com.zanoapps.onboarding.domain.enums.buyer.PropertyIntent

// Create this new file: UserPreferences.kt
data class UserPreferences(
    val clientIntent: ClientIntent? = null,
    val lifeSituations: LifeSituation? = null,
    val propertyIntents: PropertyIntent? = null,
    val amenities: List<Amenity> = emptyList(),
    val timestamp: Long = System.currentTimeMillis(),
    val userId: String? = null
)