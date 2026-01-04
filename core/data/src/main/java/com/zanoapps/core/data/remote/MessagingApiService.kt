package com.zanoapps.core.data.remote

import com.zanoapps.core.data.networking.constructRoute
import com.zanoapps.core.data.networking.safeCall
import com.zanoapps.core.data.remote.dto.ConversationResponse
import com.zanoapps.core.data.remote.dto.ConversationsResponse
import com.zanoapps.core.data.remote.dto.CreateConversationRequest
import com.zanoapps.core.data.remote.dto.MessageResponse
import com.zanoapps.core.data.remote.dto.MessagesResponse
import com.zanoapps.core.data.remote.dto.SendMessageRequest
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType

class MessagingApiService(
    private val httpClient: HttpClient
) {
    suspend fun getConversations(): Result<ConversationsResponse, DataError.Network> {
        return safeCall {
            httpClient.get {
                url(constructRoute("/api/conversations"))
            }
        }
    }

    suspend fun getConversationById(conversationId: String): Result<ConversationResponse, DataError.Network> {
        return safeCall {
            httpClient.get {
                url(constructRoute("/api/conversations/$conversationId"))
            }
        }
    }

    suspend fun createConversation(request: CreateConversationRequest): Result<ConversationResponse, DataError.Network> {
        return safeCall {
            httpClient.post {
                url(constructRoute("/api/conversations"))
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    suspend fun deleteConversation(conversationId: String): Result<Unit, DataError.Network> {
        return safeCall {
            httpClient.delete {
                url(constructRoute("/api/conversations/$conversationId"))
            }
        }
    }

    suspend fun getMessages(conversationId: String): Result<MessagesResponse, DataError.Network> {
        return safeCall {
            httpClient.get {
                url(constructRoute("/api/conversations/$conversationId/messages"))
            }
        }
    }

    suspend fun sendMessage(request: SendMessageRequest): Result<MessageResponse, DataError.Network> {
        return safeCall {
            httpClient.post {
                url(constructRoute("/api/messages"))
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    suspend fun markConversationAsRead(conversationId: String): Result<Unit, DataError.Network> {
        return safeCall {
            httpClient.put {
                url(constructRoute("/api/conversations/$conversationId/read"))
            }
        }
    }
}
