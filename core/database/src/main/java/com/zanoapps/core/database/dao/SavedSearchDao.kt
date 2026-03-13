package com.zanoapps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zanoapps.core.database.entity.SavedSearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedSearchDao {
    @Query("SELECT * FROM saved_searches ORDER BY createdAt DESC")
    fun getAll(): Flow<List<SavedSearchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SavedSearchEntity)

    @Query("DELETE FROM saved_searches WHERE id = :id")
    suspend fun delete(id: String)

    @Query("UPDATE saved_searches SET notificationsEnabled = :enabled WHERE id = :id")
    suspend fun toggleNotifications(id: String, enabled: Boolean)
}
