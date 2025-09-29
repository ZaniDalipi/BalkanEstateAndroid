package com.zanoapps.onboarding.domain.models

import com.zanoapps.onboarding.domain.enums.buyer.Amenity
import com.zanoapps.onboarding.domain.enums.buyer.LifeSituation
import com.zanoapps.onboarding.domain.enums.buyer.PropertyIntent

data class SearchQuery(
    val propertyIntent: PropertyIntent,
    val lifeSituation: LifeSituation,
    val amenities: List<Amenity>,
    val filters: Map<String, Any> = emptyMap()

)
