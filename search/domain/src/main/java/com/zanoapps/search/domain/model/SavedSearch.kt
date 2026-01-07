package com.zanoapps.search.domain.model

data class SavedSearch(
    val id: Long = 0,
    val userId: String,
    val name: String,
    val country: String? = null,
    val city: String? = null,
    val listingType: String? = null,
    val propertyTypes: List<String> = emptyList(),
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val minArea: Double? = null,
    val maxArea: Double? = null,
    val bedrooms: Int? = null,
    val bathrooms: Int? = null,
    val amenities: List<String> = emptyList(),
    val notificationsEnabled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastUsedAt: Long = System.currentTimeMillis()
)
