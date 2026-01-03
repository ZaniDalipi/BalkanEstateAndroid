package com.zanoapps.notification.domain.model

import java.time.LocalDateTime

data class Notification(
    val id: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val timestamp: LocalDateTime,
    val isRead: Boolean = false,
    val data: Map<String, String> = emptyMap()
)

enum class NotificationType {
    NEW_MESSAGE,
    PROPERTY_ALERT,
    PRICE_DROP,
    VIEWING_REMINDER,
    SAVED_SEARCH_MATCH,
    OFFER_UPDATE,
    SYSTEM
}
