package com.zanoapps.notification.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.BellIcon
import com.zanoapps.core.presentation.designsystem.HeartFilledIcon
import com.zanoapps.core.presentation.designsystem.HomeIcon
import com.zanoapps.core.presentation.designsystem.MailIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.Poppins

enum class NotificationType {
    NEW_LISTING,
    PRICE_DROP,
    MESSAGE,
    VIEWING_REMINDER,
    SAVED_SEARCH_MATCH,
    SYSTEM
}

data class NotificationItem(
    val id: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean,
    val propertyId: String? = null,
    val conversationId: String? = null
)

@Composable
fun NotificationsScreenRoot(
    notifications: List<NotificationItem>,
    onBackClick: () -> Unit,
    onNotificationClick: (NotificationItem) -> Unit,
    onDeleteNotification: (NotificationItem) -> Unit,
    onMarkAllAsRead: () -> Unit,
    modifier: Modifier = Modifier
) {
    NotificationsScreen(
        notifications = notifications,
        onBackClick = onBackClick,
        onNotificationClick = onNotificationClick,
        onDeleteNotification = onDeleteNotification,
        onMarkAllAsRead = onMarkAllAsRead,
        modifier = modifier
    )
}

@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem>,
    onBackClick: () -> Unit,
    onNotificationClick: (NotificationItem) -> Unit,
    onDeleteNotification: (NotificationItem) -> Unit,
    onMarkAllAsRead: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.DarkGray
                            )
                        }
                        Text(
                            text = "Notifications",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                            fontFamily = Poppins
                        )
                    }

                    if (notifications.any { !it.isRead }) {
                        Text(
                            text = "Mark all as read",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = BalkanEstatePrimaryBlue,
                            modifier = Modifier
                                .clickable { onMarkAllAsRead() }
                                .padding(8.dp)
                        )
                    }
                }
            }

            if (notifications.isEmpty()) {
                // Empty State
                EmptyNotificationsContent()
            } else {
                // Notifications List
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Today's notifications
                    val todayNotifications = notifications.filter { it.timestamp.contains("ago") }
                    if (todayNotifications.isNotEmpty()) {
                        item {
                            Text(
                                text = "Today",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                            )
                        }
                        items(todayNotifications) { notification ->
                            NotificationCard(
                                notification = notification,
                                onClick = { onNotificationClick(notification) },
                                onDelete = { onDeleteNotification(notification) }
                            )
                        }
                    }

                    // Earlier notifications
                    val earlierNotifications = notifications.filter { !it.timestamp.contains("ago") }
                    if (earlierNotifications.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Earlier",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                            )
                        }
                        items(earlierNotifications) { notification ->
                            NotificationCard(
                                notification = notification,
                                onClick = { onNotificationClick(notification) },
                                onDelete = { onDeleteNotification(notification) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(
    notification: NotificationItem,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) Color.White else Color(0xFFF0F7FF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    .background(getNotificationColor(notification.type).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getNotificationIcon(notification.type),
                    contentDescription = null,
                    tint = getNotificationColor(notification.type),
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 15.sp,
                        fontWeight = if (notification.isRead) FontWeight.Medium else FontWeight.SemiBold,
                        color = Color.DarkGray,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = notification.timestamp,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.message,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                // Unread indicator
                if (!notification.isRead) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(BalkanEstatePrimaryBlue)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Delete button
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyNotificationsContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = BellIcon,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No notifications yet",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "We'll notify you when there's something new for you",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

private fun getNotificationIcon(type: NotificationType): ImageVector {
    return when (type) {
        NotificationType.NEW_LISTING -> HomeIcon
        NotificationType.PRICE_DROP -> HomeIcon
        NotificationType.MESSAGE -> MailIcon
        NotificationType.VIEWING_REMINDER -> BellIcon
        NotificationType.SAVED_SEARCH_MATCH -> HeartFilledIcon
        NotificationType.SYSTEM -> BellIcon
    }
}

private fun getNotificationColor(type: NotificationType): Color {
    return when (type) {
        NotificationType.NEW_LISTING -> BalkanEstatePrimaryBlue
        NotificationType.PRICE_DROP -> BalkanEstateGreen
        NotificationType.MESSAGE -> BalkanEstateOrange
        NotificationType.VIEWING_REMINDER -> BalkanEstatePrimaryBlue
        NotificationType.SAVED_SEARCH_MATCH -> BalkanEstateRed
        NotificationType.SYSTEM -> Color.Gray
    }
}

// Mock data
object NotificationsMockData {
    val mockNotifications = listOf(
        NotificationItem(
            id = "1",
            type = NotificationType.NEW_LISTING,
            title = "New Property Listed",
            message = "A new apartment matching your search criteria is now available in Tirana Center.",
            timestamp = "2 min ago",
            isRead = false,
            propertyId = "1"
        ),
        NotificationItem(
            id = "2",
            type = NotificationType.MESSAGE,
            title = "New Message from John Doe",
            message = "Hi! I wanted to follow up on your interest in the villa...",
            timestamp = "1 hour ago",
            isRead = false,
            conversationId = "conv_1"
        ),
        NotificationItem(
            id = "3",
            type = NotificationType.PRICE_DROP,
            title = "Price Drop Alert",
            message = "The price for 'Luxury Villa with Sea View' has dropped by €20,000!",
            timestamp = "3 hours ago",
            isRead = true,
            propertyId = "2"
        ),
        NotificationItem(
            id = "4",
            type = NotificationType.VIEWING_REMINDER,
            title = "Viewing Reminder",
            message = "Don't forget your property viewing tomorrow at 2:00 PM.",
            timestamp = "Yesterday",
            isRead = true
        ),
        NotificationItem(
            id = "5",
            type = NotificationType.SAVED_SEARCH_MATCH,
            title = "5 New Matches",
            message = "5 new properties match your saved search 'Belgrade Houses'.",
            timestamp = "2 days ago",
            isRead = true
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun NotificationsScreenPreview() {
    BalkanEstateTheme {
        NotificationsScreen(
            notifications = NotificationsMockData.mockNotifications,
            onBackClick = {},
            onNotificationClick = {},
            onDeleteNotification = {},
            onMarkAllAsRead = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationsScreenEmptyPreview() {
    BalkanEstateTheme {
        NotificationsScreen(
            notifications = emptyList(),
            onBackClick = {},
            onNotificationClick = {},
            onDeleteNotification = {},
            onMarkAllAsRead = {}
        )
    }
}
