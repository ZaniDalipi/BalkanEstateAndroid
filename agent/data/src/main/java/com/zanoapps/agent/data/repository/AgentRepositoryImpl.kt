package com.zanoapps.agent.data.repository

import com.zanoapps.agent.domain.model.Agent
import com.zanoapps.agent.domain.model.Agency
import com.zanoapps.agent.domain.repository.AgentRepository
import com.zanoapps.core.database.dao.AgencyDao
import com.zanoapps.core.database.dao.AgentDao
import com.zanoapps.core.database.entity.AgencyEntity
import com.zanoapps.core.database.entity.AgentEntity
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AgentRepositoryImpl(
    private val agentDao: AgentDao,
    private val agencyDao: AgencyDao
) : AgentRepository {

    override fun getAgents(): Flow<List<Agent>> {
        return agentDao.getAllAgents().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAgencies(): Flow<List<Agency>> {
        return agencyDao.getAllAgencies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getAgentById(id: String): Result<Agent, DataError.Network> {
        val entity = agentDao.getById(id)
        return if (entity != null) {
            Result.Success(entity.toDomain())
        } else {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun getAgencyById(id: String): Result<Agency, DataError.Network> {
        val entity = agencyDao.getById(id)
        return if (entity != null) {
            Result.Success(entity.toDomain())
        } else {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun searchAgents(query: String): Result<List<Agent>, DataError.Network> {
        return try {
            Result.Success(agentDao.search(query).map { it.toDomain() })
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun searchAgencies(query: String): Result<List<Agency>, DataError.Network> {
        return try {
            Result.Success(agencyDao.search(query).map { it.toDomain() })
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}

fun AgentEntity.toDomain(): Agent {
    return Agent(
        id = id, name = name, avatarUrl = avatarUrl, agency = agency,
        specialization = specialization, location = location, phone = phone,
        email = email, rating = rating, reviewsCount = reviewsCount,
        listingsCount = listingsCount, soldCount = soldCount,
        yearsExperience = yearsExperience,
        languages = if (languages.isBlank()) emptyList() else languages.split(","),
        isVerified = isVerified, isPremium = isPremium, bio = bio
    )
}

fun Agent.toEntity(): AgentEntity {
    return AgentEntity(
        id = id, name = name, avatarUrl = avatarUrl, agency = agency,
        specialization = specialization, location = location, phone = phone,
        email = email, rating = rating, reviewsCount = reviewsCount,
        listingsCount = listingsCount, soldCount = soldCount,
        yearsExperience = yearsExperience, languages = languages.joinToString(","),
        isVerified = isVerified, isPremium = isPremium, bio = bio
    )
}

fun AgencyEntity.toDomain(): Agency {
    return Agency(
        id = id, name = name, logoUrl = logoUrl, address = address,
        city = city, country = country, phone = phone, email = email,
        website = website, rating = rating, reviewsCount = reviewsCount,
        agentsCount = agentsCount, listingsCount = listingsCount,
        description = description, isVerified = isVerified
    )
}

fun Agency.toEntity(): AgencyEntity {
    return AgencyEntity(
        id = id, name = name, logoUrl = logoUrl, address = address,
        city = city, country = country, phone = phone, email = email,
        website = website, rating = rating, reviewsCount = reviewsCount,
        agentsCount = agentsCount, listingsCount = listingsCount,
        description = description, isVerified = isVerified
    )
}
