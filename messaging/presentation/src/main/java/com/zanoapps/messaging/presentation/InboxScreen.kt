package com.zanoapps.messaging.presentation

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
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.MailIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.Poppins
import com.zanoapps.core.presentation.designsystem.VerifiedIcon

data class ConversationItem(
    val id: String,
    val participantName: String,
    val participantAvatar: String? = null,
    val isAgent: Boolean = false,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val propertyTitle: String? = null
)

@Composable
fun InboxScreenRoot(
    conversations: List<ConversationItem>,
    onConversationClick: (ConversationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    InboxScreen(
        conversations = conversations,
        onConversationClick = onConversationClick,
        modifier = modifier
    )
}

@Composable
fun InboxScreen(
    conversations: List<ConversationItem>,
    onConversationClick: (ConversationItem) -> Unit,
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "Inbox",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        fontFamily = Poppins
                    )
                    Text(
                        text = "${conversations.size} conversations",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            if (conversations.isEmpty()) {
                // Empty State
                EmptyInboxContent(
                    modifier = Modifier.weight(1f)
                )
            } else {
                // Conversations List
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = conversations,
                        key = { it.id }
                    ) { conversation ->
                        ConversationCard(
                            conversation = conversation,
                            onClick = { onConversationClick(conversation) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationCard(
    conversation: ConversationItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = PersonIcon,
                    contentDescription = null,
                    tint = BalkanEstatePrimaryBlue,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Text(
                            text = conversation.participantName,
                            fontSize = 16.sp,
                            fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.SemiBold,
                            color = Color.DarkGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (conversation.isAgent) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = VerifiedIcon,
                                contentDescription = "Agent",
                                tint = BalkanEstatePrimaryBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = conversation.timestamp,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                if (conversation.propertyTitle != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = conversation.propertyTitle,
                        fontSize = 12.sp,
                        color = BalkanEstatePrimaryBlue,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.lastMessage,
                        fontSize = 14.sp,
                        color = if (conversation.unreadCount > 0) Color.DarkGray else Color.Gray,
                        fontWeight = if (conversation.unreadCount > 0) FontWeight.Medium else FontWeight.Normal,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (conversation.unreadCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Badge(
                            containerColor = BalkanEstatePrimaryBlue,
                            contentColor = Color.White
                        ) {
                            Text(
                                text = conversation.unreadCount.toString(),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyInboxContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = MailIcon,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No messages yet",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Start a conversation by contacting an agent about a property you're interested in.",
            fontSize = 14.sp,
            color = Color.Gray,
            lineHeight = 20.sp
        )
    }
}

// Mock data
object InboxMockData {
    val mockConversations = listOf(
        ConversationItem(
            id = "1",
            participantName = "John Doe",
            isAgent = true,
            lastMessage = "Hello! I'm interested in the apartment. Is it still available?",
            timestamp = "10:30 AM",
            unreadCount = 2,
            propertyTitle = "Modern Apartment in Tirana Center"
        ),
        ConversationItem(
            id = "2",
            participantName = "Maria Smith",
            isAgent = true,
            lastMessage = "When would you like to schedule a viewing?",
            timestamp = "Yesterday",
            unreadCount = 0,
            propertyTitle = "Luxury Villa with Sea View"
        ),
        ConversationItem(
            id = "3",
            participantName = "Stefan Jovic",
            isAgent = true,
            lastMessage = "The property has just been renovated. I can show you the latest photos.",
            timestamp = "2 days ago",
            unreadCount = 0,
            propertyTitle = "Cozy Studio in Belgrade"
        ),
        ConversationItem(
            id = "4",
            participantName = "Elena Petrova",
            isAgent = true,
            lastMessage = "Thank you for your interest! Let me know if you have any questions.",
            timestamp = "1 week ago",
            unreadCount = 0,
            propertyTitle = "Family House in Sofia"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun InboxScreenPreview() {
    BalkanEstateTheme {
        InboxScreen(
            conversations = InboxMockData.mockConversations,
            onConversationClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InboxScreenEmptyPreview() {
    BalkanEstateTheme {
        InboxScreen(
            conversations = emptyList(),
            onConversationClick = {}
        )
    }
}
