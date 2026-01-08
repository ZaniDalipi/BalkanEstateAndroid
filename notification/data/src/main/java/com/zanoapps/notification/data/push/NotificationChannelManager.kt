package com.zanoapps.notification.data.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat

/**
 * Manages notification channels for the app.
 */
object NotificationChannelManager {

    // Channel IDs
    const val CHANNEL_PRICE_DROPS = "price_drops"
    const val CHANNEL_NEW_LISTINGS = "new_listings"
    const val CHANNEL_MESSAGES = "messages"
    const val CHANNEL_VIEWINGS = "viewings"
    const val CHANNEL_PROPERTY_UPDATES = "property_updates"
    const val CHANNEL_GENERAL = "general"

    /**
     * Creates all notification channels for the app.
     * Should be called on app startup.
     */
    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val notificationManager = NotificationManagerCompat.from(context)

        val channels = listOf(
            createChannel(
                id = CHANNEL_PRICE_DROPS,
                name = "Price Drops",
                description = "Notifications about price reductions on saved properties",
                importance = NotificationManager.IMPORTANCE_HIGH
            ),
            createChannel(
                id = CHANNEL_NEW_LISTINGS,
                name = "New Listings",
                description = "Notifications about new properties matching your searches",
                importance = NotificationManager.IMPORTANCE_DEFAULT
            ),
            createChannel(
                id = CHANNEL_MESSAGES,
                name = "Messages",
                description = "Messages from agents and other users",
                importance = NotificationManager.IMPORTANCE_HIGH
            ),
            createChannel(
                id = CHANNEL_VIEWINGS,
                name = "Property Viewings",
                description = "Viewing reminders and updates",
                importance = NotificationManager.IMPORTANCE_HIGH
            ),
            createChannel(
                id = CHANNEL_PROPERTY_UPDATES,
                name = "Property Updates",
                description = "Updates about saved properties",
                importance = NotificationManager.IMPORTANCE_DEFAULT
            ),
            createChannel(
                id = CHANNEL_GENERAL,
                name = "General",
                description = "General notifications and announcements",
                importance = NotificationManager.IMPORTANCE_LOW
            )
        )

        channels.forEach { channel ->
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createChannel(
        id: String,
        name: String,
        description: String,
        importance: Int
    ): NotificationChannel {
        return NotificationChannel(id, name, importance).apply {
            this.description = description
            enableLights(true)
            enableVibration(importance >= NotificationManager.IMPORTANCE_DEFAULT)
        }
    }

    /**
     * Gets the appropriate channel ID for a notification type.
     */
    fun getChannelForType(type: String): String {
        return when (type) {
            "PRICE_DROP" -> CHANNEL_PRICE_DROPS
            "NEW_LISTING_MATCH", "SAVED_SEARCH_ALERT" -> CHANNEL_NEW_LISTINGS
            "MESSAGE", "AGENT_RESPONSE" -> CHANNEL_MESSAGES
            "VIEWING_SCHEDULED", "VIEWING_REMINDER" -> CHANNEL_VIEWINGS
            "PROPERTY_STATUS_CHANGE", "FAVORITE_UPDATE", "PROPERTY_SAVED" -> CHANNEL_PROPERTY_UPDATES
            else -> CHANNEL_GENERAL
        }
    }
}
