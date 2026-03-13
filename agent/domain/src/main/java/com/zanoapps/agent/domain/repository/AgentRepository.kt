package com.zanoapps.agent.domain.repository

import com.zanoapps.agent.domain.model.Agent
import com.zanoapps.agent.domain.model.Agency
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface AgentRepository {
    fun getAgents(): Flow<List<Agent>>
    fun getAgencies(): Flow<List<Agency>>
    suspend fun getAgentById(id: String): Result<Agent, DataError.Network>
    suspend fun getAgencyById(id: String): Result<Agency, DataError.Network>
    suspend fun searchAgents(query: String): Result<List<Agent>, DataError.Network>
    suspend fun searchAgencies(query: String): Result<List<Agency>, DataError.Network>
}
