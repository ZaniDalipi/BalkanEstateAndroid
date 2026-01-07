package com.zanoapps.search.domain.repository

import com.zanoapps.search.domain.model.SearchHistoryItem
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun getRecentSearches(userId: String, limit: Int = 20): Flow<List<SearchHistoryItem>>
    fun getRecentSearchesByType(userId: String, searchType: String, limit: Int = 20): Flow<List<SearchHistoryItem>>

    suspend fun getSearchSuggestions(userId: String, prefix: String, limit: Int = 10): List<String>
    suspend fun addSearchHistory(searchHistory: SearchHistoryItem): Long
    suspend fun deleteSearchHistory(historyId: Long)
    suspend fun clearSearchHistory(userId: String)
    suspend fun deleteOldSearchHistory(userId: String, olderThanDays: Int = 30)
}
