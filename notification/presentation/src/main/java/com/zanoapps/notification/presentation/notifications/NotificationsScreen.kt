package com.zanoapps.notification.presentation.notifications

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.BellIcon
import com.zanoapps.core.presentation.designsystem.DeleteIcon
import com.zanoapps.core.presentation.designsystem.HomeIcon
import com.zanoapps.core.presentation.designsystem.MessageIcon
import com.zanoapps.core.presentation.designsystem.SearchIcon
import com.zanoapps.core.presentation.designsystem.StarIcon
import com.zanoapps.core.presentation.designsystem.TrendingDownIcon
import com.zanoapps.notification.domain.model.Notification
import com.zanoapps.notification.domain.model.NotificationType
import com.zanoapps.notification.presentation.R
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun NotificationsScreenRoot(
    viewModel: NotificationsViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onNavigateToMessage: (String) -> Unit,
    onNavigateToProperty: (String) -> Unit,
    onNavigateToSavedSearch: (String) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.events.collect { event ->
            when (event) {
                is NotificationsEvent.Error -> {
                    Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
                }
                is NotificationsEvent.NavigateToMessage -> {
                    onNavigateToMessage(event.conversationId)
                }
                is NotificationsEvent.NavigateToProperty -> {
                    onNavigateToProperty(event.propertyId)
                }
                is NotificationsEvent.NavigateToSavedSearch -> {
                    onNavigateToSavedSearch(event.searchId)
                }
            }
        }
    }

    NotificationsScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                NotificationsAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationsScreen(
    state: NotificationsState,
    onAction: (NotificationsAction) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.notifications),
                            fontWeight = FontWeight.SemiBold
                        )
                        if (state.unreadCount > 0) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(BalkanEstatePrimaryBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (state.unreadCount > 9) "9+" else state.unreadCount.toString(),
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(NotificationsAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options"
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.mark_all_read)) },
                                onClick = {
                                    showMenu = false
                                    onAction(NotificationsAction.OnMarkAllAsRead)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.clear_all)) },
                                onClick = {
                                    showMenu = false
                                    onAction(NotificationsAction.OnClearAll)
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        if (state.isLoading && state.notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
            }
        } else if (state.notifications.isEmpty()) {
            EmptyNotificationsState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { onAction(NotificationsAction.OnRefresh) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = state.notifications,
                        key = { it.id }
                    ) { notification ->
                        SwipeableNotificationItem(
                            notification = notification,
                            onClick = { onAction(NotificationsAction.OnNotificationClick(notification.id)) },
                            onDelete = { onAction(NotificationsAction.OnDeleteNotification(notification.id)) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableNotificationItem(
    notification: Notification,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = DeleteIcon,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        },
        content = {
            NotificationItem(
                notification = notification,
                onClick = onClick
            )
        }
    )
}

@Composable
private fun NotificationItem(
    notification: Notification,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (notification.isRead) Color.White else Color(0xFFF0F7FF))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(getNotificationIconBackground(notification.type)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getNotificationIcon(notification.type),
                    contentDescription = null,
                    tint = getNotificationIconColor(notification.type),
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 15.sp,
                        fontWeight = if (notification.isRead) FontWeight.Medium else FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = formatTimestamp(notification.timestamp),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Unread indicator
            if (!notification.isRead) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(BalkanEstatePrimaryBlue)
                )
            }
        }
        HorizontalDivider(color = Color(0xFFE5E7EB))
    }
}

@Composable
private fun EmptyNotificationsState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = BellIcon,
                contentDescription = null,
                tint = BalkanEstatePrimaryBlue,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.no_notifications),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.no_notifications_description),
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

private fun getNotificationIcon(type: NotificationType): ImageVector {
    return when (type) {
        NotificationType.NEW_MESSAGE -> MessageIcon
        NotificationType.PROPERTY_ALERT -> HomeIcon
        NotificationType.PRICE_DROP -> TrendingDownIcon
        NotificationType.VIEWING_REMINDER -> StarIcon
        NotificationType.SAVED_SEARCH_MATCH -> SearchIcon
        NotificationType.OFFER_UPDATE -> HomeIcon
        NotificationType.SYSTEM -> BellIcon
    }
}

private fun getNotificationIconColor(type: NotificationType): Color {
    return when (type) {
        NotificationType.NEW_MESSAGE -> BalkanEstatePrimaryBlue
        NotificationType.PROPERTY_ALERT -> Color(0xFFEA580C)
        NotificationType.PRICE_DROP -> Color(0xFF16A34A)
        NotificationType.VIEWING_REMINDER -> Color(0xFFEAB308)
        NotificationType.SAVED_SEARCH_MATCH -> BalkanEstatePrimaryBlue
        NotificationType.OFFER_UPDATE -> Color(0xFF7C3AED)
        NotificationType.SYSTEM -> Color.Gray
    }
}

private fun getNotificationIconBackground(type: NotificationType): Color {
    return getNotificationIconColor(type).copy(alpha = 0.1f)
}

private fun formatTimestamp(time: LocalDateTime): String {
    val now = LocalDateTime.now()
    val minutesBetween = ChronoUnit.MINUTES.between(time, now)
    val hoursBetween = ChronoUnit.HOURS.between(time, now)
    val daysBetween = ChronoUnit.DAYS.between(time.toLocalDate(), now.toLocalDate())

    return when {
        minutesBetween < 60 -> "${minutesBetween}m ago"
        hoursBetween < 24 -> "${hoursBetween}h ago"
        daysBetween == 1L -> "Yesterday"
        daysBetween < 7L -> "${daysBetween}d ago"
        else -> time.format(DateTimeFormatter.ofPattern("MMM d"))
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationsScreenPreview() {
    BalkanEstateTheme {
        NotificationsScreen(
            state = NotificationsState(
                notifications = listOf(
                    Notification(
                        id = "1",
                        type = NotificationType.NEW_MESSAGE,
                        title = "New Message",
                        message = "Sarah Johnson sent you a message",
                        timestamp = LocalDateTime.now().minusMinutes(30),
                        isRead = false
                    ),
                    Notification(
                        id = "2",
                        type = NotificationType.PRICE_DROP,
                        title = "Price Drop",
                        message = "A property you saved has reduced its price",
                        timestamp = LocalDateTime.now().minusHours(2),
                        isRead = true
                    )
                ),
                unreadCount = 1
            ),
            onAction = {}
        )
    }
}
