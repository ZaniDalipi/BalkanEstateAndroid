package com.zanoapps.agent.domain.repository

import com.zanoapps.agent.domain.model.Agent
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface AgentRepository {
    fun getAgents(): Flow<List<Agent>>
    fun getAgentById(agentId: String): Flow<Agent?>
    fun getAgentListings(agentId: String): Flow<List<BalkanEstateProperty>>

    suspend fun refreshAgents(): EmptyResult<DataError.Network>
    suspend fun refreshAgent(agentId: String): EmptyResult<DataError.Network>
    suspend fun refreshAgentListings(agentId: String): EmptyResult<DataError.Network>

    suspend fun contactAgent(
        agentId: String,
        name: String,
        email: String,
        phone: String?,
        message: String,
        propertyId: String?
    ): EmptyResult<DataError.Network>
}
