package com.zanoapps.search.data.mappers

import com.zanoapps.core.database.entity.AIChatMessageEntity
import com.zanoapps.search.domain.model.AIChatMessage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun AIChatMessageEntity.toDomain(): AIChatMessage {
    return AIChatMessage(
        id = id,
        sessionId = sessionId,
        userId = userId,
        content = content,
        isFromUser = isFromUser,
        relatedPropertyIds = relatedPropertyIds?.let {
            try { Json.decodeFromString<List<String>>(it) } catch (e: Exception) { emptyList() }
        } ?: emptyList(),
        filtersApplied = filtersApplied,
        timestamp = timestamp
    )
}

fun AIChatMessage.toEntity(): AIChatMessageEntity {
    return AIChatMessageEntity(
        id = id,
        sessionId = sessionId,
        userId = userId,
        content = content,
        isFromUser = isFromUser,
        relatedPropertyIds = if (relatedPropertyIds.isNotEmpty()) Json.encodeToString(relatedPropertyIds) else null,
        filtersApplied = filtersApplied,
        timestamp = timestamp
    )
}

fun List<AIChatMessageEntity>.toDomainList(): List<AIChatMessage> = map { it.toDomain() }
