package com.zanoapps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zanoapps.core.database.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM search_history WHERE userId = :userId ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentSearches(userId: String, limit: Int = 20): Flow<List<SearchHistoryEntity>>

    @Query("SELECT * FROM search_history WHERE userId = :userId AND searchType = :searchType ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentSearchesByType(userId: String, searchType: String, limit: Int = 20): Flow<List<SearchHistoryEntity>>

    @Query("SELECT DISTINCT query FROM search_history WHERE userId = :userId AND query LIKE '%' || :prefix || '%' ORDER BY createdAt DESC LIMIT :limit")
    suspend fun getSearchSuggestions(userId: String, prefix: String, limit: Int = 10): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(searchHistory: SearchHistoryEntity): Long

    @Query("DELETE FROM search_history WHERE id = :historyId")
    suspend fun deleteSearchHistory(historyId: Long)

    @Query("DELETE FROM search_history WHERE userId = :userId")
    suspend fun clearSearchHistory(userId: String)

    @Query("DELETE FROM search_history WHERE userId = :userId AND createdAt < :timestamp")
    suspend fun deleteOldSearchHistory(userId: String, timestamp: Long)

    @Query("SELECT COUNT(*) FROM search_history WHERE userId = :userId")
    suspend fun getSearchHistoryCount(userId: String): Int
}
