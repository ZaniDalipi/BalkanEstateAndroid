package com.zanoapps.notification.presentation.notifications

import com.zanoapps.presentation.ui.UiText

sealed interface NotificationsEvent {
    data class Error(val error: UiText) : NotificationsEvent
    data class NavigateToMessage(val conversationId: String) : NotificationsEvent
    data class NavigateToProperty(val propertyId: String) : NotificationsEvent
    data class NavigateToSavedSearch(val searchId: String) : NotificationsEvent
}
