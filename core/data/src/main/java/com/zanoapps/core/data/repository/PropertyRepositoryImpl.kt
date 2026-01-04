package com.zanoapps.core.data.repository

import com.zanoapps.core.data.mappers.toDomain
import com.zanoapps.core.data.mappers.toDomainList
import com.zanoapps.core.data.mappers.toEntities
import com.zanoapps.core.data.mappers.toEntity
import com.zanoapps.core.data.remote.PropertyApiService
import com.zanoapps.core.database.dao.PropertyDao
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Single Source of Truth Implementation for Properties.
 *
 * Room database is the single source of truth.
 * Data is fetched from MongoDB API and cached to Room.
 * UI observes Room database through Flow.
 */
class PropertyRepositoryImpl(
    private val propertyDao: PropertyDao,
    private val propertyApiService: PropertyApiService
) {
    /**
     * Get all properties from Room (single source of truth).
     * Call refreshProperties() to sync with remote.
     */
    fun getProperties(): Flow<List<BalkanEstateProperty>> {
        return propertyDao.getAllProperties().map { it.toDomainList() }
    }

    /**
     * Get a single property by ID from Room.
     */
    fun getPropertyById(propertyId: String): Flow<BalkanEstateProperty?> {
        return propertyDao.getPropertyById(propertyId).map { it?.toDomain() }
    }

    /**
     * Search properties from Room.
     */
    fun searchProperties(
        listingType: String? = null,
        propertyType: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        minBedrooms: Int? = null,
        city: String? = null
    ): Flow<List<BalkanEstateProperty>> {
        return propertyDao.searchProperties(
            listingType = listingType,
            propertyType = propertyType,
            minPrice = minPrice,
            maxPrice = maxPrice,
            minBedrooms = minBedrooms,
            city = city
        ).map { it.toDomainList() }
    }

    /**
     * Get featured properties from Room.
     */
    fun getFeaturedProperties(limit: Int = 10): Flow<List<BalkanEstateProperty>> {
        return propertyDao.getFeaturedProperties(limit).map { it.toDomainList() }
    }

    /**
     * Refresh properties from remote API and cache to Room.
     * This is the sync operation that fetches from MongoDB.
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
    ): EmptyResult<DataError.Network> {
        return when (val result = propertyApiService.getProperties(
            page = page,
            limit = limit,
            listingType = listingType,
            propertyType = propertyType,
            city = city,
            minPrice = minPrice,
            maxPrice = maxPrice,
            minBedrooms = minBedrooms
        )) {
            is Result.Success -> {
                // Cache to Room - this triggers Flow updates automatically
                propertyDao.insertProperties(result.data.data.toEntities())
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }

    /**
     * Refresh a single property from remote API.
     */
    suspend fun refreshProperty(propertyId: String): EmptyResult<DataError.Network> {
        return when (val result = propertyApiService.getPropertyById(propertyId)) {
            is Result.Success -> {
                propertyDao.insertProperty(result.data.data.toEntity())
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }

    /**
     * Refresh featured properties from remote API.
     */
    suspend fun refreshFeaturedProperties(limit: Int = 10): EmptyResult<DataError.Network> {
        return when (val result = propertyApiService.getFeaturedProperties(limit)) {
            is Result.Success -> {
                propertyDao.insertProperties(result.data.data.toEntities())
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }

    /**
     * Check if we have local data.
     */
    suspend fun hasLocalData(): Boolean {
        return propertyDao.getPropertyCount() > 0
    }

    /**
     * Clear all local property data.
     */
    suspend fun clearLocalData() {
        propertyDao.deleteAllProperties()
    }
}
