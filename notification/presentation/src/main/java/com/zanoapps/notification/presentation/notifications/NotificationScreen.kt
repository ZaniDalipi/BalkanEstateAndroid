package com.zanoapps.notification.presentation.notifications

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateBackground
import com.zanoapps.core.presentation.designsystem.BalkanEstateBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateCardBackground
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.BalkanEstateTextPrimary
import com.zanoapps.core.presentation.designsystem.BalkanEstateTextSecondary
import com.zanoapps.notification.domain.model.Notification
import com.zanoapps.notification.domain.model.NotificationType
import com.zanoapps.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotificationScreenRoot(
    viewModel: NotificationViewModel = koinViewModel(),
    onNavigateToProperty: (String) -> Unit = {}
) {
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is NotificationEvent.NavigateToProperty -> onNavigateToProperty(event.propertyId)
            is NotificationEvent.Error -> Unit
        }
    }

    NotificationScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationScreen(
    state: NotificationState,
    onAction: (NotificationAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Notifications",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        if (state.unreadCount > 0) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(BalkanEstateRed)
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${state.unreadCount}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                actions = {
                    if (state.notifications.any { !it.isRead }) {
                        TextButton(onClick = { onAction(NotificationAction.OnMarkAllAsRead) }) {
                            Text(
                                text = "Mark all read",
                                color = BalkanEstatePrimaryBlue,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BalkanEstateCardBackground
                )
            )
        },
        containerColor = BalkanEstateBackground
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
                }
            }
            state.notifications.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = BalkanEstateGray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No notifications yet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = BalkanEstateTextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "You'll see notifications about your properties and searches here",
                            fontSize = 14.sp,
                            color = BalkanEstateTextSecondary
                        )
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(
                        items = state.notifications,
                        key = { it.id }
                    ) { notification ->
                        NotificationItem(
                            notification = notification,
                            onNotificationClick = {
                                onAction(NotificationAction.OnNotificationClick(notification.id))
                            },
                            onDeleteNotification = {
                                onAction(NotificationAction.OnDeleteNotification(notification.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationItem(
    notification: Notification,
    onNotificationClick: () -> Unit,
    onDeleteNotification: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDeleteNotification()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> BalkanEstateRed
                    else -> Color.Transparent
                },
                label = "dismissColor"
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        },
        enableDismissFromStartToEnd = false
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNotificationClick() },
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (!notification.isRead) {
                    BalkanEstateBlue.copy(alpha = 0.05f)
                } else {
                    BalkanEstateCardBackground
                }
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Notification type icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(getNotificationIconBackground(notification.type)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getNotificationIcon(notification.type),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = notification.title,
                            fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 15.sp,
                            color = BalkanEstateTextPrimary,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = formatTimestamp(notification.timestamp),
                            fontSize = 12.sp,
                            color = BalkanEstateTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = notification.message,
                        fontSize = 13.sp,
                        color = BalkanEstateTextSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Unread indicator
                if (!notification.isRead) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(BalkanEstatePrimaryBlue)
                    )
                }
            }
            HorizontalDivider(
                color = BalkanEstateGray.copy(alpha = 0.2f),
                thickness = 0.5.dp
            )
        }
    }
}

private fun getNotificationIcon(type: NotificationType): ImageVector {
    return when (type) {
        NotificationType.GENERAL -> Icons.Default.Notifications
        NotificationType.PRICE_DROP -> Icons.Default.Star
        NotificationType.NEW_LISTING -> Icons.Default.Home
        NotificationType.MESSAGE -> Icons.Default.Email
        NotificationType.SAVED_SEARCH_MATCH -> Icons.Default.Search
        NotificationType.PROPERTY_UPDATE -> Icons.Default.Info
        NotificationType.SYSTEM -> Icons.Default.Notifications
    }
}

private fun getNotificationIconBackground(type: NotificationType): Color {
    return when (type) {
        NotificationType.GENERAL -> BalkanEstateGray
        NotificationType.PRICE_DROP -> BalkanEstateGreen
        NotificationType.NEW_LISTING -> BalkanEstatePrimaryBlue
        NotificationType.MESSAGE -> BalkanEstateOrange
        NotificationType.SAVED_SEARCH_MATCH -> BalkanEstateBlue
        NotificationType.PROPERTY_UPDATE -> BalkanEstateGreen
        NotificationType.SYSTEM -> BalkanEstateGray
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60_000 -> "Just now"
        diff < 3_600_000 -> "${diff / 60_000}m ago"
        diff < 86_400_000 -> "${diff / 3_600_000}h ago"
        diff < 604_800_000 -> "${diff / 86_400_000}d ago"
        else -> {
            val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
            sdf.format(Date(timestamp))
        }
    }
}
