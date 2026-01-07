package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_chat_messages")
data class AIChatMessageEntity(
    @PrimaryKey
    val id: String,
    val sessionId: String, // Groups messages in a conversation session
    val userId: String,
    val content: String,
    val isFromUser: Boolean,
    val relatedPropertyIds: String?, // JSON array of property IDs mentioned/suggested
    val filtersApplied: String?, // JSON of filters extracted from AI conversation
    val timestamp: Long = System.currentTimeMillis()
)
