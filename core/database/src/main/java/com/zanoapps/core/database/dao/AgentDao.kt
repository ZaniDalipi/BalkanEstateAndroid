package com.zanoapps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zanoapps.core.database.entity.AgentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AgentDao {

    @Query("SELECT * FROM agents ORDER BY rating DESC, listingsCount DESC")
    fun getAllAgents(): Flow<List<AgentEntity>>

    @Query("SELECT * FROM agents WHERE id = :agentId")
    fun getAgentById(agentId: String): Flow<AgentEntity?>

    @Query("SELECT * FROM agents WHERE id = :agentId")
    suspend fun getAgentByIdOnce(agentId: String): AgentEntity?

    @Query("SELECT * FROM agents WHERE isVerified = 1 ORDER BY rating DESC LIMIT :limit")
    fun getVerifiedAgents(limit: Int = 10): Flow<List<AgentEntity>>

    @Query("SELECT * FROM agents WHERE rating >= :minRating ORDER BY rating DESC")
    fun getTopRatedAgents(minRating: Float = 4.0f): Flow<List<AgentEntity>>

    @Query("SELECT * FROM agents WHERE name LIKE '%' || :query || '%' OR agency LIKE '%' || :query || '%' ORDER BY rating DESC")
    fun searchAgents(query: String): Flow<List<AgentEntity>>

    @Query("SELECT * FROM agents WHERE specializations LIKE '%' || :specialization || '%'")
    fun getAgentsBySpecialization(specialization: String): Flow<List<AgentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgent(agent: AgentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgents(agents: List<AgentEntity>)

    @Update
    suspend fun updateAgent(agent: AgentEntity)

    @Query("DELETE FROM agents WHERE id = :agentId")
    suspend fun deleteAgent(agentId: String)

    @Query("DELETE FROM agents")
    suspend fun deleteAllAgents()

    @Query("SELECT COUNT(*) FROM agents")
    suspend fun getAgentCount(): Int
}
