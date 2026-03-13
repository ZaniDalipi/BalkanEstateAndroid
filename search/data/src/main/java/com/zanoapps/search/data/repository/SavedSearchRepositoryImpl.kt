package com.zanoapps.search.data.repository

import com.zanoapps.core.database.dao.SavedSearchDao
import com.zanoapps.core.database.entity.SavedSearchEntity
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.search.domain.repository.SavedSearchItem
import com.zanoapps.search.domain.repository.SavedSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SavedSearchRepositoryImpl(
    private val savedSearchDao: SavedSearchDao
) : SavedSearchRepository {

    override fun getSavedSearches(): Flow<List<SavedSearchItem>> {
        return savedSearchDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveSearch(search: SavedSearchItem): EmptyResult<DataError.Local> {
        return try {
            savedSearchDao.insert(search.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteSearch(id: String): EmptyResult<DataError.Local> {
        return try {
            savedSearchDao.delete(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun toggleNotifications(id: String, enabled: Boolean): EmptyResult<DataError.Local> {
        return try {
            savedSearchDao.toggleNotifications(id, enabled)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }
}

private fun SavedSearchEntity.toDomain(): SavedSearchItem {
    return SavedSearchItem(
        id = id, name = name, query = query, location = location,
        propertyType = propertyType, priceRange = priceRange, bedrooms = bedrooms,
        notificationsEnabled = notificationsEnabled, matchCount = matchCount,
        newCount = newCount, createdAt = createdAt
    )
}

private fun SavedSearchItem.toEntity(): SavedSearchEntity {
    return SavedSearchEntity(
        id = id, name = name, query = query, location = location,
        propertyType = propertyType, priceRange = priceRange, bedrooms = bedrooms,
        notificationsEnabled = notificationsEnabled, matchCount = matchCount,
        newCount = newCount, createdAt = createdAt
    )
}
