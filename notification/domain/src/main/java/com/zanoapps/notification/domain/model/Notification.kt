package com.zanoapps.notification.domain.model

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType = NotificationType.GENERAL,
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val actionUrl: String = "",
    val propertyId: String = ""
)

enum class NotificationType {
    GENERAL,
    PRICE_DROP,
    NEW_LISTING,
    MESSAGE,
    SAVED_SEARCH_MATCH,
    PROPERTY_UPDATE,
    SYSTEM
}
