package com.zanoapps.agent.data.repository

import com.zanoapps.agent.domain.model.Agent
import com.zanoapps.agent.domain.repository.AgentRepository
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * Agent Repository implementation.
 * Uses mock data for now - will be replaced with Room + API integration.
 */
class AgentRepositoryImpl : AgentRepository {

    private val agentsFlow = MutableStateFlow(getMockAgents())
    private val listingsFlow = MutableStateFlow<Map<String, List<BalkanEstateProperty>>>(emptyMap())

    override fun getAgents(): Flow<List<Agent>> = agentsFlow

    override fun getAgentById(agentId: String): Flow<Agent?> {
        return agentsFlow.map { agents -> agents.find { it.id == agentId } }
    }

    override fun getAgentListings(agentId: String): Flow<List<BalkanEstateProperty>> {
        return listingsFlow.map { it[agentId] ?: getMockListings(agentId) }
    }

    override suspend fun refreshAgents(): EmptyResult<DataError.Network> {
        // TODO: Fetch from API and cache to Room
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

    private fun getMockAgents(): List<Agent> = listOf(
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

    private fun getMockListings(agentId: String): List<BalkanEstateProperty> = listOf(
        BalkanEstateProperty(
            id = "prop_${agentId}_1",
            title = "Modern Apartment in City Center",
            price = 185000.0,
            currency = "EUR",
            imageUrl = "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267",
            bedrooms = 2,
            bathrooms = 1,
            squareFootage = 85,
            address = "Rruga Myslym Shyri",
            city = "Tirana",
            country = "Albania",
            latitude = 41.3275,
            longitude = 19.8187,
            propertyType = "Apartment",
            listingType = "Sale",
            agentName = agentsFlow.value.find { it.id == agentId }?.name ?: "Agent",
            isFeatured = true,
            isUrgent = false
        ),
        BalkanEstateProperty(
            id = "prop_${agentId}_2",
            title = "Spacious Villa with Garden",
            price = 450000.0,
            currency = "EUR",
            imageUrl = "https://images.unsplash.com/photo-1564013799919-ab600027ffc6",
            bedrooms = 4,
            bathrooms = 3,
            squareFootage = 280,
            address = "Rruga e Elbasanit",
            city = "Tirana",
            country = "Albania",
            latitude = 41.3290,
            longitude = 19.8200,
            propertyType = "Villa",
            listingType = "Sale",
            agentName = agentsFlow.value.find { it.id == agentId }?.name ?: "Agent",
            isFeatured = false,
            isUrgent = true
        )
    )
}
