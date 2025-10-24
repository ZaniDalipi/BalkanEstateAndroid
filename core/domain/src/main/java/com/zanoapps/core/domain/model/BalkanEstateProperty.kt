package com.zanoapps.core.domain.model

data class BalkanEstateProperty(
    val id: String,
    val title: String,
    val price: Double,
    val currency: String,
    val imageUrl: String,
    val bedrooms: Int,
    val bathrooms: Int,
    val squareFootage: Int,
    val address: String,
    val city: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val propertyType: String,
    val listingType: String,
    val agentName: String,
    val isFeatured: Boolean = false,
    val isUrgent: Boolean = false
)