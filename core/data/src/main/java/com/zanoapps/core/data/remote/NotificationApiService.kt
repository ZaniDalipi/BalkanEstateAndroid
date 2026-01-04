package com.zanoapps.core.data.remote

import com.zanoapps.core.data.networking.constructRoute
import com.zanoapps.core.data.networking.safeCall
import com.zanoapps.core.data.remote.dto.NotificationsResponse
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.url

class NotificationApiService(
    private val httpClient: HttpClient
) {
    suspend fun getNotifications(): Result<NotificationsResponse, DataError.Network> {
        return safeCall {
            httpClient.get {
                url(constructRoute("/api/notifications"))
            }
        }
    }

    suspend fun markAsRead(notificationId: String): Result<Unit, DataError.Network> {
        return safeCall {
            httpClient.put {
                url(constructRoute("/api/notifications/$notificationId/read"))
            }
        }
    }

    suspend fun markAllAsRead(): Result<Unit, DataError.Network> {
        return safeCall {
            httpClient.put {
                url(constructRoute("/api/notifications/read-all"))
            }
        }
    }

    suspend fun deleteNotification(notificationId: String): Result<Unit, DataError.Network> {
        return safeCall {
            httpClient.delete {
                url(constructRoute("/api/notifications/$notificationId"))
            }
        }
    }

    suspend fun clearAllNotifications(): Result<Unit, DataError.Network> {
        return safeCall {
            httpClient.delete {
                url(constructRoute("/api/notifications"))
            }
        }
    }
}
