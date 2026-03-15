package com.zanoapps.notification.presentation.notifications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    var state by mutableStateOf(NotificationState())
        private set

    private val eventChannel = Channel<NotificationEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadNotifications()
    }

    fun onAction(action: NotificationAction) {
        when (action) {
            NotificationAction.OnLoadNotifications -> loadNotifications()
            is NotificationAction.OnNotificationClick -> {
                viewModelScope.launch {
                    notificationRepository.markAsRead(action.notificationId)
                    val notification = state.notifications.find { it.id == action.notificationId }
                    if (notification != null && notification.propertyId.isNotEmpty()) {
                        eventChannel.send(NotificationEvent.NavigateToProperty(notification.propertyId))
                    }
                }
            }
            is NotificationAction.OnDeleteNotification -> {
                viewModelScope.launch {
                    notificationRepository.deleteNotification(action.notificationId)
                }
            }
            NotificationAction.OnMarkAllAsRead -> {
                viewModelScope.launch {
                    notificationRepository.markAllAsRead()
                }
            }
        }
    }

    private fun loadNotifications() {
        state = state.copy(isLoading = true)
        notificationRepository.getNotifications()
            .onEach { notifications ->
                state = state.copy(
                    notifications = notifications,
                    unreadCount = notifications.count { !it.isRead },
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }
}
