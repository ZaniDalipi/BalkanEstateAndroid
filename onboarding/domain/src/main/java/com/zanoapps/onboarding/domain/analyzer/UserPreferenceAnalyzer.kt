package com.zanoapps.onboarding.domain.analyzer

import com.zanoapps.onboarding.domain.analyzer.enums.BudgetRange
import com.zanoapps.onboarding.domain.analyzer.enums.LocationType
import com.zanoapps.onboarding.domain.analyzer.enums.Priority
import com.zanoapps.onboarding.domain.analyzer.enums.WalkabilityScore
import com.zanoapps.onboarding.domain.analyzer.models.LocationPreferences
import com.zanoapps.onboarding.domain.analyzer.models.PriorityFeature
import com.zanoapps.onboarding.domain.analyzer.models.PropertyRecommendationProfile
import com.zanoapps.onboarding.domain.enums.ClientIntent
import com.zanoapps.onboarding.domain.enums.buyer.Amenity
import com.zanoapps.onboarding.domain.enums.buyer.LifeSituation
import com.zanoapps.onboarding.domain.enums.buyer.PropertyIntent

class UserPreferenceAnalyzer {
    fun analyzePreferences(
        clientIntent: ClientIntent?,
        lifeSituations: List<LifeSituation>,
        propertyIntents: List<PropertyIntent>,
        selectedAmenities: List<Amenity>
    ): PropertyRecommendationProfile {

        val suggestedAmenities = generateAmenitySuggestions(lifeSituations, propertyIntents)
        val propertyTypes = inferPropertyTypes(lifeSituations, propertyIntents)
        val budgetRange = inferBudgetRange(lifeSituations, propertyIntents)
        val locationPreferences = inferLocationPreferences(lifeSituations, selectedAmenities)
        val priorityFeatures = determinePriorityFeatures(lifeSituations, selectedAmenities)
        val searchTags = generateSearchTags(lifeSituations, propertyIntents, selectedAmenities)

        return PropertyRecommendationProfile(
            clientIntent = clientIntent,
            lifeSituations = lifeSituations,
            propertyIntents = propertyIntents,
            selectedAmenities = selectedAmenities,
            suggestedAmenities = suggestedAmenities,
            recommendedPropertyTypes = propertyTypes,
            estimatedBudgetRange = budgetRange,
            locationPreferences = locationPreferences,
            priorityFeatures = priorityFeatures,
            searchTags = searchTags,
            confidenceScore = calculateConfidenceScore(
                lifeSituations, propertyIntents, selectedAmenities
            )
        )
    }



    private fun generateAmenitySuggestions(
        lifeSituations: List<LifeSituation>, propertyIntents: List<PropertyIntent>
    ): List<Amenity> {
        val suggestions = mutableSetOf<Amenity>()

        lifeSituations.forEach { situation ->
            when (situation) {
                LifeSituation.SINGLE -> {
                    suggestions.addAll(
                        listOf(
                            Amenity.GYM_FITNESS,
                            Amenity.COWORKING_SPACE,
                            Amenity.RESTAURANTS_CAFES,
                            Amenity.PUBLIC_TRANSPORT
                        )
                    )
                }

                LifeSituation.COUPLE -> {
                    suggestions.addAll(
                        listOf(
                            Amenity.PARKING,
                            Amenity.RESTAURANTS_CAFES,
                            Amenity.PARKS_RECREATION,
                        )
                    )
                }

                LifeSituation.ESTABLISHED_FAMILY -> {
                    suggestions.addAll(
                        listOf(
                            Amenity.GOOD_SCHOOLS, Amenity.PARKS_RECREATION, Amenity.PARKING
                        )
                    )
                }

                LifeSituation.GROWING_FAMILY -> {
                    suggestions.addAll(
                        listOf(
                            Amenity.GOOD_SCHOOLS,
                            Amenity.PARKS_RECREATION,
                            Amenity.HEALTHCARE,
                            Amenity.SHOPPING_CENTERS
                        )
                    )
                }

                LifeSituation.RETIREE -> {
                    suggestions.addAll(
                        listOf(
                            Amenity.HEALTHCARE,
                            Amenity.PARKS_RECREATION,
                            Amenity.QUIET_NEIGHBORHOOD,
                            Amenity.PUBLIC_TRANSPORT,
                        )
                    )
                }

                LifeSituation.STUDENT -> {
                    suggestions.addAll(
                        listOf(
                            Amenity.PUBLIC_TRANSPORT,
                            Amenity.RESTAURANTS_CAFES,
                            Amenity.COWORKING_SPACE,
                            Amenity.EDUCATION
                        )
                    )
                }

                LifeSituation.YOUNG_PROFESSIONAL -> {
                    suggestions.addAll(
                        listOf(
                            Amenity.PUBLIC_TRANSPORT,
                            Amenity.GYM_FITNESS,
                            Amenity.COWORKING_SPACE,
                            Amenity.RESTAURANTS_CAFES,
                            Amenity.NIGHTLIFE
                        )
                    )
                }

                LifeSituation.REMOTE_WORKER -> {
                    suggestions.addAll(
                        listOf(
                            Amenity.COWORKING_SPACE,
                            Amenity.INTERNET,
                            Amenity.QUIET_NEIGHBORHOOD,
                            Amenity.RESTAURANTS_CAFES
                        )
                    )
                }

                LifeSituation.INVESTOR -> {
                    suggestions.addAll(
                        listOf(
                            Amenity.PUBLIC_TRANSPORT,
                            Amenity.SHOPPING_CENTERS,
                            Amenity.PRIME_BUSINESS_LOCATION,
                            Amenity.RENTAL_POTENTIAL
                        )
                    )
                }

                LifeSituation.EMPTY_NESTERS -> {
                    suggestions.addAll(
                        listOf(
                            Amenity.QUIET_NEIGHBORHOOD,
                            Amenity.HEALTHCARE,
                        )
                    )
                }
            }
        }

        propertyIntents.forEach { intent ->
            when (intent) {
                PropertyIntent.BUY -> {
                    suggestions.addAll(
                        listOf(
                            Amenity.PARKING,
                            Amenity.SECURITY,
                        )
                    )
                }

                PropertyIntent.BUY_TO_INVEST -> {
                    suggestions.addAll(
                        listOf(
                            Amenity.PUBLIC_TRANSPORT, Amenity.PRIME_BUSINESS_LOCATION
                        )
                    )
                }

                PropertyIntent.RENT -> {
                    suggestions.addAll(
                        listOf(
                            Amenity.FURNISHED, Amenity.PUBLIC_TRANSPORT
                        )
                    )
                }

                PropertyIntent.OPEN_TO_BOTH -> {
                    suggestions.addAll(
                        listOf(
                            Amenity.RENTAL_POTENTIAL,
                            Amenity.PRIME_BUSINESS_LOCATION,

                            )
                    )
                }
            }
        }

        return suggestions.toList()
    }

    private fun inferPropertyTypes(
        lifeSituations: List<LifeSituation>, propertyIntents: List<PropertyIntent>
    ): List<String> {
        val types = mutableSetOf<String>()

        lifeSituations.forEach { situation ->
            when (situation) {
                LifeSituation.SINGLE, LifeSituation.STUDENT, LifeSituation.YOUNG_PROFESSIONAL -> {
                    types.addAll(listOf("Studio", "1 Bedroom Apartment"))
                }

                LifeSituation.COUPLE, LifeSituation.REMOTE_WORKER -> {
                    types.addAll(listOf("1 Bedroom Apartment", "2 Bedroom Apartment"))
                }

                LifeSituation.ESTABLISHED_FAMILY, LifeSituation.GROWING_FAMILY -> {
                    types.addAll(listOf("3 Bedroom Apartment", "House", "Townhouse"))
                }


                LifeSituation.INVESTOR -> {
                    types.addAll(listOf("Apartment", "Commercial Property", "Land"))
                }

                LifeSituation.EMPTY_NESTERS -> {
                    types.addAll(listOf("Apartment", "Commercial Property", "Land"))

                }

                LifeSituation.RETIREE -> {
                    types.addAll(listOf("2 Bedroom Apartment", "Bungalow", "Ground Floor Unit"))
                }
            }
        }

        return types.toList()
    }

    private fun inferBudgetRange(
        lifeSituations: List<LifeSituation>, propertyIntents: List<PropertyIntent>
    ): BudgetRange {
        val hasStudent = lifeSituations.contains(LifeSituation.STUDENT)
        val hasFamily = lifeSituations.any {
            it == LifeSituation.ESTABLISHED_FAMILY || it == LifeSituation.GROWING_FAMILY
        }
        val hasInvestor = lifeSituations.contains(LifeSituation.INVESTOR)
        val isBuying = propertyIntents.any {
            it == PropertyIntent.BUY || it == PropertyIntent.BUY_TO_INVEST
        }

        return when {
            hasStudent -> BudgetRange.LOW
            hasFamily && isBuying -> BudgetRange.HIGH
            hasInvestor -> BudgetRange.VERY_HIGH
            else -> BudgetRange.MEDIUM
        }
    }

    private fun inferLocationPreferences(
        lifeSituations: List<LifeSituation>, selectedAmenities: List<Amenity>
    ): LocationPreferences {
        val needsUrban = lifeSituations.any {
            it == LifeSituation.STUDENT || it == LifeSituation.YOUNG_PROFESSIONAL
        }
        val needsSuburban = lifeSituations.any {
            it == LifeSituation.ESTABLISHED_FAMILY || it == LifeSituation.GROWING_FAMILY
        }
        val needsQuiet =
            lifeSituations.contains(LifeSituation.RETIREE) || selectedAmenities.contains(Amenity.QUIET_NEIGHBORHOOD)

        return LocationPreferences(
            preferredLocationType = when {
                needsQuiet && needsSuburban -> LocationType.SUBURBAN
                needsUrban -> LocationType.URBAN
                needsSuburban -> LocationType.SUBURBAN
                else -> LocationType.MIXED
            },
            proximityToTransit = selectedAmenities.contains(Amenity.PUBLIC_TRANSPORT),
            proximityToSchools = selectedAmenities.contains(Amenity.GOOD_SCHOOLS),
            proximityToHealthcare = selectedAmenities.contains(Amenity.HEALTHCARE),
            walkabilityScore = if (needsUrban) WalkabilityScore.HIGH else WalkabilityScore.MEDIUM
        )
    }

    private fun determinePriorityFeatures(
        lifeSituations: List<LifeSituation>, selectedAmenities: List<Amenity>
    ): List<PriorityFeature> {
        val features = mutableListOf<PriorityFeature>()

        // Based on life situation
        if (lifeSituations.any { it == LifeSituation.ESTABLISHED_FAMILY || it == LifeSituation.GROWING_FAMILY }) {
            features.add(PriorityFeature("Child Safety Features", Priority.CRITICAL))
            features.add(PriorityFeature("Nearby Schools", Priority.HIGH))
            features.add(PriorityFeature("Play Areas", Priority.HIGH))
        }

        if (lifeSituations.contains(LifeSituation.REMOTE_WORKER)) {
            features.add(PriorityFeature("Home Office Space", Priority.HIGH))
            features.add(PriorityFeature("High-Speed Internet", Priority.CRITICAL))
        }

        if (lifeSituations.contains(LifeSituation.RETIREE)) {
            features.add(PriorityFeature("Accessibility Features", Priority.HIGH))
            features.add(PriorityFeature("Ground Floor or Elevator", Priority.HIGH))
        }

        // Based on amenities
        selectedAmenities.forEach { amenity ->
            when (amenity) {
                Amenity.PARKING -> features.add(PriorityFeature("Parking Space", Priority.HIGH))
                Amenity.PET_POLICY -> features.add(
                    PriorityFeature(
                        "Pet-Friendly", Priority.MEDIUM
                    )
                )

                else -> {}
            }
        }

        return features
    }

    private fun generateSearchTags(
        lifeSituations: List<LifeSituation>,
        propertyIntents: List<PropertyIntent>,
        selectedAmenities: List<Amenity>
    ): List<String> {
        val tags = mutableSetOf<String>()

        lifeSituations.forEach { tags.add(it.name.lowercase().replace('_', '-')) }
        propertyIntents.forEach { tags.add(it.name.lowercase().replace('_', '-')) }
        selectedAmenities.forEach { tags.add(it.name.lowercase().replace('_', '-')) }

        // Add derived tags
        if (lifeSituations.any { it == LifeSituation.ESTABLISHED_FAMILY || it == LifeSituation.GROWING_FAMILY }) {
            tags.addAll(listOf("family-friendly", "child-safe", "spacious"))
        }

        if (lifeSituations.contains(LifeSituation.YOUNG_PROFESSIONAL)) {
            tags.addAll(listOf("modern", "trendy", "convenient"))
        }

        return tags.toList()
    }

    private fun calculateConfidenceScore(
        lifeSituations: List<LifeSituation>,
        propertyIntents: List<PropertyIntent>,
        selectedAmenities: List<Amenity>
    ): Float {
        var score = 0f

        if (lifeSituations.isNotEmpty()) score += 0.4f
        if (propertyIntents.isNotEmpty()) score += 0.3f
        if (selectedAmenities.isNotEmpty()) score += 0.3f

        return score
    }
}

