package com.zanoapps.core.data.mappers

import com.zanoapps.core.data.remote.dto.NotificationDto
import com.zanoapps.core.database.entity.NotificationEntity
import com.zanoapps.notification.domain.model.Notification
import com.zanoapps.notification.domain.model.NotificationType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun NotificationDto.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        type = type,
        title = title,
        message = message,
        timestamp = timestamp,
        isRead = isRead,
        data = data?.let { Json.encodeToString(it) }
    )
}

fun NotificationEntity.toDomain(): Notification {
    return Notification(
        id = id,
        type = try {
            NotificationType.valueOf(type)
        } catch (e: Exception) {
            NotificationType.SYSTEM
        },
        title = title,
        message = message,
        timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()),
        isRead = isRead,
        data = data?.let {
            try {
                Json.decodeFromString<Map<String, String>>(it)
            } catch (e: Exception) {
                emptyMap()
            }
        } ?: emptyMap()
    )
}

fun List<NotificationDto>.toEntities(): List<NotificationEntity> = map { it.toEntity() }
fun List<NotificationEntity>.toDomainList(): List<Notification> = map { it.toDomain() }
