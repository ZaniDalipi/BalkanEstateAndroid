package com.zanoapps.notification.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.core.domain.util.Result
import com.zanoapps.notification.domain.model.Notification
import com.zanoapps.notification.domain.model.NotificationError
import com.zanoapps.notification.domain.model.NotificationFilter
import com.zanoapps.notification.domain.model.NotificationPreferences
import com.zanoapps.notification.domain.model.NotificationSortOption
import com.zanoapps.notification.domain.repository.NotificationRepository
import com.zanoapps.notification.domain.validation.NotificationValidation
import com.zanoapps.presentation.ui.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for notification screens.
 */
class NotificationViewModel(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    var state by mutableStateOf(NotificationState())
        private set

    private val eventChannel = Channel<NotificationEvent>()
    val events = eventChannel.receiveAsFlow()

    private var deletedNotifications: List<Notification> = emptyList()

    init {
        loadNotifications()
        observeUnreadCount()
        observePreferences()
    }

    fun onAction(action: NotificationAction) {
        when (action) {
            // Navigation
            is NotificationAction.OnNotificationClick -> handleNotificationClick(action.notification)
            is NotificationAction.OnPropertyClick -> navigateToProperty(action.propertyId)
            is NotificationAction.OnSavedSearchClick -> navigateToSavedSearch(action.savedSearchId)
            is NotificationAction.OnMessageClick -> navigateToMessage(action.notificationId)
            NotificationAction.OnBackClick -> sendEvent(NotificationEvent.NavigateBack)
            NotificationAction.OnOpenSettings -> state = state.copy(showSettingsSheet = true)
            NotificationAction.OnCloseSettings -> state = state.copy(showSettingsSheet = false)

            // List actions
            NotificationAction.OnRefresh, NotificationAction.OnPullToRefresh -> refreshNotifications()
            NotificationAction.OnLoadMore -> loadMoreNotifications()
            NotificationAction.OnRetry -> loadNotifications()

            // Selection
            NotificationAction.OnToggleSelectionMode -> toggleSelectionMode()
            is NotificationAction.OnToggleSelection -> toggleNotificationSelection(action.notificationId)
            NotificationAction.OnSelectAll -> selectAllNotifications()
            NotificationAction.OnDeselectAll -> deselectAllNotifications()

            // Read/Unread
            is NotificationAction.OnMarkAsRead -> markAsRead(action.notificationId)
            is NotificationAction.OnMarkAsUnread -> markAsUnread(action.notificationId)
            NotificationAction.OnMarkSelectedAsRead -> markSelectedAsRead()
            NotificationAction.OnMarkAllAsRead -> state = state.copy(showMarkAllReadConfirmation = true)
            NotificationAction.OnConfirmMarkAllAsRead -> confirmMarkAllAsRead()
            NotificationAction.OnCancelMarkAllAsRead -> state = state.copy(showMarkAllReadConfirmation = false)

            // Delete
            is NotificationAction.OnDeleteNotification -> deleteNotification(action.notificationId)
            NotificationAction.OnDeleteSelected -> state = state.copy(showDeleteConfirmation = true)
            NotificationAction.OnDeleteAll -> state = state.copy(showDeleteConfirmation = true)
            NotificationAction.OnDeleteReadNotifications -> deleteReadNotifications()
            NotificationAction.OnConfirmDelete -> confirmDelete()
            NotificationAction.OnCancelDelete -> state = state.copy(showDeleteConfirmation = false)

            // Filter/Sort
            is NotificationAction.OnFilterTabChange -> changeFilterTab(action.tab)
            is NotificationAction.OnApplyFilter -> applyFilter(action.filter)
            NotificationAction.OnClearFilters -> clearFilters()
            is NotificationAction.OnSortChange -> changeSortOption(action.sortOption)
            NotificationAction.OnShowFilterSheet -> state = state.copy(showFilterSheet = true)
            NotificationAction.OnHideFilterSheet -> state = state.copy(showFilterSheet = false)
            NotificationAction.OnShowSortSheet -> state = state.copy(showSortSheet = true)
            NotificationAction.OnHideSortSheet -> state = state.copy(showSortSheet = false)

            // Preferences
            is NotificationAction.OnTogglePushNotifications -> updatePreference { it.copy(pushNotificationsEnabled = action.enabled) }
            is NotificationAction.OnTogglePriceDrops -> updatePreference { it.copy(priceDropsEnabled = action.enabled) }
            is NotificationAction.OnToggleNewListings -> updatePreference { it.copy(newListingsEnabled = action.enabled) }
            is NotificationAction.OnToggleMessages -> updatePreference { it.copy(messagesEnabled = action.enabled) }
            is NotificationAction.OnToggleViewingReminders -> updatePreference { it.copy(viewingRemindersEnabled = action.enabled) }
            is NotificationAction.OnTogglePropertyStatus -> updatePreference { it.copy(propertyStatusEnabled = action.enabled) }
            is NotificationAction.OnToggleSavedSearchAlerts -> updatePreference { it.copy(savedSearchAlertsEnabled = action.enabled) }
            is NotificationAction.OnTogglePromotional -> updatePreference { it.copy(promotionalEnabled = action.enabled) }
            is NotificationAction.OnToggleSystemAnnouncements -> updatePreference { it.copy(systemAnnouncementsEnabled = action.enabled) }
            is NotificationAction.OnToggleEmailNotifications -> updatePreference { it.copy(emailNotificationsEnabled = action.enabled) }
            is NotificationAction.OnUpdateEmail -> updateEmail(action.email)
            is NotificationAction.OnToggleSound -> updatePreference { it.copy(soundEnabled = action.enabled) }
            is NotificationAction.OnToggleVibration -> updatePreference { it.copy(vibrationEnabled = action.enabled) }
            is NotificationAction.OnToggleLockScreen -> updatePreference { it.copy(showOnLockScreen = action.enabled) }
            is NotificationAction.OnSetQuietHours -> updatePreference { it.copy(quietHoursStart = action.start, quietHoursEnd = action.end) }
            is NotificationAction.OnSetMinPriceDropPercentage -> updatePreference { it.copy(minPriceDropPercentage = action.percentage) }
            is NotificationAction.OnUpdatePreferences -> state = state.copy(preferences = action.preferences)
            NotificationAction.OnSavePreferences -> savePreferences()
            NotificationAction.OnResetPreferences -> resetPreferences()

            // Swipe actions
            is NotificationAction.OnSwipeToDelete -> deleteNotification(action.notificationId)
            is NotificationAction.OnSwipeToToggleRead -> toggleReadStatus(action.notificationId)

            // Error handling
            NotificationAction.OnDismissError -> state = state.copy(errorMessage = null)
        }
    }

    // ============ Loading ============

    private fun loadNotifications() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null)

            when (val result = notificationRepository.getNotifications(
                filter = state.currentFilter,
                sortOption = state.currentSortOption
            )) {
                is Result.Success -> {
                    state = state.copy(
                        notifications = result.data,
                        isLoading = false,
                        hasMoreNotifications = result.data.size >= PAGE_SIZE
                    )
                    loadSummary()
                }
                is Result.Error -> {
                    state = state.copy(
                        isLoading = false,
                        errorMessage = result.error.toErrorMessage()
                    )
                }
            }
        }
    }

    private fun refreshNotifications() {
        viewModelScope.launch {
            state = state.copy(isRefreshing = true)

            notificationRepository.refreshNotifications()

            when (val result = notificationRepository.getNotifications(
                filter = state.currentFilter,
                sortOption = state.currentSortOption
            )) {
                is Result.Success -> {
                    state = state.copy(
                        notifications = result.data,
                        isRefreshing = false,
                        currentPage = 0
                    )
                }
                is Result.Error -> {
                    state = state.copy(isRefreshing = false)
                    sendEvent(NotificationEvent.ShowError(UiText.DynamicString(result.error.toErrorMessage())))
                }
            }
        }
    }

    private fun loadMoreNotifications() {
        if (state.isLoadingMore || !state.hasMoreNotifications) return

        viewModelScope.launch {
            state = state.copy(isLoadingMore = true)

            val nextPage = state.currentPage + 1
            val filter = state.currentFilter.copy(
                offset = nextPage * PAGE_SIZE,
                limit = PAGE_SIZE
            )

            when (val result = notificationRepository.getNotifications(filter, state.currentSortOption)) {
                is Result.Success -> {
                    state = state.copy(
                        notifications = state.notifications + result.data,
                        isLoadingMore = false,
                        currentPage = nextPage,
                        hasMoreNotifications = result.data.size >= PAGE_SIZE
                    )
                }
                is Result.Error -> {
                    state = state.copy(isLoadingMore = false)
                }
            }
        }
    }

    private fun loadSummary() {
        viewModelScope.launch {
            when (val result = notificationRepository.getNotificationSummary()) {
                is Result.Success -> {
                    state = state.copy(summary = result.data)
                }
                is Result.Error -> { /* Ignore summary errors */ }
            }
        }
    }

    private fun observeUnreadCount() {
        notificationRepository.getUnreadCountFlow()
            .onEach { count ->
                state = state.copy(unreadCount = count)
            }
            .launchIn(viewModelScope)
    }

    private fun observePreferences() {
        notificationRepository.getPreferencesFlow()
            .onEach { preferences ->
                state = state.copy(preferences = preferences)
            }
            .launchIn(viewModelScope)
    }

    // ============ Navigation ============

    private fun handleNotificationClick(notification: Notification) {
        if (state.isSelectionMode) {
            toggleNotificationSelection(notification.id)
            return
        }

        // Mark as read when clicked
        markAsRead(notification.id)

        // Navigate based on notification type
        when {
            notification.deepLink != null -> {
                sendEvent(NotificationEvent.NavigateToDeepLink(notification.deepLink))
            }
            notification.propertyId != null -> {
                sendEvent(NotificationEvent.NavigateToProperty(notification.propertyId))
            }
            notification.savedSearchId != null -> {
                sendEvent(NotificationEvent.NavigateToSavedSearch(notification.savedSearchId))
            }
            notification.isMessage() -> {
                sendEvent(NotificationEvent.NavigateToMessage(notification.id))
            }
        }
    }

    private fun navigateToProperty(propertyId: String) {
        sendEvent(NotificationEvent.NavigateToProperty(propertyId))
    }

    private fun navigateToSavedSearch(savedSearchId: String) {
        sendEvent(NotificationEvent.NavigateToSavedSearch(savedSearchId))
    }

    private fun navigateToMessage(notificationId: String) {
        sendEvent(NotificationEvent.NavigateToMessage(notificationId))
    }

    // ============ Selection ============

    private fun toggleSelectionMode() {
        if (state.isSelectionMode) {
            state = state.copy(
                isSelectionMode = false,
                selectedNotificationIds = emptySet()
            )
        } else {
            state = state.copy(isSelectionMode = true)
        }
    }

    private fun toggleNotificationSelection(notificationId: String) {
        val currentSelection = state.selectedNotificationIds.toMutableSet()
        if (currentSelection.contains(notificationId)) {
            currentSelection.remove(notificationId)
        } else {
            currentSelection.add(notificationId)
        }
        state = state.copy(selectedNotificationIds = currentSelection)

        if (currentSelection.isEmpty()) {
            state = state.copy(isSelectionMode = false)
        }
    }

    private fun selectAllNotifications() {
        state = state.copy(
            selectedNotificationIds = state.notifications.map { it.id }.toSet()
        )
    }

    private fun deselectAllNotifications() {
        state = state.copy(selectedNotificationIds = emptySet())
    }

    // ============ Read/Unread ============

    private fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            when (notificationRepository.markAsRead(notificationId)) {
                is Result.Success -> {
                    state = state.copy(
                        notifications = state.notifications.map {
                            if (it.id == notificationId) it.copy(isRead = true) else it
                        }
                    )
                }
                is Result.Error -> { /* Ignore */ }
            }
        }
    }

    private fun markAsUnread(notificationId: String) {
        viewModelScope.launch {
            when (notificationRepository.markAsUnread(notificationId)) {
                is Result.Success -> {
                    state = state.copy(
                        notifications = state.notifications.map {
                            if (it.id == notificationId) it.copy(isRead = false) else it
                        }
                    )
                }
                is Result.Error -> { /* Ignore */ }
            }
        }
    }

    private fun toggleReadStatus(notificationId: String) {
        val notification = state.notifications.find { it.id == notificationId } ?: return
        if (notification.isRead) {
            markAsUnread(notificationId)
        } else {
            markAsRead(notificationId)
        }
    }

    private fun markSelectedAsRead() {
        viewModelScope.launch {
            val ids = state.selectedNotificationIds.toList()
            when (notificationRepository.markAsRead(ids)) {
                is Result.Success -> {
                    state = state.copy(
                        notifications = state.notifications.map {
                            if (it.id in ids) it.copy(isRead = true) else it
                        },
                        isSelectionMode = false,
                        selectedNotificationIds = emptySet()
                    )
                    sendEvent(NotificationEvent.ExitSelectionMode)
                }
                is Result.Error -> { /* Ignore */ }
            }
        }
    }

    private fun confirmMarkAllAsRead() {
        viewModelScope.launch {
            state = state.copy(showMarkAllReadConfirmation = false)

            when (notificationRepository.markAllAsRead()) {
                is Result.Success -> {
                    state = state.copy(
                        notifications = state.notifications.map { it.copy(isRead = true) }
                    )
                    sendEvent(NotificationEvent.AllNotificationsMarkedAsRead)
                }
                is Result.Error -> {
                    sendEvent(NotificationEvent.ShowError(UiText.DynamicString("Failed to mark all as read")))
                }
            }
        }
    }

    // ============ Delete ============

    private fun deleteNotification(notificationId: String) {
        viewModelScope.launch {
            val notification = state.notifications.find { it.id == notificationId }

            when (notificationRepository.deleteNotification(notificationId)) {
                is Result.Success -> {
                    state = state.copy(
                        notifications = state.notifications.filter { it.id != notificationId }
                    )
                    if (notification != null) {
                        sendEvent(NotificationEvent.ShowUndoDelete(notification))
                    } else {
                        sendEvent(NotificationEvent.NotificationDeleted)
                    }
                }
                is Result.Error -> {
                    sendEvent(NotificationEvent.ShowError(UiText.DynamicString("Failed to delete notification")))
                }
            }
        }
    }

    private fun confirmDelete() {
        viewModelScope.launch {
            state = state.copy(showDeleteConfirmation = false)

            val ids = state.selectedNotificationIds.toList()
            deletedNotifications = state.notifications.filter { it.id in ids }

            when (notificationRepository.deleteNotifications(ids)) {
                is Result.Success -> {
                    state = state.copy(
                        notifications = state.notifications.filter { it.id !in ids },
                        isSelectionMode = false,
                        selectedNotificationIds = emptySet()
                    )
                    sendEvent(NotificationEvent.NotificationsDeleted(ids.size))
                    sendEvent(NotificationEvent.ExitSelectionMode)
                }
                is Result.Error -> {
                    sendEvent(NotificationEvent.ShowError(UiText.DynamicString("Failed to delete notifications")))
                }
            }
        }
    }

    private fun deleteReadNotifications() {
        viewModelScope.launch {
            when (notificationRepository.deleteReadNotifications()) {
                is Result.Success -> {
                    state = state.copy(
                        notifications = state.notifications.filter { !it.isRead }
                    )
                    sendEvent(NotificationEvent.ShowToast(UiText.DynamicString("Read notifications deleted")))
                }
                is Result.Error -> {
                    sendEvent(NotificationEvent.ShowError(UiText.DynamicString("Failed to delete read notifications")))
                }
            }
        }
    }

    // ============ Filter/Sort ============

    private fun changeFilterTab(tab: NotificationFilterTab) {
        state = state.copy(selectedFilterTab = tab)
    }

    private fun applyFilter(filter: NotificationFilter) {
        state = state.copy(
            currentFilter = filter,
            showFilterSheet = false,
            currentPage = 0
        )
        loadNotifications()
    }

    private fun clearFilters() {
        state = state.copy(
            currentFilter = NotificationFilter.ALL,
            selectedFilterTab = NotificationFilterTab.ALL,
            currentPage = 0
        )
        loadNotifications()
    }

    private fun changeSortOption(sortOption: NotificationSortOption) {
        state = state.copy(
            currentSortOption = sortOption,
            showSortSheet = false
        )
        loadNotifications()
    }

    // ============ Preferences ============

    private fun updatePreference(update: (NotificationPreferences) -> NotificationPreferences) {
        val newPreferences = update(state.preferences)
        state = state.copy(preferences = newPreferences)
        savePreferences()
    }

    private fun updateEmail(email: String) {
        val error = NotificationValidation.validateEmail(email)
        if (error != null) {
            sendEvent(NotificationEvent.ShowError(UiText.DynamicString("Invalid email format")))
            return
        }
        updatePreference { it.copy(notificationEmail = email.ifBlank { null }) }
    }

    private fun savePreferences() {
        viewModelScope.launch {
            state = state.copy(isSavingPreferences = true)

            when (notificationRepository.updatePreferences(state.preferences)) {
                is Result.Success -> {
                    state = state.copy(isSavingPreferences = false)
                    sendEvent(NotificationEvent.PreferencesSaved)
                }
                is Result.Error -> {
                    state = state.copy(isSavingPreferences = false)
                    sendEvent(NotificationEvent.ShowError(UiText.DynamicString("Failed to save preferences")))
                }
            }
        }
    }

    private fun resetPreferences() {
        state = state.copy(preferences = NotificationPreferences())
        savePreferences()
        sendEvent(NotificationEvent.PreferencesReset)
    }

    // ============ Helpers ============

    private fun sendEvent(event: NotificationEvent) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    private fun NotificationError.toErrorMessage(): String {
        return when (this) {
            NotificationError.Network.NO_INTERNET -> "No internet connection"
            NotificationError.Network.SERVER_ERROR -> "Server error occurred"
            NotificationError.Network.TIMEOUT -> "Request timed out"
            NotificationError.Network.UNAUTHORIZED -> "Unauthorized access"
            NotificationError.Network.UNKNOWN -> "Unknown network error"
            NotificationError.Local.DATABASE_ERROR -> "Database error occurred"
            NotificationError.Local.NOT_FOUND -> "Notification not found"
            NotificationError.Local.STORAGE_FULL -> "Storage is full"
            NotificationError.Local.UNKNOWN -> "Unknown error occurred"
            NotificationError.Validation.INVALID_ID -> "Invalid notification ID"
            NotificationError.Validation.INVALID_EMAIL -> "Invalid email format"
            NotificationError.Validation.INVALID_QUIET_HOURS -> "Invalid quiet hours"
            NotificationError.Validation.INVALID_PERCENTAGE -> "Invalid percentage"
            NotificationError.Push.TOKEN_REGISTRATION_FAILED -> "Failed to register for push notifications"
            NotificationError.Push.PERMISSION_DENIED -> "Notification permission denied"
            NotificationError.Push.PLAY_SERVICES_UNAVAILABLE -> "Google Play Services unavailable"
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
