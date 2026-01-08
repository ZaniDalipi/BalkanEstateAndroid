package com.zanoapps.notification.domain.model

import com.zanoapps.core.domain.util.Error

/**
 * Domain errors specific to notification operations.
 */
sealed interface NotificationError : Error {

    /**
     * Network-related errors.
     */
    enum class Network : NotificationError {
        /** No internet connection */
        NO_INTERNET,

        /** Server returned an error */
        SERVER_ERROR,

        /** Request timed out */
        TIMEOUT,

        /** Unauthorized access */
        UNAUTHORIZED,

        /** Unknown network error */
        UNKNOWN
    }

    /**
     * Local storage errors.
     */
    enum class Local : NotificationError {
        /** Database error */
        DATABASE_ERROR,

        /** Notification not found */
        NOT_FOUND,

        /** Storage is full */
        STORAGE_FULL,

        /** Unknown local error */
        UNKNOWN
    }

    /**
     * Validation errors.
     */
    enum class Validation : NotificationError {
        /** Invalid notification ID */
        INVALID_ID,

        /** Invalid email format */
        INVALID_EMAIL,

        /** Invalid time range for quiet hours */
        INVALID_QUIET_HOURS,

        /** Invalid price drop percentage */
        INVALID_PERCENTAGE
    }

    /**
     * Push notification errors.
     */
    enum class Push : NotificationError {
        /** FCM token registration failed */
        TOKEN_REGISTRATION_FAILED,

        /** Push notification permission denied */
        PERMISSION_DENIED,

        /** Google Play Services not available */
        PLAY_SERVICES_UNAVAILABLE
    }
}
