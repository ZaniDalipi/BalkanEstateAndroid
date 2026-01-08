package com.zanoapps.notification.data.mock

import com.zanoapps.notification.domain.model.Notification
import com.zanoapps.notification.domain.model.NotificationPriority
import com.zanoapps.notification.domain.model.NotificationType

/**
 * Mock data for notification development and testing.
 */
object MockNotificationData {

    fun getMockNotifications(): List<Notification> {
        val now = System.currentTimeMillis()
        val hourInMillis = 60 * 60 * 1000L
        val dayInMillis = 24 * hourInMillis

        return listOf(
            // Price drop notification
            Notification(
                id = "notif_1",
                title = "Price Drop Alert!",
                message = "A property you saved has reduced its price by 15%",
                type = NotificationType.PRICE_DROP,
                priority = NotificationPriority.HIGH,
                createdAt = now - (2 * hourInMillis),
                isRead = false,
                propertyId = "prop_123",
                propertyTitle = "Modern 3BR Apartment in Tirana",
                propertyImageUrl = "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=400",
                price = 85000.0,
                oldPrice = 100000.0,
                currency = "EUR",
                deepLink = "balkanestate://property/prop_123",
                actionText = "View Property"
            ),
            // New listing match
            Notification(
                id = "notif_2",
                title = "New Listing Matches Your Search",
                message = "2 new properties match your 'Apartments in Pristina' search",
                type = NotificationType.NEW_LISTING_MATCH,
                priority = NotificationPriority.NORMAL,
                createdAt = now - (5 * hourInMillis),
                isRead = false,
                savedSearchId = "search_456",
                savedSearchName = "Apartments in Pristina",
                deepLink = "balkanestate://saved-search/search_456",
                actionText = "View Matches"
            ),
            // Message from agent
            Notification(
                id = "notif_3",
                title = "New Message from Arben K.",
                message = "Hello! I would be happy to schedule a viewing for the property you inquired about.",
                type = NotificationType.MESSAGE,
                priority = NotificationPriority.HIGH,
                createdAt = now - (8 * hourInMillis),
                isRead = true,
                senderName = "Arben Krasniqi",
                senderAvatarUrl = "https://randomuser.me/api/portraits/men/32.jpg",
                deepLink = "balkanestate://messages/conv_789",
                actionText = "Reply"
            ),
            // Viewing reminder
            Notification(
                id = "notif_4",
                title = "Viewing Reminder",
                message = "You have a property viewing scheduled tomorrow at 2:00 PM",
                type = NotificationType.VIEWING_REMINDER,
                priority = NotificationPriority.URGENT,
                createdAt = now - (12 * hourInMillis),
                isRead = false,
                propertyId = "prop_456",
                propertyTitle = "Spacious Villa with Garden",
                propertyImageUrl = "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=400",
                deepLink = "balkanestate://viewing/viewing_123",
                actionText = "View Details"
            ),
            // Property status change
            Notification(
                id = "notif_5",
                title = "Property Status Updated",
                message = "A property you were interested in has been marked as 'Under Contract'",
                type = NotificationType.PROPERTY_STATUS_CHANGE,
                priority = NotificationPriority.NORMAL,
                createdAt = now - dayInMillis,
                isRead = true,
                propertyId = "prop_789",
                propertyTitle = "Cozy Studio Near University",
                propertyImageUrl = "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=400",
                deepLink = "balkanestate://property/prop_789"
            ),
            // Saved search alert
            Notification(
                id = "notif_6",
                title = "Weekly Search Summary",
                message = "5 new properties were added matching your 'Houses in Skopje' criteria this week",
                type = NotificationType.SAVED_SEARCH_ALERT,
                priority = NotificationPriority.LOW,
                createdAt = now - (2 * dayInMillis),
                isRead = false,
                savedSearchId = "search_101",
                savedSearchName = "Houses in Skopje",
                deepLink = "balkanestate://saved-search/search_101",
                actionText = "View All"
            ),
            // Agent response
            Notification(
                id = "notif_7",
                title = "Agent Responded to Your Inquiry",
                message = "Marina B. has responded to your inquiry about the property in Ohrid",
                type = NotificationType.AGENT_RESPONSE,
                priority = NotificationPriority.HIGH,
                createdAt = now - (3 * dayInMillis),
                isRead = true,
                senderName = "Marina Bojkova",
                senderAvatarUrl = "https://randomuser.me/api/portraits/women/44.jpg",
                propertyId = "prop_202",
                propertyTitle = "Lakefront Apartment in Ohrid",
                deepLink = "balkanestate://messages/conv_303",
                actionText = "View Response"
            ),
            // Favorite update
            Notification(
                id = "notif_8",
                title = "Saved Property Updated",
                message = "New photos have been added to a property in your favorites",
                type = NotificationType.FAVORITE_UPDATE,
                priority = NotificationPriority.LOW,
                createdAt = now - (4 * dayInMillis),
                isRead = true,
                propertyId = "prop_404",
                propertyTitle = "Penthouse with Panoramic View",
                propertyImageUrl = "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=400",
                deepLink = "balkanestate://property/prop_404"
            ),
            // System notification
            Notification(
                id = "notif_9",
                title = "Welcome to BalkanEstate!",
                message = "Thank you for joining. Start your property search today and find your dream home.",
                type = NotificationType.SYSTEM,
                priority = NotificationPriority.LOW,
                createdAt = now - (7 * dayInMillis),
                isRead = true,
                deepLink = "balkanestate://search",
                actionText = "Start Searching"
            ),
            // Promotional
            Notification(
                id = "notif_10",
                title = "Special Offer: Featured Listings",
                message = "Get 20% off on premium listing features this week only!",
                type = NotificationType.PROMOTIONAL,
                priority = NotificationPriority.LOW,
                createdAt = now - (10 * dayInMillis),
                isRead = true,
                deepLink = "balkanestate://premium",
                actionText = "Learn More",
                isDismissible = true,
                expiresAt = now + (3 * dayInMillis)
            )
        )
    }

    fun getSamplePriceDropNotification(): Notification {
        return Notification(
            id = "sample_price_drop_${System.currentTimeMillis()}",
            title = "Price Reduced!",
            message = "Great news! This property just dropped in price.",
            type = NotificationType.PRICE_DROP,
            priority = NotificationPriority.HIGH,
            createdAt = System.currentTimeMillis(),
            isRead = false,
            propertyId = "prop_sample",
            propertyTitle = "Sample Property",
            price = 75000.0,
            oldPrice = 85000.0,
            currency = "EUR"
        )
    }

    fun getSampleMessageNotification(senderName: String, message: String): Notification {
        return Notification(
            id = "sample_message_${System.currentTimeMillis()}",
            title = "New Message from $senderName",
            message = message,
            type = NotificationType.MESSAGE,
            priority = NotificationPriority.HIGH,
            createdAt = System.currentTimeMillis(),
            isRead = false,
            senderName = senderName
        )
    }
}
