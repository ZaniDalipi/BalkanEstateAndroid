package com.zanoapps.search.domain.repository

import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for property search operations.
 * Uses the Single Source of Truth pattern - Room database is the source,
 * and API is used to sync data.
 */
interface SearchRepository {
    /**
     * Get all properties from local database (single source of truth).
     */
    fun getProperties(): Flow<List<BalkanEstateProperty>>

    /**
     * Search properties with filters from local database.
     */
    fun searchProperties(
        listingType: String? = null,
        propertyType: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        minBedrooms: Int? = null,
        city: String? = null
    ): Flow<List<BalkanEstateProperty>>

    /**
     * Get featured properties from local database.
     */
    fun getFeaturedProperties(limit: Int = 10): Flow<List<BalkanEstateProperty>>

    /**
     * Refresh properties from remote API and cache to local database.
     * This is a suspend function that fetches from MongoDB API and saves to Room.
     */
    suspend fun refreshProperties(
        page: Int = 1,
        limit: Int = 20,
        listingType: String? = null,
        propertyType: String? = null,
        city: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        minBedrooms: Int? = null
    ): EmptyResult<DataError.Network>

    /**
     * Check if local data exists.
     */
    suspend fun hasLocalData(): Boolean
}
