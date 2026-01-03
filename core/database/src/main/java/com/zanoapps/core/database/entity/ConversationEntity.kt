package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey
    val id: String,
    val participantId: String,
    val participantName: String,
    val participantImageUrl: String?,
    val propertyId: String?,
    val propertyTitle: String?,
    val lastMessage: String?,
    val lastMessageTime: Long?,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long,
    val syncedAt: Long = System.currentTimeMillis()
)
