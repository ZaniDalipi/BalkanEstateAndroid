package com.zanoapps.search.domain.repository

import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

data class SavedSearchItem(
    val id: String,
    val name: String,
    val query: String = "",
    val location: String = "",
    val propertyType: String = "",
    val priceRange: String = "",
    val bedrooms: String = "",
    val notificationsEnabled: Boolean = true,
    val matchCount: Int = 0,
    val newCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

interface SavedSearchRepository {
    fun getSavedSearches(): Flow<List<SavedSearchItem>>
    suspend fun saveSearch(search: SavedSearchItem): EmptyResult<DataError.Local>
    suspend fun deleteSearch(id: String): EmptyResult<DataError.Local>
    suspend fun toggleNotifications(id: String, enabled: Boolean): EmptyResult<DataError.Local>
}
