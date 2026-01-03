package com.zanoapps.messaging.presentation.conversations

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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.DeleteIcon
import com.zanoapps.core.presentation.designsystem.MessageIcon
import com.zanoapps.messaging.domain.model.Conversation
import com.zanoapps.messaging.presentation.R
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun ConversationsScreenRoot(
    viewModel: ConversationsViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onNavigateToChat: (String) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.events.collect { event ->
            when (event) {
                is ConversationsEvent.Error -> {
                    Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
                }
                is ConversationsEvent.NavigateToChat -> {
                    onNavigateToChat(event.conversationId)
                }
            }
        }
    }

    ConversationsScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                ConversationsAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConversationsScreen(
    state: ConversationsState,
    onAction: (ConversationsAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.messages),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(ConversationsAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        if (state.isLoading && state.conversations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
            }
        } else if (state.conversations.isEmpty()) {
            EmptyConversationsState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { onAction(ConversationsAction.OnRefresh) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = state.conversations,
                        key = { it.id }
                    ) { conversation ->
                        SwipeableConversationItem(
                            conversation = conversation,
                            onClick = { onAction(ConversationsAction.OnConversationClick(conversation.id)) },
                            onDelete = { onAction(ConversationsAction.OnDeleteConversation(conversation.id)) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableConversationItem(
    conversation: Conversation,
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
            ConversationItem(
                conversation = conversation,
                onClick = onClick
            )
        }
    )
}

@Composable
private fun ConversationItem(
    conversation: Conversation,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box {
                if (conversation.participantImageUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(conversation.participantImageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = conversation.participantName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = conversation.participantName.take(2).uppercase(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = BalkanEstatePrimaryBlue
                        )
                    }
                }

                // Online indicator
                if (conversation.isOnline) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color(0xFF22C55E))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.participantName,
                        fontSize = 16.sp,
                        fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.Medium,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    conversation.lastMessageTime?.let { time ->
                        Text(
                            text = formatTimestamp(time),
                            fontSize = 12.sp,
                            color = if (conversation.unreadCount > 0) BalkanEstatePrimaryBlue else Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                conversation.propertyTitle?.let { propertyTitle ->
                    Text(
                        text = propertyTitle,
                        fontSize = 13.sp,
                        color = BalkanEstatePrimaryBlue,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.lastMessage ?: stringResource(R.string.no_messages),
                        fontSize = 14.sp,
                        color = if (conversation.unreadCount > 0) Color.Black else Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = if (conversation.unreadCount > 0) FontWeight.Medium else FontWeight.Normal,
                        modifier = Modifier.weight(1f)
                    )

                    if (conversation.unreadCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(BalkanEstatePrimaryBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (conversation.unreadCount > 9) "9+" else conversation.unreadCount.toString(),
                                fontSize = 11.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
        HorizontalDivider(color = Color(0xFFE5E7EB))
    }
}

@Composable
private fun EmptyConversationsState(modifier: Modifier = Modifier) {
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
                imageVector = MessageIcon,
                contentDescription = null,
                tint = BalkanEstatePrimaryBlue,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.no_conversations),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.no_conversations_description),
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

private fun formatTimestamp(time: LocalDateTime): String {
    val now = LocalDateTime.now()
    val daysBetween = ChronoUnit.DAYS.between(time.toLocalDate(), now.toLocalDate())

    return when {
        daysBetween == 0L -> time.format(DateTimeFormatter.ofPattern("HH:mm"))
        daysBetween == 1L -> "Yesterday"
        daysBetween < 7L -> time.format(DateTimeFormatter.ofPattern("EEEE"))
        else -> time.format(DateTimeFormatter.ofPattern("dd/MM/yy"))
    }
}

@Preview(showBackground = true)
@Composable
private fun ConversationsScreenPreview() {
    BalkanEstateTheme {
        ConversationsScreen(
            state = ConversationsState(
                conversations = listOf(
                    Conversation(
                        id = "1",
                        participantId = "agent1",
                        participantName = "Sarah Johnson",
                        participantImageUrl = null,
                        propertyId = "prop1",
                        propertyTitle = "Modern Apartment",
                        lastMessage = "I can schedule a viewing for tomorrow",
                        lastMessageTime = LocalDateTime.now().minusMinutes(30),
                        unreadCount = 2,
                        isOnline = true
                    )
                )
            ),
            onAction = {}
        )
    }
}
