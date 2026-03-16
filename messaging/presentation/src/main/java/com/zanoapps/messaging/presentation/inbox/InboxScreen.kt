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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BackIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.EmailIcon
import com.zanoapps.core.presentation.designsystem.InboxIcon
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import com.zanoapps.messaging.domain.model.Conversation
import com.zanoapps.messaging.domain.model.Message
import androidx.compose.ui.tooling.preview.Preview
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun InboxScreenRoot(
    viewModel: InboxViewModel = koinViewModel()
) {
    InboxScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun InboxScreen(
    state: InboxState,
    onAction: (InboxAction) -> Unit
) {
    if (state.selectedConversation != null) {
        ConversationDetailScreen(
            conversation = state.selectedConversation,
            messages = state.messages,
            newMessage = state.newMessage,
            isLoadingMessages = state.isLoadingMessages,
            isSendingMessage = state.isSendingMessage,
            onAction = onAction
        )
    } else {
        ConversationListScreen(state = state, onAction = onAction)
    }
}

@Composable
private fun ConversationListScreen(
    state: InboxState,
    onAction: (InboxAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "Inbox",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Search
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { onAction(InboxAction.OnSearchQueryChanged(it)) },
                placeholder = { Text("Search conversations...") },
                leadingIcon = {
                    Icon(
                        imageVector = SaveSearchIcon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BalkanEstatePrimaryBlue,
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tab filters
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InboxTab.entries.forEach { tab ->
                    FilterChip(
                        selected = state.selectedTab == tab,
                        onClick = { onAction(InboxAction.OnTabChanged(tab)) },
                        label = { Text(tab.displayName, fontSize = 13.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BalkanEstatePrimaryBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
            }
        } else if (state.filteredConversations.isEmpty()) {
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
                    modifier = Modifier.size(64.dp),
                    tint = BalkanEstatePrimaryBlue
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No Messages",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Start a conversation by contacting\nan agent on a property listing.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(
                    items = state.filteredConversations,
                    key = { it.id }
                ) { conversation ->
                    ConversationItem(
                        conversation = conversation,
                        onClick = { onAction(InboxAction.OnConversationClick(conversation)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ConversationItem(
    conversation: Conversation,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (conversation.unreadCount > 0) BalkanEstatePrimaryBlue.copy(alpha = 0.03f)
            else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(BalkanEstatePrimaryBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = conversation.agentName.take(1),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                if (conversation.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(BalkanEstateGreen)
                            .align(Alignment.BottomEnd)
                    )
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
                        text = conversation.agentName,
                        fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 15.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = conversation.lastMessageTime,
                        fontSize = 12.sp,
                        color = BalkanEstateGray
                    )
                }
                if (conversation.propertyTitle.isNotBlank()) {
                    Text(
                        text = conversation.propertyTitle,
                        fontSize = 12.sp,
                        color = BalkanEstatePrimaryBlue,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.lastMessage,
                        fontSize = 13.sp,
                        color = if (conversation.unreadCount > 0) Color.DarkGray else BalkanEstateGray,
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
                            Text(conversation.unreadCount.toString())
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationDetailScreen(
    conversation: Conversation,
    messages: List<Message>,
    newMessage: String,
    isLoadingMessages: Boolean,
    isSendingMessage: Boolean,
    onAction: (InboxAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onAction(InboxAction.OnBackFromConversation) }) {
                Icon(
                    imageVector = BackIcon,
                    contentDescription = "Back",
                    modifier = Modifier.size(20.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BalkanEstatePrimaryBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = conversation.agentName.take(1),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = conversation.agentName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                if (conversation.isOnline) {
                    Text(
                        text = "Online",
                        fontSize = 12.sp,
                        color = BalkanEstateGreen
                    )
                }
            }
        }

        // Property reference
        if (conversation.propertyTitle.isNotBlank()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BalkanEstatePrimaryBlue.copy(alpha = 0.05f))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Re: ${conversation.propertyTitle}",
                    fontSize = 13.sp,
                    color = BalkanEstatePrimaryBlue,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Messages
        if (isLoadingMessages) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
            }
        } else {
            val listState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = listState,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = messages,
                    key = { it.id }
                ) { message ->
                    MessageBubble(message = message)
                }
            }
        }

        // Message input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newMessage,
                onValueChange = { onAction(InboxAction.OnNewMessageChanged(it)) },
                placeholder = { Text("Type a message...") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BalkanEstatePrimaryBlue,
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { onAction(InboxAction.OnSendMessage) },
                enabled = newMessage.isNotBlank() && !isSendingMessage
            ) {
                Icon(
                    imageVector = EmailIcon,
                    contentDescription = "Send",
                    tint = if (newMessage.isNotBlank()) BalkanEstatePrimaryBlue else BalkanEstateGray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun MessageBubble(message: Message) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isFromUser) 16.dp else 4.dp,
                        bottomEnd = if (message.isFromUser) 4.dp else 16.dp
                    )
                )
                .background(
                    if (message.isFromUser) BalkanEstatePrimaryBlue
                    else Color(0xFFE8EDF2)
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .fillMaxWidth(0.75f)
        ) {
            Column {
                Text(
                    text = message.content,
                    color = if (message.isFromUser) Color.White else Color.DarkGray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = message.timestamp,
                    color = if (message.isFromUser) Color.White.copy(alpha = 0.7f) else BalkanEstateGray,
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Preview
@Composable
private fun InboxScreenPreview() {
    BalkanEstateTheme {
        InboxScreen(
            state = InboxState(),
            onAction = {}
        )
    }
}
