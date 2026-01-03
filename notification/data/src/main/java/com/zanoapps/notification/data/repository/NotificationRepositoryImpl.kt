package com.zanoapps.notification.data.repository

import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.EmptyResult
import com.zanoapps.core.domain.util.Result
import com.zanoapps.notification.domain.model.Notification
import com.zanoapps.notification.domain.model.NotificationType
import com.zanoapps.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

class NotificationRepositoryImpl : NotificationRepository {

    private val notificationsFlow = MutableStateFlow(getMockNotifications())

    override fun getNotifications(): Flow<List<Notification>> {
        return notificationsFlow.map { notifications ->
            notifications.sortedByDescending { it.timestamp }
        }
    }

    override suspend fun markAsRead(notificationId: String): EmptyResult<DataError.Network> {
        delay(100)

        val currentNotifications = notificationsFlow.value.map { notification ->
            if (notification.id == notificationId) {
                notification.copy(isRead = true)
            } else {
                notification
            }
        }
        notificationsFlow.value = currentNotifications

        return Result.Success(Unit)
    }

    override suspend fun markAllAsRead(): EmptyResult<DataError.Network> {
        delay(200)

        val currentNotifications = notificationsFlow.value.map { notification ->
            notification.copy(isRead = true)
        }
        notificationsFlow.value = currentNotifications

        return Result.Success(Unit)
    }

    override suspend fun deleteNotification(notificationId: String): EmptyResult<DataError.Network> {
        delay(100)

        val currentNotifications = notificationsFlow.value.toMutableList()
        currentNotifications.removeIf { it.id == notificationId }
        notificationsFlow.value = currentNotifications

        return Result.Success(Unit)
    }

    override suspend fun clearAllNotifications(): EmptyResult<DataError.Network> {
        delay(200)
        notificationsFlow.value = emptyList()
        return Result.Success(Unit)
    }

    override fun getUnreadCount(): Flow<Int> {
        return notificationsFlow.map { notifications ->
            notifications.count { !it.isRead }
        }
    }

    private fun getMockNotifications(): List<Notification> = listOf(
        Notification(
            id = "notif1",
            type = NotificationType.NEW_MESSAGE,
            title = "New Message",
            message = "Sarah Johnson sent you a message about Modern Apartment in City Center",
            timestamp = LocalDateTime.now().minusMinutes(30),
            isRead = false,
            data = mapOf("conversationId" to "conv1")
        ),
        Notification(
            id = "notif2",
            type = NotificationType.PRICE_DROP,
            title = "Price Drop Alert",
            message = "The price of Luxury Villa with Pool has been reduced by 10%!",
            timestamp = LocalDateTime.now().minusHours(2),
            isRead = false,
            data = mapOf("propertyId" to "prop2")
        ),
        Notification(
            id = "notif3",
            type = NotificationType.VIEWING_REMINDER,
            title = "Viewing Reminder",
            message = "You have a property viewing scheduled for tomorrow at 2:00 PM",
            timestamp = LocalDateTime.now().minusHours(5),
            isRead = true,
            data = mapOf("propertyId" to "prop1")
        ),
        Notification(
            id = "notif4",
            type = NotificationType.SAVED_SEARCH_MATCH,
            title = "New Property Match",
            message = "3 new properties match your saved search \"2-bedroom apartments under $500k\"",
            timestamp = LocalDateTime.now().minusDays(1),
            isRead = true,
            data = mapOf("searchId" to "search1")
        ),
        Notification(
            id = "notif5",
            type = NotificationType.PROPERTY_ALERT,
            title = "Property Update",
            message = "A property you saved has new photos available",
            timestamp = LocalDateTime.now().minusDays(2),
            isRead = true,
            data = mapOf("propertyId" to "prop3")
        ),
        Notification(
            id = "notif6",
            type = NotificationType.SYSTEM,
            title = "Welcome to Balkan Estate!",
            message = "Start exploring properties in your area and find your dream home",
            timestamp = LocalDateTime.now().minusDays(7),
            isRead = true
        )
    )
}
