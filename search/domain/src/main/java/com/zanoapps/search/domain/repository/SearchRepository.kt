package com.zanoapps.search.domain.repository

import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.Result
import com.zanoapps.search.domain.model.BoundingBox
import com.zanoapps.search.domain.model.SearchFilters
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    
    suspend fun searchProperties(
        filters: SearchFilters
    ): Result<List<BalkanEstateProperty>, DataError.Network>
    
    suspend fun searchPropertiesInBounds(
        boundingBox: BoundingBox,
        filters: SearchFilters
    ): Result<List<BalkanEstateProperty>, DataError.Network>
    
    suspend fun getPropertyById(
        propertyId: String
    ): Result<BalkanEstateProperty, DataError.Network>
    
    fun getFavoritePropertyIds(userId: String): Flow<Set<String>>
    
    suspend fun toggleFavorite(
        userId: String,
        propertyId: String
    ): Result<Boolean, DataError.Network>
    
    suspend fun subscribeToNewProperties(
        email: String
    ): Result<Unit, DataError.Network>
}
