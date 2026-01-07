package com.zanoapps.agent.data.mappers

import com.zanoapps.agent.domain.model.Agent
import com.zanoapps.core.database.entity.AgentEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun AgentEntity.toDomain(): Agent {
    return Agent(
        id = id,
        name = name,
        email = email,
        phone = phone,
        imageUrl = imageUrl,
        bio = bio,
        agency = agency,
        licenseNumber = licenseNumber,
        specializations = specializations?.let {
            try { Json.decodeFromString<List<String>>(it) } catch (e: Exception) { emptyList() }
        } ?: emptyList(),
        languages = languages?.let {
            try { Json.decodeFromString<List<String>>(it) } catch (e: Exception) { emptyList() }
        } ?: emptyList(),
        rating = rating,
        reviewCount = reviewCount,
        listingsCount = listingsCount,
        soldCount = soldCount,
        yearsOfExperience = yearsOfExperience,
        isVerified = isVerified,
        createdAt = createdAt
    )
}

fun Agent.toEntity(): AgentEntity {
    return AgentEntity(
        id = id,
        name = name,
        email = email,
        phone = phone,
        imageUrl = imageUrl,
        bio = bio,
        agency = agency,
        licenseNumber = licenseNumber,
        specializations = Json.encodeToString(specializations),
        languages = Json.encodeToString(languages),
        rating = rating,
        reviewCount = reviewCount,
        listingsCount = listingsCount,
        soldCount = soldCount,
        yearsOfExperience = yearsOfExperience,
        isVerified = isVerified,
        createdAt = createdAt
    )
}

fun List<AgentEntity>.toDomainList(): List<Agent> = map { it.toDomain() }
fun List<Agent>.toEntities(): List<AgentEntity> = map { it.toEntity() }
