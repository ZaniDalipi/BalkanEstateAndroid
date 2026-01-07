package com.zanoapps.agent.data.repository

import com.zanoapps.agent.data.mappers.toDomain
import com.zanoapps.agent.data.mappers.toDomainList
import com.zanoapps.agent.data.mappers.toEntities
import com.zanoapps.agent.domain.model.Agent
import com.zanoapps.agent.domain.repository.AgentRepository
import com.zanoapps.core.data.mappers.toDomainList
import com.zanoapps.core.database.dao.AgentDao
import com.zanoapps.core.database.dao.PropertyDao
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Single Source of Truth Implementation for Agents.
 *
 * Room database is the single source of truth.
 * Data is cached to Room and UI observes Room database through Flow.
 */
class AgentRepositoryImpl(
    private val agentDao: AgentDao,
    private val propertyDao: PropertyDao
) : AgentRepository {

    init {
        // Seed initial data if empty (for development)
        kotlinx.coroutines.runBlocking {
            if (agentDao.getAgentCount() == 0) {
                agentDao.insertAgents(getInitialAgents().toEntities())
            }
        }
    }

    override fun getAgents(): Flow<List<Agent>> {
        return agentDao.getAllAgents().map { it.toDomainList() }
    }

    override fun getAgentById(agentId: String): Flow<Agent?> {
        return agentDao.getAgentById(agentId).map { it?.toDomain() }
    }

    override fun getAgentListings(agentId: String): Flow<List<BalkanEstateProperty>> {
        return propertyDao.getPropertiesByAgent(agentId).map { it.toDomainList() }
    }

    override suspend fun refreshAgents(): EmptyResult<DataError.Network> {
        // TODO: Fetch from API and cache to Room
        // For now, ensure mock data is present
        return Result.Success(Unit)
    }

    override suspend fun refreshAgent(agentId: String): EmptyResult<DataError.Network> {
        // TODO: Fetch single agent from API
        return Result.Success(Unit)
    }

    override suspend fun refreshAgentListings(agentId: String): EmptyResult<DataError.Network> {
        // TODO: Fetch agent listings from API
        return Result.Success(Unit)
    }

    override suspend fun contactAgent(
        agentId: String,
        name: String,
        email: String,
        phone: String?,
        message: String,
        propertyId: String?
    ): EmptyResult<DataError.Network> {
        // TODO: Send contact request to API
        return Result.Success(Unit)
    }

    private fun getInitialAgents(): List<Agent> = listOf(
        Agent(
            id = "agent1",
            name = "Sarah Johnson",
            email = "sarah.johnson@balkanestate.com",
            phone = "+355 69 123 4567",
            imageUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200",
            bio = "Experienced real estate agent with over 10 years in the Tirana market. Specializing in luxury properties and international clients.",
            agency = "Balkan Estate Premium",
            licenseNumber = "AL-RE-2015-0042",
            specializations = listOf("Luxury", "Residential", "Investment"),
            languages = listOf("English", "Albanian", "Italian"),
            rating = 4.8f,
            reviewCount = 127,
            listingsCount = 24,
            soldCount = 156,
            yearsOfExperience = 10,
            isVerified = true
        ),
        Agent(
            id = "agent2",
            name = "Michael Chen",
            email = "michael.chen@balkanestate.com",
            phone = "+355 69 234 5678",
            imageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
            bio = "Commercial property specialist with expertise in office spaces and retail locations throughout Albania.",
            agency = "Balkan Estate Commercial",
            licenseNumber = "AL-RE-2018-0123",
            specializations = listOf("Commercial", "Office", "Retail"),
            languages = listOf("English", "Albanian", "Chinese"),
            rating = 4.6f,
            reviewCount = 89,
            listingsCount = 18,
            soldCount = 72,
            yearsOfExperience = 6,
            isVerified = true
        ),
        Agent(
            id = "agent3",
            name = "Emma Wilson",
            email = "emma.wilson@balkanestate.com",
            phone = "+355 69 345 6789",
            imageUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=200",
            bio = "Focused on helping first-time buyers find their perfect home. Patient, knowledgeable, and dedicated to client satisfaction.",
            agency = "Balkan Estate Residential",
            licenseNumber = "AL-RE-2020-0256",
            specializations = listOf("Residential", "First-time Buyers", "Rentals"),
            languages = listOf("English", "Albanian", "French"),
            rating = 4.9f,
            reviewCount = 64,
            listingsCount = 12,
            soldCount = 45,
            yearsOfExperience = 4,
            isVerified = true
        ),
        Agent(
            id = "agent4",
            name = "David Brown",
            email = "david.brown@balkanestate.com",
            phone = "+355 69 456 7890",
            imageUrl = null,
            bio = "Land and development specialist. Expert in zoning regulations and investment opportunities.",
            agency = "Balkan Estate Development",
            licenseNumber = "AL-RE-2016-0089",
            specializations = listOf("Land", "Development", "Investment"),
            languages = listOf("English", "Albanian"),
            rating = 4.5f,
            reviewCount = 42,
            listingsCount = 8,
            soldCount = 28,
            yearsOfExperience = 8,
            isVerified = false
        )
    )
}
