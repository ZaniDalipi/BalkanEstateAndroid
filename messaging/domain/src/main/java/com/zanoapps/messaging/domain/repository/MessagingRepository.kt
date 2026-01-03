package com.zanoapps.messaging.domain.repository

import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.messaging.domain.model.Conversation
import com.zanoapps.messaging.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessagingRepository {
    fun getConversations(): Flow<List<Conversation>>
    fun getMessages(conversationId: String): Flow<List<Message>>
    suspend fun sendMessage(conversationId: String, content: String): EmptyResult<DataError.Network>
    suspend fun markAsRead(conversationId: String): EmptyResult<DataError.Network>
    suspend fun startConversation(participantId: String, propertyId: String?): Result<Conversation, DataError.Network>
    suspend fun deleteConversation(conversationId: String): EmptyResult<DataError.Network>
}
