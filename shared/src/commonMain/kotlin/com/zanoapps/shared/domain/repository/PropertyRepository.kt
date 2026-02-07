package com.zanoapps.shared.domain.repository

import com.zanoapps.shared.domain.model.Property
import com.zanoapps.shared.domain.model.PropertyFilter
import com.zanoapps.shared.domain.model.SortOption
import com.zanoapps.shared.util.DataError
import com.zanoapps.shared.util.Result
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    /**
     * Get all properties with optional filtering and sorting
     */
    suspend fun getProperties(
        filter: PropertyFilter = PropertyFilter(),
        sortOption: SortOption = SortOption.NEWEST,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<Property>, DataError.Network>

    /**
     * Get a single property by ID
     */
    suspend fun getPropertyById(id: String): Result<Property, DataError.Network>

    /**
     * Get featured properties
     */
    suspend fun getFeaturedProperties(limit: Int = 10): Result<List<Property>, DataError.Network>

    /**
     * Get properties by agent
     */
    suspend fun getPropertiesByAgent(agentId: String): Result<List<Property>, DataError.Network>

    /**
     * Get similar properties
     */
    suspend fun getSimilarProperties(propertyId: String, limit: Int = 5): Result<List<Property>, DataError.Network>

    /**
     * Search properties by query
     */
    suspend fun searchProperties(query: String): Result<List<Property>, DataError.Network>

    /**
     * Get saved/favorite properties
     */
    fun getSavedProperties(): Flow<List<Property>>

    /**
     * Save a property to favorites
     */
    suspend fun saveProperty(propertyId: String): Result<Unit, DataError.Local>

    /**
     * Remove a property from favorites
     */
    suspend fun unsaveProperty(propertyId: String): Result<Unit, DataError.Local>

    /**
     * Check if a property is saved
     */
    suspend fun isPropertySaved(propertyId: String): Boolean
}
