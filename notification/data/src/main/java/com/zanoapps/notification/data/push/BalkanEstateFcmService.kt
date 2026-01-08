package com.zanoapps.notification.data.push

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zanoapps.notification.data.R
import com.zanoapps.notification.domain.model.Notification
import com.zanoapps.notification.domain.model.NotificationPriority
import com.zanoapps.notification.domain.model.NotificationType
import com.zanoapps.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.net.URL
import java.util.UUID

/**
 * Firebase Cloud Messaging service for handling push notifications.
 */
class BalkanEstateFcmService : FirebaseMessagingService() {

    private val notificationRepository: NotificationRepository by inject()
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("FCM Token refreshed: $token")

        // Register the new token with the backend
        serviceScope.launch {
            try {
                notificationRepository.registerPushToken(token)
                Timber.d("FCM Token registered successfully")
            } catch (e: Exception) {
                Timber.e(e, "Failed to register FCM token")
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Timber.d("FCM Message received from: ${remoteMessage.from}")

        // Handle data payload
        if (remoteMessage.data.isNotEmpty()) {
            Timber.d("Message data payload: ${remoteMessage.data}")
            handleDataMessage(remoteMessage.data)
        }

        // Handle notification payload (when app is in foreground)
        remoteMessage.notification?.let { notification ->
            Timber.d("Message notification body: ${notification.body}")
            showNotification(
                title = notification.title ?: "BalkanEstate",
                message = notification.body ?: "",
                data = remoteMessage.data
            )
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val notification = parseNotificationFromData(data)

        // Save notification locally
        serviceScope.launch {
            try {
                notificationRepository.saveNotificationLocally(notification)
                Timber.d("Notification saved locally: ${notification.id}")
            } catch (e: Exception) {
                Timber.e(e, "Failed to save notification locally")
            }
        }

        // Show the notification
        showNotification(
            title = notification.title,
            message = notification.message,
            data = data,
            notification = notification
        )
    }

    private fun parseNotificationFromData(data: Map<String, String>): Notification {
        return Notification(
            id = data["id"] ?: UUID.randomUUID().toString(),
            title = data["title"] ?: "BalkanEstate",
            message = data["message"] ?: data["body"] ?: "",
            type = data["type"]?.let { type ->
                try {
                    NotificationType.valueOf(type)
                } catch (e: Exception) {
                    NotificationType.SYSTEM
                }
            } ?: NotificationType.SYSTEM,
            priority = data["priority"]?.let { priority ->
                try {
                    NotificationPriority.valueOf(priority)
                } catch (e: Exception) {
                    NotificationPriority.NORMAL
                }
            } ?: NotificationPriority.NORMAL,
            createdAt = data["created_at"]?.toLongOrNull() ?: System.currentTimeMillis(),
            isRead = false,
            propertyId = data["property_id"],
            propertyTitle = data["property_title"],
            propertyImageUrl = data["property_image_url"],
            price = data["price"]?.toDoubleOrNull(),
            oldPrice = data["old_price"]?.toDoubleOrNull(),
            currency = data["currency"],
            deepLink = data["deep_link"],
            senderName = data["sender_name"],
            senderAvatarUrl = data["sender_avatar_url"],
            actionText = data["action_text"],
            savedSearchId = data["saved_search_id"],
            savedSearchName = data["saved_search_name"],
            groupKey = data["group_key"]
        )
    }

    private fun showNotification(
        title: String,
        message: String,
        data: Map<String, String>,
        notification: Notification? = null
    ) {
        val channelId = NotificationChannelManager.getChannelForType(
            data["type"] ?: "SYSTEM"
        )

        // Create pending intent for notification click
        val intent = createNotificationIntent(data)
        val pendingIntent = PendingIntent.getActivity(
            this,
            notification?.id.hashCode() ?: System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(getNotificationPriority(data["priority"]))

        // Add large icon if available
        data["property_image_url"]?.let { imageUrl ->
            loadImageAsBitmap(imageUrl)?.let { bitmap ->
                builder.setLargeIcon(bitmap)
            }
        }

        data["sender_avatar_url"]?.let { avatarUrl ->
            loadImageAsBitmap(avatarUrl)?.let { bitmap ->
                builder.setLargeIcon(bitmap)
            }
        }

        // Add big text style for longer messages
        builder.setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(message)
        )

        // Add price drop information if applicable
        notification?.let {
            if (it.type == NotificationType.PRICE_DROP && it.oldPrice != null && it.price != null) {
                val priceText = "${formatPrice(it.oldPrice)} → ${formatPrice(it.price)} ${it.currency ?: ""}"
                builder.setSubText(priceText)
            }
        }

        // Show the notification
        try {
            NotificationManagerCompat.from(this).notify(
                notification?.id.hashCode() ?: System.currentTimeMillis().toInt(),
                builder.build()
            )
        } catch (e: SecurityException) {
            Timber.e(e, "Notification permission not granted")
        }
    }

    private fun createNotificationIntent(data: Map<String, String>): Intent {
        // Create an intent that opens the app to the appropriate screen
        val intent = packageManager.getLaunchIntentForPackage(packageName)
            ?: Intent()

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        // Add data for deep linking
        data["deep_link"]?.let { deepLink ->
            intent.putExtra("deep_link", deepLink)
        }
        data["property_id"]?.let { propertyId ->
            intent.putExtra("property_id", propertyId)
        }
        data["saved_search_id"]?.let { savedSearchId ->
            intent.putExtra("saved_search_id", savedSearchId)
        }
        data["type"]?.let { type ->
            intent.putExtra("notification_type", type)
        }

        return intent
    }

    private fun getNotificationPriority(priority: String?): Int {
        return when (priority) {
            "URGENT" -> NotificationCompat.PRIORITY_MAX
            "HIGH" -> NotificationCompat.PRIORITY_HIGH
            "NORMAL" -> NotificationCompat.PRIORITY_DEFAULT
            "LOW" -> NotificationCompat.PRIORITY_LOW
            else -> NotificationCompat.PRIORITY_DEFAULT
        }
    }

    private fun loadImageAsBitmap(url: String): Bitmap? {
        return try {
            val connection = URL(url).openConnection()
            connection.doInput = true
            connection.connect()
            val input = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            Timber.e(e, "Failed to load image: $url")
            null
        }
    }

    private fun formatPrice(price: Double): String {
        return String.format("%,.0f", price)
    }
}
