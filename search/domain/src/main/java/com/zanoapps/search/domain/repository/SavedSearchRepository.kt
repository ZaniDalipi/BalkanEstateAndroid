package com.zanoapps.search.domain.repository

import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.Result
import com.zanoapps.search.domain.model.SavedSearch
import kotlinx.coroutines.flow.Flow


interface SavedSearchRepository {
    
    fun getSavedSearches(userId: String): Flow<List<SavedSearch>>
    
    suspend fun saveSearch(
        userId: String,
        savedSearch: SavedSearch
    ): Result<SavedSearch, DataError.Local>
    
    suspend fun deleteSavedSearch(
        userId: String,
        searchId: Long
    ): Result<Unit, DataError.Network>
    
    suspend fun getSavedSearchById(
        userId: String,
        searchId: Long
    ): Result<SavedSearch?, DataError.Network>
    
    suspend fun updateSavedSearch(
        userId: String,
        savedSearch: SavedSearch
    ): Result<SavedSearch, DataError.Network>
}
