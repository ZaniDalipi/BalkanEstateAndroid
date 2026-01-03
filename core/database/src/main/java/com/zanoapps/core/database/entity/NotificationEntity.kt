package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val type: String, // NEW_MESSAGE, PROPERTY_ALERT, PRICE_DROP, etc.
    val title: String,
    val message: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val data: String?, // JSON object with additional data
    val syncedAt: Long = System.currentTimeMillis()
)
