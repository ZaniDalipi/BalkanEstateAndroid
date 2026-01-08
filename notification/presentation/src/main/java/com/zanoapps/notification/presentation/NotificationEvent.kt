package com.zanoapps.notification.presentation

import com.zanoapps.presentation.ui.UiText

/**
 * One-off events for notification screens.
 */
sealed interface NotificationEvent {

    // ============ Navigation Events ============

    /** Navigate to property details */
    data class NavigateToProperty(val propertyId: String) : NotificationEvent

    /** Navigate to saved search results */
    data class NavigateToSavedSearch(val savedSearchId: String) : NotificationEvent

    /** Navigate to message conversation */
    data class NavigateToMessage(val conversationId: String) : NotificationEvent

    /** Navigate to notification settings */
    data object NavigateToSettings : NotificationEvent

    /** Navigate back */
    data object NavigateBack : NotificationEvent

    /** Navigate to deep link */
    data class NavigateToDeepLink(val deepLink: String) : NotificationEvent

    // ============ Success Events ============

    /** Notification marked as read */
    data object NotificationMarkedAsRead : NotificationEvent

    /** All notifications marked as read */
    data object AllNotificationsMarkedAsRead : NotificationEvent

    /** Notification deleted */
    data object NotificationDeleted : NotificationEvent

    /** Multiple notifications deleted */
    data class NotificationsDeleted(val count: Int) : NotificationEvent

    /** Preferences saved successfully */
    data object PreferencesSaved : NotificationEvent

    /** Preferences reset to defaults */
    data object PreferencesReset : NotificationEvent

    // ============ Error Events ============

    /** Show error message */
    data class ShowError(val message: UiText) : NotificationEvent

    /** Network error occurred */
    data object NetworkError : NotificationEvent

    // ============ UI Events ============

    /** Show toast message */
    data class ShowToast(val message: UiText) : NotificationEvent

    /** Show snackbar with action */
    data class ShowSnackbar(
        val message: UiText,
        val actionLabel: UiText? = null,
        val action: (() -> Unit)? = null
    ) : NotificationEvent

    /** Scroll to top of list */
    data object ScrollToTop : NotificationEvent

    /** Exit selection mode */
    data object ExitSelectionMode : NotificationEvent

    /** Show undo option for delete */
    data class ShowUndoDelete(val notification: com.zanoapps.notification.domain.model.Notification) : NotificationEvent
}
