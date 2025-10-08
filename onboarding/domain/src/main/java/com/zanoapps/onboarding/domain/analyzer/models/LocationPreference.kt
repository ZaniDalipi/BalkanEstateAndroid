package com.zanoapps.onboarding.domain.analyzer.models

import com.zanoapps.onboarding.domain.analyzer.enums.LocationType
import com.zanoapps.onboarding.domain.analyzer.enums.WalkabilityScore

data class LocationPreferences(
    val preferredLocationType: LocationType,
    val proximityToTransit: Boolean,
    val proximityToSchools: Boolean,
    val proximityToHealthcare: Boolean,
    val walkabilityScore: WalkabilityScore
)

