package com.zanoapps.core.data.mappers

import com.zanoapps.core.data.remote.dto.ConversationDto
import com.zanoapps.core.database.entity.ConversationEntity
import com.zanoapps.messaging.domain.model.Conversation
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun ConversationDto.toEntity(): ConversationEntity {
    return ConversationEntity(
        id = id,
        participantId = participantId,
        participantName = participantName,
        participantImageUrl = participantImageUrl,
        propertyId = propertyId,
        propertyTitle = propertyTitle,
        lastMessage = lastMessage,
        lastMessageTime = lastMessageTime,
        unreadCount = unreadCount,
        isOnline = isOnline,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun ConversationEntity.toDomain(): Conversation {
    return Conversation(
        id = id,
        participantId = participantId,
        participantName = participantName,
        participantImageUrl = participantImageUrl,
        propertyId = propertyId,
        propertyTitle = propertyTitle,
        lastMessage = lastMessage,
        lastMessageTime = lastMessageTime?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
        },
        unreadCount = unreadCount,
        isOnline = isOnline
    )
}

fun List<ConversationDto>.toEntities(): List<ConversationEntity> = map { it.toEntity() }
fun List<ConversationEntity>.toDomainList(): List<Conversation> = map { it.toDomain() }
