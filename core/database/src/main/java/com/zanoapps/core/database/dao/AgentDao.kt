package com.zanoapps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zanoapps.core.database.entity.AgentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AgentDao {
    @Query("SELECT * FROM agents ORDER BY rating DESC")
    fun getAllAgents(): Flow<List<AgentEntity>>

    @Query("SELECT * FROM agents WHERE id = :id")
    suspend fun getById(id: String): AgentEntity?

    @Query("SELECT * FROM agents WHERE name LIKE '%' || :query || '%' OR specialization LIKE '%' || :query || '%'")
    suspend fun search(query: String): List<AgentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(agents: List<AgentEntity>)
}
