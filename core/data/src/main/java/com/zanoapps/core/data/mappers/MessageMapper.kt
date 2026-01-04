package com.zanoapps.core.data.mappers

import com.zanoapps.core.data.remote.dto.MessageDto
import com.zanoapps.core.database.entity.MessageEntity
import com.zanoapps.messaging.domain.model.Message
import com.zanoapps.messaging.domain.model.MessageType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun MessageDto.toEntity(): MessageEntity {
    return MessageEntity(
        id = id,
        conversationId = conversationId,
        senderId = senderId,
        content = content,
        timestamp = timestamp,
        isRead = isRead,
        messageType = messageType
    )
}

fun MessageEntity.toDomain(): Message {
    return Message(
        id = id,
        conversationId = conversationId,
        senderId = senderId,
        content = content,
        timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()),
        isRead = isRead,
        type = try {
            MessageType.valueOf(messageType)
        } catch (e: Exception) {
            MessageType.TEXT
        }
    )
}

fun Message.toEntity(): MessageEntity {
    return MessageEntity(
        id = id,
        conversationId = conversationId,
        senderId = senderId,
        content = content,
        timestamp = timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        isRead = isRead,
        messageType = type.name
    )
}

fun List<MessageDto>.toEntities(): List<MessageEntity> = map { it.toEntity() }
fun List<MessageEntity>.toDomainList(): List<Message> = map { it.toDomain() }
