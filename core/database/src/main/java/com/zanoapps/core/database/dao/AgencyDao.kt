package com.zanoapps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zanoapps.core.database.entity.AgencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AgencyDao {
    @Query("SELECT * FROM agencies ORDER BY rating DESC")
    fun getAllAgencies(): Flow<List<AgencyEntity>>

    @Query("SELECT * FROM agencies WHERE id = :id")
    suspend fun getById(id: String): AgencyEntity?

    @Query("SELECT * FROM agencies WHERE name LIKE '%' || :query || '%' OR city LIKE '%' || :query || '%'")
    suspend fun search(query: String): List<AgencyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(agencies: List<AgencyEntity>)
}
