package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey val id: String,
    val agentName: String,
    val agentAvatar: String = "",
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0,
    val propertyTitle: String = "",
    val propertyImageUrl: String = "",
    val isOnline: Boolean = false,
    val isArchived: Boolean = false
)
