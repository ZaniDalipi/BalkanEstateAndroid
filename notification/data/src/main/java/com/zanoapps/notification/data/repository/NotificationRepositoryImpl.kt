package com.zanoapps.notification.data.repository

import com.zanoapps.notification.domain.model.Notification
import com.zanoapps.notification.domain.model.NotificationType
import com.zanoapps.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class NotificationRepositoryImpl : NotificationRepository {

    private val notifications = MutableStateFlow(generateMockNotifications())

    override fun getNotifications(): Flow<List<Notification>> {
        return notifications.map { it.sortedByDescending { n -> n.timestamp } }
    }

    override suspend fun markAsRead(notificationId: String) {
        notifications.value = notifications.value.map {
            if (it.id == notificationId) it.copy(isRead = true) else it
        }
    }

    override suspend fun markAllAsRead() {
        notifications.value = notifications.value.map { it.copy(isRead = true) }
    }

    override suspend fun deleteNotification(notificationId: String) {
        notifications.value = notifications.value.filter { it.id != notificationId }
    }

    override suspend fun getUnreadCount(): Int {
        return notifications.value.count { !it.isRead }
    }

    private fun generateMockNotifications(): List<Notification> {
        return listOf(
            Notification(
                id = "n1", title = "Price Drop Alert",
                message = "A property you saved has dropped in price by 10%",
                type = NotificationType.PRICE_DROP, isRead = false,
                timestamp = System.currentTimeMillis() - 3600000, propertyId = "p1"
            ),
            Notification(
                id = "n2", title = "New Listing Match",
                message = "A new 3-bedroom apartment in Skopje matches your saved search",
                type = NotificationType.SAVED_SEARCH_MATCH, isRead = false,
                timestamp = System.currentTimeMillis() - 7200000, propertyId = "p2"
            ),
            Notification(
                id = "n3", title = "New Message",
                message = "Agent Marko Petrovic sent you a message about your inquiry",
                type = NotificationType.MESSAGE, isRead = true,
                timestamp = System.currentTimeMillis() - 86400000
            ),
            Notification(
                id = "n4", title = "Property Updated",
                message = "The villa in Ohrid you bookmarked has new photos",
                type = NotificationType.PROPERTY_UPDATE, isRead = false,
                timestamp = System.currentTimeMillis() - 172800000, propertyId = "p3"
            ),
            Notification(
                id = "n5", title = "Welcome to Balkan Estate",
                message = "Start exploring properties across the Balkans",
                type = NotificationType.SYSTEM, isRead = true,
                timestamp = System.currentTimeMillis() - 604800000
            ),
            Notification(
                id = "n6", title = "New Listing",
                message = "A luxury penthouse was just listed in Belgrade center",
                type = NotificationType.NEW_LISTING, isRead = false,
                timestamp = System.currentTimeMillis() - 14400000, propertyId = "p4"
            )
        )
    }
}
