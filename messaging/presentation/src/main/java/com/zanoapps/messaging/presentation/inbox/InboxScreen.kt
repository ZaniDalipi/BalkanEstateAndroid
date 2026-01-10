package com.zanoapps.messaging.presentation.inbox

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.InboxIcon
import org.koin.androidx.compose.koinViewModel

@Composable
fun InboxScreenRoot(
    viewModel: InboxViewModel = koinViewModel(),
    onNavigateToChat: (String) -> Unit
) {
    InboxScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is InboxAction.OnConversationClick -> onNavigateToChat(action.conversation.id)
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InboxScreen(
    state: InboxState,
    onAction: (InboxAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        InboxHeader(
            unreadCount = state.conversations.sumOf { it.unreadCount }
        )

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
                }
            }
            state.isEmpty -> {
                EmptyInboxContent()
            }
            else -> {
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = { onAction(InboxAction.RefreshConversations) },
                    modifier = Modifier.fillMaxSize()
                ) {
                    ConversationsList(
                        conversations = state.conversations,
                        onConversationClick = { onAction(InboxAction.OnConversationClick(it)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun InboxHeader(unreadCount: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Messages",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = BalkanEstatePrimaryBlue
            )

            if (unreadCount > 0) {
                Badge(
                    containerColor = BalkanEstatePrimaryBlue,
                    contentColor = Color.White
                ) {
                    Text(text = "$unreadCount unread")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Connect with agents about your property interests",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
private fun EmptyInboxContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = InboxIcon,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = BalkanEstatePrimaryBlue.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No Messages Yet",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Start a conversation with an agent\nabout a property you're interested in.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun ConversationsList(
    conversations: List<Conversation>,
    onConversationClick: (Conversation) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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

@Composable
private fun ConversationCard(
    conversation: Conversation,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (conversation.unreadCount > 0)
                BalkanEstatePrimaryBlue.copy(alpha = 0.05f) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Avatar with online indicator
            Box {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(BalkanEstatePrimaryBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = conversation.participantName.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                if (conversation.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(BalkanEstateGreen)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.participantName,
                        fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = conversation.lastMessageTime,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Property info
                conversation.propertyTitle?.let { title ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        conversation.propertyImageUrl?.let { imageUrl ->
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = title,
                            color = BalkanEstatePrimaryBlue,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.lastMessage,
                        color = if (conversation.unreadCount > 0) Color.DarkGray else Color.Gray,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = if (conversation.unreadCount > 0) FontWeight.Medium else FontWeight.Normal,
                        modifier = Modifier.weight(1f)
                    )

                    if (conversation.unreadCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Badge(
                            containerColor = BalkanEstatePrimaryBlue,
                            contentColor = Color.White
                        ) {
                            Text(text = conversation.unreadCount.toString())
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InboxScreenPreview() {
    BalkanEstateTheme {
        InboxScreen(
            state = InboxState(
                conversations = listOf(
                    Conversation(
                        id = "1",
                        participantName = "Marina Lleshi",
                        participantAvatarUrl = null,
                        lastMessage = "The property is still available",
                        lastMessageTime = "2 min ago",
                        unreadCount = 2,
                        propertyTitle = "Luxury Penthouse",
                        propertyImageUrl = null,
                        isOnline = true
                    )
                ),
                isLoading = false,
                isEmpty = false
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyInboxScreenPreview() {
    BalkanEstateTheme {
        InboxScreen(
            state = InboxState(
                conversations = emptyList(),
                isLoading = false,
                isEmpty = true
            ),
            onAction = {}
        )
    }
}
