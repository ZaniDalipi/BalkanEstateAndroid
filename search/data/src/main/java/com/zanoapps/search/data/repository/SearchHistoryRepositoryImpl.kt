package com.zanoapps.search.data.repository

import com.zanoapps.core.database.dao.SearchHistoryDao
import com.zanoapps.search.data.mappers.toDomainList
import com.zanoapps.search.data.mappers.toEntity
import com.zanoapps.search.domain.model.SearchHistoryItem
import com.zanoapps.search.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Single Source of Truth Implementation for Search History.
 *
 * Room database is the single source of truth.
 */
class SearchHistoryRepositoryImpl(
    private val searchHistoryDao: SearchHistoryDao
) : SearchHistoryRepository {

    override fun getRecentSearches(userId: String, limit: Int): Flow<List<SearchHistoryItem>> {
        return searchHistoryDao.getRecentSearches(userId, limit).map { it.toDomainList() }
    }

    override fun getRecentSearchesByType(userId: String, searchType: String, limit: Int): Flow<List<SearchHistoryItem>> {
        return searchHistoryDao.getRecentSearchesByType(userId, searchType, limit).map { it.toDomainList() }
    }

    override suspend fun getSearchSuggestions(userId: String, prefix: String, limit: Int): List<String> {
        return searchHistoryDao.getSearchSuggestions(userId, prefix, limit)
    }

    override suspend fun addSearchHistory(searchHistory: SearchHistoryItem): Long {
        return searchHistoryDao.insertSearchHistory(searchHistory.toEntity())
    }

    override suspend fun deleteSearchHistory(historyId: Long) {
        searchHistoryDao.deleteSearchHistory(historyId)
    }

    override suspend fun clearSearchHistory(userId: String) {
        searchHistoryDao.clearSearchHistory(userId)
    }

    override suspend fun deleteOldSearchHistory(userId: String, olderThanDays: Int) {
        val threshold = System.currentTimeMillis() - (olderThanDays * 24 * 60 * 60 * 1000L)
        searchHistoryDao.deleteOldSearchHistory(userId, threshold)
    }
}
