package com.zanoapps.notification.data.mapper

import com.zanoapps.notification.data.local.entity.NotificationEntity
import com.zanoapps.notification.data.remote.dto.NotificationDto
import com.zanoapps.notification.domain.model.Notification
import com.zanoapps.notification.domain.model.NotificationPriority
import com.zanoapps.notification.domain.model.NotificationType

/**
 * Mapper functions for notification data transformations.
 */

// ============ Entity to Domain ============

fun NotificationEntity.toDomain(): Notification {
    return Notification(
        id = id,
        title = title,
        message = message,
        type = type.toNotificationType(),
        priority = priority.toNotificationPriority(),
        createdAt = createdAt,
        isRead = isRead,
        propertyId = propertyId,
        propertyTitle = propertyTitle,
        propertyImageUrl = propertyImageUrl,
        price = price,
        oldPrice = oldPrice,
        currency = currency,
        deepLink = deepLink,
        senderName = senderName,
        senderAvatarUrl = senderAvatarUrl,
        actionText = actionText,
        savedSearchId = savedSearchId,
        savedSearchName = savedSearchName,
        groupKey = groupKey,
        isDismissible = isDismissible,
        expiresAt = expiresAt
    )
}

fun List<NotificationEntity>.toDomain(): List<Notification> = map { it.toDomain() }

// ============ Domain to Entity ============

fun Notification.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        title = title,
        message = message,
        type = type.name,
        priority = priority.name,
        createdAt = createdAt,
        isRead = isRead,
        propertyId = propertyId,
        propertyTitle = propertyTitle,
        propertyImageUrl = propertyImageUrl,
        price = price,
        oldPrice = oldPrice,
        currency = currency,
        deepLink = deepLink,
        senderName = senderName,
        senderAvatarUrl = senderAvatarUrl,
        actionText = actionText,
        savedSearchId = savedSearchId,
        savedSearchName = savedSearchName,
        groupKey = groupKey,
        isDismissible = isDismissible,
        expiresAt = expiresAt
    )
}

// ============ DTO to Entity ============

fun NotificationDto.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        title = title,
        message = message,
        type = type,
        priority = priority ?: NotificationPriority.NORMAL.name,
        createdAt = createdAt,
        isRead = isRead ?: false,
        propertyId = propertyId,
        propertyTitle = propertyTitle,
        propertyImageUrl = propertyImageUrl,
        price = price,
        oldPrice = oldPrice,
        currency = currency,
        deepLink = deepLink,
        senderName = senderName,
        senderAvatarUrl = senderAvatarUrl,
        actionText = actionText,
        savedSearchId = savedSearchId,
        savedSearchName = savedSearchName,
        groupKey = groupKey,
        isDismissible = isDismissible ?: true,
        expiresAt = expiresAt
    )
}

fun List<NotificationDto>.toEntities(): List<NotificationEntity> = map { it.toEntity() }

// ============ DTO to Domain ============

fun NotificationDto.toDomain(): Notification {
    return toEntity().toDomain()
}

// ============ Enum Conversions ============

fun String.toNotificationType(): NotificationType {
    return try {
        NotificationType.valueOf(this)
    } catch (e: IllegalArgumentException) {
        NotificationType.SYSTEM
    }
}

fun String.toNotificationPriority(): NotificationPriority {
    return try {
        NotificationPriority.valueOf(this)
    } catch (e: IllegalArgumentException) {
        NotificationPriority.NORMAL
    }
}
