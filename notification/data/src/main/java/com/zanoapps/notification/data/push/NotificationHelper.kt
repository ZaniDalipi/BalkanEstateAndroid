package com.zanoapps.notification.data.push

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Helper class for notification-related operations.
 */
object NotificationHelper {

    const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001

    /**
     * Checks if notification permission is granted.
     */
    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Permission not needed for older versions
        }
    }

    /**
     * Requests notification permission on Android 13+.
     */
    fun requestNotificationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasNotificationPermission(activity)) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    /**
     * Checks if the user should be shown a rationale for notification permission.
     */
    fun shouldShowPermissionRationale(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            false
        }
    }

    /**
     * Gets the current FCM token.
     */
    suspend fun getFcmToken(): String? = suspendCancellableCoroutine { continuation ->
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                Timber.d("FCM Token: $token")
                continuation.resume(token)
            }
            .addOnFailureListener { exception ->
                Timber.e(exception, "Failed to get FCM token")
                continuation.resume(null)
            }
    }

    /**
     * Subscribes to a topic.
     */
    fun subscribeToTopic(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnSuccessListener {
                Timber.d("Subscribed to topic: $topic")
            }
            .addOnFailureListener { exception ->
                Timber.e(exception, "Failed to subscribe to topic: $topic")
            }
    }

    /**
     * Unsubscribes from a topic.
     */
    fun unsubscribeFromTopic(topic: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            .addOnSuccessListener {
                Timber.d("Unsubscribed from topic: $topic")
            }
            .addOnFailureListener { exception ->
                Timber.e(exception, "Failed to unsubscribe from topic: $topic")
            }
    }

    /**
     * Deletes the FCM instance ID and token.
     * Useful when user logs out.
     */
    suspend fun deleteToken(): Boolean = suspendCancellableCoroutine { continuation ->
        FirebaseMessaging.getInstance().deleteToken()
            .addOnSuccessListener {
                Timber.d("FCM token deleted")
                continuation.resume(true)
            }
            .addOnFailureListener { exception ->
                Timber.e(exception, "Failed to delete FCM token")
                continuation.resume(false)
            }
    }

    // Common notification topics
    object Topics {
        const val ALL_USERS = "all_users"
        const val PRICE_ALERTS = "price_alerts"
        const val NEW_LISTINGS = "new_listings"
        const val PROMOTIONS = "promotions"
    }
}
