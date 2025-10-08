package com.zanoapps.onboarding.domain.analyzer.models

import com.zanoapps.onboarding.domain.enums.ClientIntent
import com.zanoapps.onboarding.domain.analyzer.enums.BudgetRange
import com.zanoapps.onboarding.domain.enums.buyer.Amenity
import com.zanoapps.onboarding.domain.enums.buyer.LifeSituation
import com.zanoapps.onboarding.domain.enums.buyer.PropertyIntent

data class PropertyRecommendationProfile(
    val clientIntent: ClientIntent?,
    val lifeSituations: List<LifeSituation>,
    val propertyIntents: List<PropertyIntent>,
    val selectedAmenities: List<Amenity>,
    val suggestedAmenities: List<Amenity>,
    val recommendedPropertyTypes: List<String>,
    val estimatedBudgetRange: BudgetRange,
    val locationPreferences: LocationPreferences,
    val priorityFeatures: List<PriorityFeature>,
    val searchTags: List<String>,
    val confidenceScore: Float
)
