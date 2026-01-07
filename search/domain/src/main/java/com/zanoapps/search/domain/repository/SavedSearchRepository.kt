package com.zanoapps.search.domain.repository

import com.zanoapps.search.domain.model.SavedSearch
import kotlinx.coroutines.flow.Flow

interface SavedSearchRepository {
    fun getSavedSearches(userId: String): Flow<List<SavedSearch>>
    fun getSavedSearchesWithNotifications(userId: String): Flow<List<SavedSearch>>
    fun getSavedSearchCount(userId: String): Flow<Int>

    suspend fun getSavedSearchById(searchId: Long): SavedSearch?
    suspend fun saveSavedSearch(savedSearch: SavedSearch): Long
    suspend fun updateSavedSearch(savedSearch: SavedSearch)
    suspend fun updateLastUsedAt(searchId: Long)
    suspend fun updateNotificationsEnabled(searchId: Long, enabled: Boolean)
    suspend fun deleteSavedSearch(searchId: Long)
    suspend fun deleteAllSavedSearches(userId: String)
}
