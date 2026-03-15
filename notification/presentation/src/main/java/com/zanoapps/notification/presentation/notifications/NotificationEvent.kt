package com.zanoapps.notification.presentation.notifications

import com.zanoapps.presentation.ui.UiText

sealed interface NotificationEvent {
    data class Error(val error: UiText) : NotificationEvent
    data class NavigateToProperty(val propertyId: String) : NotificationEvent
}
