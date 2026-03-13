package com.zanoapps.search.domain.repository

import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.Result
import com.zanoapps.search.domain.model.SearchFilters
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    fun getProperties(): Flow<List<BalkanEstateProperty>>
    suspend fun searchProperties(query: String, filters: SearchFilters): Result<List<BalkanEstateProperty>, DataError.Network>
    suspend fun getPropertyById(id: String): Result<BalkanEstateProperty, DataError.Network>
    suspend fun getFeaturedProperties(): Result<List<BalkanEstateProperty>, DataError.Network>
}
