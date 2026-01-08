package com.zanoapps.notification.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.notification.presentation.components.NotificationItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * Root composable for the notification screen.
 */
@Composable
fun NotificationScreenRoot(
    onNavigateBack: () -> Unit,
    onNavigateToProperty: (String) -> Unit,
    onNavigateToSavedSearch: (String) -> Unit,
    onNavigateToMessage: (String) -> Unit,
    viewModel: NotificationViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NotificationEvent.NavigateBack -> onNavigateBack()
                is NotificationEvent.NavigateToProperty -> onNavigateToProperty(event.propertyId)
                is NotificationEvent.NavigateToSavedSearch -> onNavigateToSavedSearch(event.savedSearchId)
                is NotificationEvent.NavigateToMessage -> onNavigateToMessage(event.conversationId)
                is NotificationEvent.NavigateToDeepLink -> { /* Handle deep link */ }
                NotificationEvent.NavigateToSettings -> { /* Show settings */ }
                NotificationEvent.NotificationMarkedAsRead -> { /* Handled */ }
                NotificationEvent.AllNotificationsMarkedAsRead -> {
                    scope.launch {
                        snackbarHostState.showSnackbar("All notifications marked as read")
                    }
                }
                NotificationEvent.NotificationDeleted -> { /* Handled */ }
                is NotificationEvent.NotificationsDeleted -> {
                    scope.launch {
                        snackbarHostState.showSnackbar("${event.count} notifications deleted")
                    }
                }
                NotificationEvent.PreferencesSaved -> {
                    scope.launch {
                        snackbarHostState.showSnackbar("Preferences saved")
                    }
                }
                NotificationEvent.PreferencesReset -> {
                    scope.launch {
                        snackbarHostState.showSnackbar("Preferences reset to defaults")
                    }
                }
                is NotificationEvent.ShowError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(event.message.asString())
                    }
                }
                NotificationEvent.NetworkError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar("Network error occurred")
                    }
                }
                is NotificationEvent.ShowToast -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(event.message.asString())
                    }
                }
                is NotificationEvent.ShowSnackbar -> {
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = event.message.asString(),
                            actionLabel = event.actionLabel?.asString(),
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            event.action?.invoke()
                        }
                    }
                }
                NotificationEvent.ScrollToTop -> { /* Scroll to top */ }
                NotificationEvent.ExitSelectionMode -> { /* Handled */ }
                is NotificationEvent.ShowUndoDelete -> {
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Notification deleted",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            // Undo delete - would need to implement in ViewModel
                        }
                    }
                }
            }
        }
    }

    NotificationScreen(
        state = viewModel.state,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationScreen(
    state: NotificationState,
    onAction: (NotificationAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var showMenu by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if (state.isSelectionMode) {
                        Text("${state.selectedCount} selected")
                    } else {
                        BadgedBox(
                            badge = {
                                if (state.unreadCount > 0) {
                                    Badge {
                                        Text(
                                            text = if (state.unreadCount > 99) "99+" else state.unreadCount.toString()
                                        )
                                    }
                                }
                            }
                        ) {
                            Text("Notifications")
                        }
                    }
                },
                navigationIcon = {
                    if (state.isSelectionMode) {
                        IconButton(onClick = { onAction(NotificationAction.OnToggleSelectionMode) }) {
                            Icon(Icons.Default.Close, contentDescription = "Cancel selection")
                        }
                    } else {
                        IconButton(onClick = { onAction(NotificationAction.OnBackClick) }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    if (state.isSelectionMode) {
                        // Selection mode actions
                        IconButton(onClick = { onAction(NotificationAction.OnMarkSelectedAsRead) }) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Mark as read")
                        }
                        IconButton(onClick = { onAction(NotificationAction.OnDeleteSelected) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    } else {
                        // Normal mode actions
                        IconButton(onClick = { onAction(NotificationAction.OnOpenSettings) }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }

                        Box {
                            IconButton(onClick = { showMenu = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "More options")
                            }

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Select items") },
                                    onClick = {
                                        showMenu = false
                                        onAction(NotificationAction.OnToggleSelectionMode)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Mark all as read") },
                                    onClick = {
                                        showMenu = false
                                        onAction(NotificationAction.OnMarkAllAsRead)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Delete read notifications") },
                                    onClick = {
                                        showMenu = false
                                        onAction(NotificationAction.OnDeleteReadNotifications)
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter tabs
            ScrollableTabRow(
                selectedTabIndex = state.selectedFilterTab.ordinal,
                containerColor = Color.White,
                contentColor = BalkanEstatePrimaryBlue,
                edgePadding = 16.dp
            ) {
                NotificationFilterTab.entries.forEach { tab ->
                    val count = when (tab) {
                        NotificationFilterTab.ALL -> state.notifications.size
                        NotificationFilterTab.UNREAD -> state.notifications.count { !it.isRead }
                        NotificationFilterTab.MESSAGES -> state.notifications.count { it.isMessage() }
                        NotificationFilterTab.PROPERTY_UPDATES -> state.notifications.count {
                            it.type in setOf(
                                com.zanoapps.notification.domain.model.NotificationType.PRICE_DROP,
                                com.zanoapps.notification.domain.model.NotificationType.PROPERTY_STATUS_CHANGE,
                                com.zanoapps.notification.domain.model.NotificationType.NEW_LISTING_MATCH
                            )
                        }
                    }

                    Tab(
                        selected = state.selectedFilterTab == tab,
                        onClick = { onAction(NotificationAction.OnFilterTabChange(tab)) },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(tab.title)
                                if (count > 0) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "($count)",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    )
                }
            }

            HorizontalDivider()

            // Content
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
                    }
                }

                state.errorMessage != null && state.notifications.isEmpty() -> {
                    ErrorState(
                        message = state.errorMessage,
                        onRetry = { onAction(NotificationAction.OnRetry) }
                    )
                }

                state.getFilteredNotifications().isEmpty() -> {
                    EmptyState(filterTab = state.selectedFilterTab)
                }

                else -> {
                    PullToRefreshBox(
                        isRefreshing = state.isRefreshing,
                        onRefresh = { onAction(NotificationAction.OnPullToRefresh) }
                    ) {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = state.getFilteredNotifications(),
                                key = { it.id }
                            ) { notification ->
                                NotificationItem(
                                    notification = notification,
                                    isSelected = notification.id in state.selectedNotificationIds,
                                    isSelectionMode = state.isSelectionMode,
                                    onClick = { onAction(NotificationAction.OnNotificationClick(notification)) },
                                    onSwipeToDelete = { onAction(NotificationAction.OnSwipeToDelete(notification.id)) },
                                    onSwipeToToggleRead = { onAction(NotificationAction.OnSwipeToToggleRead(notification.id)) }
                                )
                                HorizontalDivider()
                            }

                            // Load more indicator
                            if (state.isLoadingMore) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            color = BalkanEstatePrimaryBlue
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Load more when reaching end
                    LaunchedEffect(listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index) {
                        val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                        val totalItems = state.getFilteredNotifications().size
                        if (lastVisibleIndex >= totalItems - 3 && state.hasMoreNotifications && !state.isLoadingMore) {
                            onAction(NotificationAction.OnLoadMore)
                        }
                    }
                }
            }
        }

        // Selection mode bottom bar
        AnimatedVisibility(
            visible = state.isSelectionMode && state.selectedCount > 0,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            SelectionBottomBar(
                selectedCount = state.selectedCount,
                isAllSelected = state.isAllSelected,
                onSelectAll = { onAction(NotificationAction.OnSelectAll) },
                onDeselectAll = { onAction(NotificationAction.OnDeselectAll) },
                onMarkAsRead = { onAction(NotificationAction.OnMarkSelectedAsRead) },
                onDelete = { onAction(NotificationAction.OnDeleteSelected) },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }

    // Mark all as read confirmation dialog
    if (state.showMarkAllReadConfirmation) {
        AlertDialog(
            onDismissRequest = { onAction(NotificationAction.OnCancelMarkAllAsRead) },
            title = { Text("Mark All as Read") },
            text = { Text("Are you sure you want to mark all notifications as read?") },
            confirmButton = {
                Button(
                    onClick = { onAction(NotificationAction.OnConfirmMarkAllAsRead) },
                    colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue)
                ) {
                    Text("Mark All Read")
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(NotificationAction.OnCancelMarkAllAsRead) }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Delete confirmation dialog
    if (state.showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { onAction(NotificationAction.OnCancelDelete) },
            title = { Text("Delete Notifications") },
            text = { Text("Are you sure you want to delete ${state.selectedCount} notification(s)?") },
            confirmButton = {
                Button(
                    onClick = { onAction(NotificationAction.OnConfirmDelete) },
                    colors = ButtonDefaults.buttonColors(containerColor = BalkanEstateRed)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(NotificationAction.OnCancelDelete) }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Settings sheet
    if (state.showSettingsSheet) {
        NotificationSettingsSheet(
            preferences = state.preferences,
            isSaving = state.isSavingPreferences,
            onAction = onAction,
            onDismiss = { onAction(NotificationAction.OnCloseSettings) }
        )
    }
}

@Composable
private fun EmptyState(filterTab: NotificationFilterTab) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = BalkanEstatePrimaryBlue.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = when (filterTab) {
                NotificationFilterTab.ALL -> "No Notifications"
                NotificationFilterTab.UNREAD -> "No Unread Notifications"
                NotificationFilterTab.MESSAGES -> "No Messages"
                NotificationFilterTab.PROPERTY_UPDATES -> "No Property Updates"
            },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = when (filterTab) {
                NotificationFilterTab.ALL -> "You're all caught up! We'll notify you about price drops, new listings, and messages."
                NotificationFilterTab.UNREAD -> "All notifications have been read."
                NotificationFilterTab.MESSAGES -> "No messages from agents yet."
                NotificationFilterTab.PROPERTY_UPDATES -> "No property updates yet. Save properties to get notified about changes."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Oops!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = BalkanEstateRed
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue)
        ) {
            Text("Try Again")
        }
    }
}

@Composable
private fun SelectionBottomBar(
    selectedCount: Int,
    isAllSelected: Boolean,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    onMarkAsRead: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = if (isAllSelected) onDeselectAll else onSelectAll) {
            Text(if (isAllSelected) "Deselect All" else "Select All")
        }

        Row {
            OutlinedButton(
                onClick = onMarkAsRead,
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Read")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(containerColor = BalkanEstateRed),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Delete")
            }
        }
    }
}
