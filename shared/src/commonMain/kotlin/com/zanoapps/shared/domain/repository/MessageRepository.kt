package com.zanoapps.shared.domain.repository

import com.zanoapps.shared.domain.model.Conversation
import com.zanoapps.shared.domain.model.CreateConversationRequest
import com.zanoapps.shared.domain.model.Message
import com.zanoapps.shared.domain.model.SendMessageRequest
import com.zanoapps.shared.util.DataError
import com.zanoapps.shared.util.Result
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    /**
     * Get all conversations for the current user
     */
    fun getConversations(): Flow<List<Conversation>>

    /**
     * Get a single conversation by ID
     */
    suspend fun getConversation(conversationId: String): Result<Conversation, DataError.Network>

    /**
     * Get messages for a conversation
     */
    fun getMessages(conversationId: String): Flow<List<Message>>

    /**
     * Send a message
     */
    suspend fun sendMessage(request: SendMessageRequest): Result<Message, DataError.Network>

    /**
     * Create a new conversation
     */
    suspend fun createConversation(request: CreateConversationRequest): Result<Conversation, DataError.Network>

    /**
     * Mark conversation as read
     */
    suspend fun markAsRead(conversationId: String): Result<Unit, DataError.Network>

    /**
     * Delete a conversation
     */
    suspend fun deleteConversation(conversationId: String): Result<Unit, DataError.Network>

    /**
     * Get unread messages count
     */
    fun getUnreadCount(): Flow<Int>
}
