package com.zanoapps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zanoapps.core.database.entity.SavedSearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedSearchDao {

    @Query("SELECT * FROM saved_searches WHERE userId = :userId ORDER BY lastUsedAt DESC")
    fun getSavedSearches(userId: String): Flow<List<SavedSearchEntity>>

    @Query("SELECT * FROM saved_searches WHERE id = :searchId")
    suspend fun getSavedSearchById(searchId: Long): SavedSearchEntity?

    @Query("SELECT * FROM saved_searches WHERE userId = :userId AND notificationsEnabled = 1")
    fun getSavedSearchesWithNotifications(userId: String): Flow<List<SavedSearchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedSearch(savedSearch: SavedSearchEntity): Long

    @Update
    suspend fun updateSavedSearch(savedSearch: SavedSearchEntity)

    @Query("UPDATE saved_searches SET lastUsedAt = :timestamp WHERE id = :searchId")
    suspend fun updateLastUsedAt(searchId: Long, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE saved_searches SET notificationsEnabled = :enabled WHERE id = :searchId")
    suspend fun updateNotificationsEnabled(searchId: Long, enabled: Boolean)

    @Query("DELETE FROM saved_searches WHERE id = :searchId")
    suspend fun deleteSavedSearch(searchId: Long)

    @Query("DELETE FROM saved_searches WHERE userId = :userId")
    suspend fun deleteAllSavedSearches(userId: String)

    @Query("SELECT COUNT(*) FROM saved_searches WHERE userId = :userId")
    fun getSavedSearchCount(userId: String): Flow<Int>
}
