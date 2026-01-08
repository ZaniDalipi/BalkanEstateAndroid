package com.zanoapps.notification.domain.validation

import com.zanoapps.notification.domain.model.NotificationError
import com.zanoapps.notification.domain.model.NotificationPreferences

/**
 * Validation utilities for notification-related data.
 */
object NotificationValidation {

    private val EMAIL_REGEX = Regex(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )

    /**
     * Validates an email address format.
     */
    fun validateEmail(email: String?): NotificationError.Validation? {
        if (email.isNullOrBlank()) return null // Empty is valid (no email set)
        return if (!EMAIL_REGEX.matches(email)) {
            NotificationError.Validation.INVALID_EMAIL
        } else {
            null
        }
    }

    /**
     * Validates quiet hours time range.
     */
    fun validateQuietHours(start: Int?, end: Int?): NotificationError.Validation? {
        // Both null is valid (quiet hours disabled)
        if (start == null && end == null) return null

        // Both must be set if one is set
        if (start == null || end == null) {
            return NotificationError.Validation.INVALID_QUIET_HOURS
        }

        // Hours must be in valid range (0-23)
        if (start !in 0..23 || end !in 0..23) {
            return NotificationError.Validation.INVALID_QUIET_HOURS
        }

        return null
    }

    /**
     * Validates price drop percentage.
     */
    fun validatePriceDropPercentage(percentage: Int): NotificationError.Validation? {
        return if (percentage < 0 || percentage > 100) {
            NotificationError.Validation.INVALID_PERCENTAGE
        } else {
            null
        }
    }

    /**
     * Validates notification ID.
     */
    fun validateNotificationId(id: String?): NotificationError.Validation? {
        return if (id.isNullOrBlank()) {
            NotificationError.Validation.INVALID_ID
        } else {
            null
        }
    }

    /**
     * Validates notification preferences.
     * Returns the first validation error found, or null if valid.
     */
    fun validatePreferences(preferences: NotificationPreferences): NotificationError.Validation? {
        // Validate email if email notifications are enabled
        if (preferences.emailNotificationsEnabled) {
            validateEmail(preferences.notificationEmail)?.let { return it }
        }

        // Validate quiet hours
        validateQuietHours(preferences.quietHoursStart, preferences.quietHoursEnd)?.let { return it }

        // Validate price drop percentage
        validatePriceDropPercentage(preferences.minPriceDropPercentage)?.let { return it }

        return null
    }
}

/**
 * Result of preference validation.
 */
sealed interface PreferenceValidationResult {
    data object Valid : PreferenceValidationResult
    data class Invalid(val error: NotificationError.Validation) : PreferenceValidationResult
}

/**
 * Extension function to validate preferences and return a typed result.
 */
fun NotificationPreferences.validate(): PreferenceValidationResult {
    val error = NotificationValidation.validatePreferences(this)
    return if (error != null) {
        PreferenceValidationResult.Invalid(error)
    } else {
        PreferenceValidationResult.Valid
    }
}
