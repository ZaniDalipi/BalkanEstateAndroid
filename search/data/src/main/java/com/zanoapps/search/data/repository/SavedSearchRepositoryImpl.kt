package com.zanoapps.search.data.repository

import com.zanoapps.core.database.dao.SavedSearchDao
import com.zanoapps.search.data.mappers.toDomain
import com.zanoapps.search.data.mappers.toDomainList
import com.zanoapps.search.data.mappers.toEntity
import com.zanoapps.search.domain.model.SavedSearch
import com.zanoapps.search.domain.repository.SavedSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Single Source of Truth Implementation for Saved Searches.
 *
 * Room database is the single source of truth.
 */
class SavedSearchRepositoryImpl(
    private val savedSearchDao: SavedSearchDao
) : SavedSearchRepository {

    override fun getSavedSearches(userId: String): Flow<List<SavedSearch>> {
        return savedSearchDao.getSavedSearches(userId).map { it.toDomainList() }
    }

    override fun getSavedSearchesWithNotifications(userId: String): Flow<List<SavedSearch>> {
        return savedSearchDao.getSavedSearchesWithNotifications(userId).map { it.toDomainList() }
    }

    override fun getSavedSearchCount(userId: String): Flow<Int> {
        return savedSearchDao.getSavedSearchCount(userId)
    }

    override suspend fun getSavedSearchById(searchId: Long): SavedSearch? {
        return savedSearchDao.getSavedSearchById(searchId)?.toDomain()
    }

    override suspend fun saveSavedSearch(savedSearch: SavedSearch): Long {
        return savedSearchDao.insertSavedSearch(savedSearch.toEntity())
    }

    override suspend fun updateSavedSearch(savedSearch: SavedSearch) {
        savedSearchDao.updateSavedSearch(savedSearch.toEntity())
    }

    override suspend fun updateLastUsedAt(searchId: Long) {
        savedSearchDao.updateLastUsedAt(searchId)
    }

    override suspend fun updateNotificationsEnabled(searchId: Long, enabled: Boolean) {
        savedSearchDao.updateNotificationsEnabled(searchId, enabled)
    }

    override suspend fun deleteSavedSearch(searchId: Long) {
        savedSearchDao.deleteSavedSearch(searchId)
    }

    override suspend fun deleteAllSavedSearches(userId: String) {
        savedSearchDao.deleteAllSavedSearches(userId)
    }
}
