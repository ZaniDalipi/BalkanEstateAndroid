package com.zanoapps.search.data.repository

import com.zanoapps.core.data.repository.PropertyRepositoryImpl
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of SearchRepository that delegates to PropertyRepositoryImpl.
 * This wrapper allows the search module to have its own abstraction while
 * using the SSOT PropertyRepository under the hood.
 */
class SearchRepositoryImpl(
    private val propertyRepository: PropertyRepositoryImpl
) : SearchRepository {

    override fun getProperties(): Flow<List<BalkanEstateProperty>> {
        return propertyRepository.getProperties()
    }

    override fun searchProperties(
        listingType: String?,
        propertyType: String?,
        minPrice: Double?,
        maxPrice: Double?,
        minBedrooms: Int?,
        city: String?
    ): Flow<List<BalkanEstateProperty>> {
        return propertyRepository.searchProperties(
            listingType = listingType,
            propertyType = propertyType,
            minPrice = minPrice,
            maxPrice = maxPrice,
            minBedrooms = minBedrooms,
            city = city
        )
    }

    override fun getFeaturedProperties(limit: Int): Flow<List<BalkanEstateProperty>> {
        return propertyRepository.getFeaturedProperties(limit)
    }

    override suspend fun refreshProperties(
        page: Int,
        limit: Int,
        listingType: String?,
        propertyType: String?,
        city: String?,
        minPrice: Double?,
        maxPrice: Double?,
        minBedrooms: Int?
    ): EmptyResult<DataError.Network> {
        return propertyRepository.refreshProperties(
            page = page,
            limit = limit,
            listingType = listingType,
            propertyType = propertyType,
            city = city,
            minPrice = minPrice,
            maxPrice = maxPrice,
            minBedrooms = minBedrooms
        )
    }

    override suspend fun hasLocalData(): Boolean {
        return propertyRepository.hasLocalData()
    }
}
