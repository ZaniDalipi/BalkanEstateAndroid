package com.zanoapps.favourites.domain.model

data class SavedProperty(
    val propertyId: String,
    val savedAt: Long = System.currentTimeMillis(),
    val notes: String = ""
)
