package com.zanoapps.notification.presentation.notifications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.util.Result
import com.zanoapps.notification.domain.model.NotificationType
import com.zanoapps.notification.domain.repository.NotificationRepository
import com.zanoapps.notification.presentation.R
import com.zanoapps.presentation.ui.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    var state by mutableStateOf(NotificationsState())
        private set

    private val eventChannel = Channel<NotificationsEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadNotifications()
        observeUnreadCount()
    }

    private fun loadNotifications() {
        state = state.copy(isLoading = true)
        notificationRepository.getNotifications()
            .onEach { notifications ->
                state = state.copy(
                    notifications = notifications,
                    isLoading = false,
                    isRefreshing = false
                )
            }
            .launchIn(viewModelScope)
    }

    private fun observeUnreadCount() {
        notificationRepository.getUnreadCount()
            .onEach { count ->
                state = state.copy(unreadCount = count)
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: NotificationsAction) {
        when (action) {
            NotificationsAction.OnBackClick -> Unit // Handled by navigation
            is NotificationsAction.OnNotificationClick -> handleNotificationClick(action.notificationId)
            is NotificationsAction.OnDeleteNotification -> deleteNotification(action.notificationId)
            NotificationsAction.OnMarkAllAsRead -> markAllAsRead()
            NotificationsAction.OnClearAll -> clearAll()
            NotificationsAction.OnRefresh -> refresh()
        }
    }

    private fun handleNotificationClick(notificationId: String) {
        val notification = state.notifications.find { it.id == notificationId } ?: return

        viewModelScope.launch {
            // Mark as read
            notificationRepository.markAsRead(notificationId)

            // Navigate based on notification type
            when (notification.type) {
                NotificationType.NEW_MESSAGE -> {
                    notification.data["conversationId"]?.let { conversationId ->
                        eventChannel.send(NotificationsEvent.NavigateToMessage(conversationId))
                    }
                }
                NotificationType.PRICE_DROP,
                NotificationType.PROPERTY_ALERT,
                NotificationType.VIEWING_REMINDER -> {
                    notification.data["propertyId"]?.let { propertyId ->
                        eventChannel.send(NotificationsEvent.NavigateToProperty(propertyId))
                    }
                }
                NotificationType.SAVED_SEARCH_MATCH -> {
                    notification.data["searchId"]?.let { searchId ->
                        eventChannel.send(NotificationsEvent.NavigateToSavedSearch(searchId))
                    }
                }
                NotificationType.OFFER_UPDATE,
                NotificationType.SYSTEM -> {
                    // No navigation needed
                }
            }
        }
    }

    private fun deleteNotification(notificationId: String) {
        viewModelScope.launch {
            when (notificationRepository.deleteNotification(notificationId)) {
                is Result.Success -> {
                    // Notification removed from flow automatically
                }
                is Result.Error -> {
                    eventChannel.send(
                        NotificationsEvent.Error(UiText.StringResource(R.string.error_delete_notification))
                    )
                }
            }
        }
    }

    private fun markAllAsRead() {
        viewModelScope.launch {
            when (notificationRepository.markAllAsRead()) {
                is Result.Success -> {
                    // Notifications updated in flow automatically
                }
                is Result.Error -> {
                    eventChannel.send(
                        NotificationsEvent.Error(UiText.StringResource(R.string.error_mark_as_read))
                    )
                }
            }
        }
    }

    private fun clearAll() {
        viewModelScope.launch {
            when (notificationRepository.clearAllNotifications()) {
                is Result.Success -> {
                    // Notifications cleared from flow automatically
                }
                is Result.Error -> {
                    eventChannel.send(
                        NotificationsEvent.Error(UiText.StringResource(R.string.error_clear_notifications))
                    )
                }
            }
        }
    }

    private fun refresh() {
        state = state.copy(isRefreshing = true)
        // Flow will automatically update
    }
}
