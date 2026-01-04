package com.zanoapps.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ConversationEntity::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("conversationId")]
)
data class MessageEntity(
    @PrimaryKey
    val id: String,
    val conversationId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val messageType: String = "TEXT", // TEXT, IMAGE, PROPERTY_LINK
    val syncedAt: Long = System.currentTimeMillis()
)
