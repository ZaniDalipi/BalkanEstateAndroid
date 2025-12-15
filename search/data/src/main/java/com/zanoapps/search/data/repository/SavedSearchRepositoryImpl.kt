package com.zanoapps.search.data.repository

import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.Result
import com.zanoapps.search.domain.model.SavedSearch
import com.zanoapps.search.domain.repository.SavedSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class SavedSearchRepositoryImpl : SavedSearchRepository {
    
    // In-memory storage for now - replace with Room database later
    private val _savedSearches = MutableStateFlow<Map<String, List<SavedSearch>>>(emptyMap())
    private var nextId = 1L
    
    override fun getSavedSearches(userId: String): Flow<List<SavedSearch>> {
        return _savedSearches.asStateFlow().map { searchesMap ->
            searchesMap[userId] ?: emptyList()
        }
    }
    
    override suspend fun saveSearch(
        userId: String,
        savedSearch: SavedSearch
    ): Result<SavedSearch, DataError.Local> {
        return try {
            val newSearch = savedSearch.copy(
                id = nextId++,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            val currentMap = _savedSearches.value.toMutableMap()
            val userSearches = currentMap[userId]?.toMutableList() ?: mutableListOf()
            userSearches.add(newSearch)
            currentMap[userId] = userSearches
            _savedSearches.value = currentMap
            
            Result.Success(newSearch)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }
    
    override suspend fun deleteSavedSearch(
        userId: String,
        searchId: Long
    ): Result<Unit, DataError.Network> {
        return try {
            val currentMap = _savedSearches.value.toMutableMap()
            val userSearches = currentMap[userId]?.toMutableList() ?: mutableListOf()
            userSearches.removeAll { it.id == searchId }
            currentMap[userId] = userSearches
            _savedSearches.value = currentMap
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
    
    override suspend fun getSavedSearchById(
        userId: String,
        searchId: Long
    ): Result<SavedSearch?, DataError.Network> {
        return try {
            val search = _savedSearches.value[userId]?.find { it.id == searchId }
            Result.Success(search)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
    
    override suspend fun updateSavedSearch(
        userId: String,
        savedSearch: SavedSearch
    ): Result<SavedSearch, DataError.Network> {
        return try {
            val updatedSearch = savedSearch.copy(
                updatedAt = System.currentTimeMillis()
            )
            
            val currentMap = _savedSearches.value.toMutableMap()
            val userSearches = currentMap[userId]?.toMutableList() ?: mutableListOf()
            val index = userSearches.indexOfFirst { it.id == savedSearch.id }
            
            if (index >= 0) {
                userSearches[index] = updatedSearch
                currentMap[userId] = userSearches
                _savedSearches.value = currentMap
                Result.Success(updatedSearch)
            } else {
                Result.Error(DataError.Network.UNKNOWN)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}
