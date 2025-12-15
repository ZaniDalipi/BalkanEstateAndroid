package com.zanoapps.search.domain.model

data class SavedSearch(
    val id: Long = 0,
    val name: String,
    val filters: SearchFilters,
    val notificationsEnabled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
