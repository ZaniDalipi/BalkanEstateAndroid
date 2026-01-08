package com.zanoapps.notification.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity for storing notifications locally.
 */
@Entity(
    tableName = "notifications",
    indices = [
        Index(value = ["isRead"]),
        Index(value = ["type"]),
        Index(value = ["createdAt"]),
        Index(value = ["propertyId"])
    ]
)
data class NotificationEntity(
    @PrimaryKey
    val id: String,

    val title: String,

    val message: String,

    val type: String,

    val priority: String,

    val createdAt: Long,

    val isRead: Boolean,

    val propertyId: String?,

    val propertyTitle: String?,

    val propertyImageUrl: String?,

    val price: Double?,

    val oldPrice: Double?,

    val currency: String?,

    val deepLink: String?,

    val senderName: String?,

    val senderAvatarUrl: String?,

    val actionText: String?,

    val savedSearchId: String?,

    val savedSearchName: String?,

    val groupKey: String?,

    val isDismissible: Boolean,

    val expiresAt: Long?
)
