package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages",
    indices = [Index("conversationId")]
)
data class MessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val content: String,
    val timestamp: String,
    val isFromUser: Boolean,
    val isRead: Boolean = false
)
