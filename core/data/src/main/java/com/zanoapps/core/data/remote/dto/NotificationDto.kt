package com.zanoapps.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    @SerialName("_id")
    val id: String,
    val type: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val data: Map<String, String>? = null
)

@Serializable
data class NotificationsResponse(
    val success: Boolean,
    val data: List<NotificationDto>
)
