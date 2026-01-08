package com.zanoapps.notification.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.notification.domain.model.Notification
import com.zanoapps.notification.domain.model.NotificationPriority
import com.zanoapps.notification.domain.model.NotificationType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Individual notification item component.
 */
@Composable
fun NotificationItem(
    notification: Notification,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onClick: () -> Unit,
    onSwipeToDelete: () -> Unit,
    onSwipeToToggleRead: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onSwipeToDelete()
                    true
                }
                SwipeToDismissBoxValue.StartToEnd -> {
                    onSwipeToToggleRead()
                    true
                }
                SwipeToDismissBoxValue.Settled -> false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            SwipeBackground(
                dismissDirection = dismissState.dismissDirection,
                isRead = notification.isRead
            )
        },
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true,
        modifier = modifier
    ) {
        NotificationItemContent(
            notification = notification,
            isSelected = isSelected,
            isSelectionMode = isSelectionMode,
            onClick = onClick
        )
    }
}

@Composable
private fun SwipeBackground(
    dismissDirection: SwipeToDismissBoxValue,
    isRead: Boolean
) {
    val color by animateColorAsState(
        targetValue = when (dismissDirection) {
            SwipeToDismissBoxValue.EndToStart -> BalkanEstateRed
            SwipeToDismissBoxValue.StartToEnd -> if (isRead) BalkanEstateOrange else BalkanEstateGreen
            else -> Color.Transparent
        },
        label = "swipeBackgroundColor"
    )

    val icon = when (dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
        SwipeToDismissBoxValue.StartToEnd -> if (isRead) Icons.Outlined.MailOutline else Icons.Default.Email
        else -> null
    }

    val scale by animateFloatAsState(
        targetValue = if (dismissDirection != SwipeToDismissBoxValue.Settled) 1f else 0.75f,
        label = "iconScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = when (dismissDirection) {
            SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
            else -> Alignment.CenterEnd
        }
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.scale(scale)
            )
        }
    }
}

@Composable
private fun NotificationItemContent(
    notification: Notification,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isSelected -> BalkanEstatePrimaryBlue.copy(alpha = 0.1f)
            !notification.isRead -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else -> MaterialTheme.colorScheme.surface
        },
        label = "itemBackgroundColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Selection checkbox
            if (isSelectionMode) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onClick() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = BalkanEstatePrimaryBlue
                    ),
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            // Icon or image
            NotificationIcon(notification = notification)

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
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = formatTimestamp(notification.createdAt),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Price drop badge
                if (notification.type == NotificationType.PRICE_DROP) {
                    notification.getPriceDropPercentage()?.let { percentage ->
                        Spacer(modifier = Modifier.height(8.dp))
                        PriceDropBadge(
                            percentage = percentage,
                            oldPrice = notification.oldPrice,
                            newPrice = notification.price,
                            currency = notification.currency
                        )
                    }
                }

                // Property info
                notification.propertyTitle?.let { propertyTitle ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = propertyTitle,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Action button
                notification.actionText?.let { actionText ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = actionText,
                        style = MaterialTheme.typography.labelMedium,
                        color = BalkanEstatePrimaryBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Unread indicator
            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(BalkanEstatePrimaryBlue)
                )
            }

            // Priority indicator
            if (notification.priority == NotificationPriority.HIGH ||
                notification.priority == NotificationPriority.URGENT) {
                Box(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            if (notification.priority == NotificationPriority.URGENT)
                                BalkanEstateRed
                            else
                                BalkanEstateOrange
                        )
                )
            }
        }
    }
}

@Composable
private fun NotificationIcon(notification: Notification) {
    val iconSize = 48.dp

    when {
        // Show property image if available
        notification.propertyImageUrl != null -> {
            AsyncImage(
                model = notification.propertyImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(iconSize)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
        // Show sender avatar for messages
        notification.senderAvatarUrl != null -> {
            AsyncImage(
                model = notification.senderAvatarUrl,
                contentDescription = notification.senderName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape)
            )
        }
        // Show type icon
        else -> {
            Box(
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape)
                    .background(notification.type.getIconBackground()),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = notification.type.getIcon(),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun PriceDropBadge(
    percentage: Int,
    oldPrice: Double?,
    newPrice: Double?,
    currency: String?
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(BalkanEstateGreen.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "-$percentage%",
            style = MaterialTheme.typography.labelMedium,
            color = BalkanEstateGreen,
            fontWeight = FontWeight.Bold
        )

        if (oldPrice != null && newPrice != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${formatPrice(oldPrice)} → ${formatPrice(newPrice)} ${currency ?: ""}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun NotificationType.getIcon(): ImageVector {
    return when (this) {
        NotificationType.PRICE_DROP -> Icons.Default.Star
        NotificationType.NEW_LISTING_MATCH -> Icons.Default.Search
        NotificationType.MESSAGE, NotificationType.AGENT_RESPONSE -> Icons.Default.Email
        NotificationType.VIEWING_SCHEDULED, NotificationType.VIEWING_REMINDER -> Icons.Default.Home
        NotificationType.PROPERTY_STATUS_CHANGE -> Icons.Default.Home
        NotificationType.PROPERTY_SAVED, NotificationType.FAVORITE_UPDATE -> Icons.Default.Star
        NotificationType.SAVED_SEARCH_ALERT -> Icons.Default.Search
        NotificationType.SYSTEM, NotificationType.PROMOTIONAL -> Icons.Default.Notifications
    }
}

private fun NotificationType.getIconBackground(): Color {
    return when (this) {
        NotificationType.PRICE_DROP -> BalkanEstateGreen
        NotificationType.NEW_LISTING_MATCH -> BalkanEstatePrimaryBlue
        NotificationType.MESSAGE, NotificationType.AGENT_RESPONSE -> BalkanEstateOrange
        NotificationType.VIEWING_SCHEDULED, NotificationType.VIEWING_REMINDER -> BalkanEstateRed
        NotificationType.PROPERTY_STATUS_CHANGE -> BalkanEstatePrimaryBlue
        NotificationType.PROPERTY_SAVED, NotificationType.FAVORITE_UPDATE -> BalkanEstateOrange
        NotificationType.SAVED_SEARCH_ALERT -> BalkanEstatePrimaryBlue
        NotificationType.SYSTEM -> Color.Gray
        NotificationType.PROMOTIONAL -> BalkanEstateOrange
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < TimeUnit.MINUTES.toMillis(1) -> "Just now"
        diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}m ago"
        diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)}h ago"
        diff < TimeUnit.DAYS.toMillis(7) -> "${TimeUnit.MILLISECONDS.toDays(diff)}d ago"
        else -> {
            val formatter = SimpleDateFormat("MMM d", Locale.getDefault())
            formatter.format(Date(timestamp))
        }
    }
}

private fun formatPrice(price: Double): String {
    return String.format(Locale.US, "%,.0f", price)
}
