package com.zanoapps.messaging.presentation.chat

import android.widget.Toast
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
import com.zanoapps.core.presentation.designsystem.HomeIcon
import com.zanoapps.messaging.domain.model.Conversation
import com.zanoapps.messaging.domain.model.Message
import com.zanoapps.messaging.presentation.R
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ChatScreenRoot(
    viewModel: ChatViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onNavigateToProperty: (String) -> Unit
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = true) {
        viewModel.events.collect { event ->
            when (event) {
                is ChatEvent.Error -> {
                    Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
                }
                ChatEvent.MessageSent -> {
                    // Scroll to bottom on new message
                }
                is ChatEvent.NavigateToProperty -> {
                    onNavigateToProperty(event.propertyId)
                }
            }
        }
    }

    // Scroll to bottom when new messages arrive
    LaunchedEffect(viewModel.state.messages.size) {
        if (viewModel.state.messages.isNotEmpty()) {
            listState.animateScrollToItem(viewModel.state.messages.size - 1)
        }
    }

    ChatScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                ChatAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreen(
    state: ChatState,
    onAction: (ChatAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    state.conversation?.let { conversation ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
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
                                            .size(40.dp)
                                            .clip(CircleShape)
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = conversation.participantName.take(2).uppercase(),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BalkanEstatePrimaryBlue
                                        )
                                    }
                                }

                                if (conversation.isOnline) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .size(12.dp)
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

                            Column {
                                Text(
                                    text = conversation.participantName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )
                                Text(
                                    text = if (conversation.isOnline) {
                                        stringResource(R.string.online)
                                    } else {
                                        stringResource(R.string.offline)
                                    },
                                    fontSize = 12.sp,
                                    color = if (conversation.isOnline) Color(0xFF22C55E) else Color.Gray
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(ChatAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    state.conversation?.propertyId?.let {
                        IconButton(onClick = { onAction(ChatAction.OnViewProperty) }) {
                            Icon(
                                imageVector = HomeIcon,
                                contentDescription = "View Property",
                                tint = BalkanEstatePrimaryBlue
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            MessageInput(
                message = state.currentMessage,
                onMessageChanged = { onAction(ChatAction.OnMessageChanged(it)) },
                onSendClick = { onAction(ChatAction.OnSendMessage) },
                isSending = state.isSending
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Property Card (if applicable)
                state.conversation?.propertyTitle?.let { propertyTitle ->
                    PropertyInfoCard(
                        propertyTitle = propertyTitle,
                        onClick = { onAction(ChatAction.OnViewProperty) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                // Messages
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.messages,
                        key = { it.id }
                    ) { message ->
                        MessageBubble(
                            message = message,
                            isCurrentUser = message.senderId == state.currentUserId
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PropertyInfoCard(
    propertyTitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = HomeIcon,
                    contentDescription = null,
                    tint = BalkanEstatePrimaryBlue,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.about_property),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = propertyTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = stringResource(R.string.view),
                fontSize = 14.sp,
                color = BalkanEstatePrimaryBlue,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun MessageBubble(
    message: Message,
    isCurrentUser: Boolean
) {
    val bubbleColor = if (isCurrentUser) BalkanEstatePrimaryBlue else Color.White
    val textColor = if (isCurrentUser) Color.White else Color.Black
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Column(
            horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isCurrentUser) 16.dp else 4.dp,
                            bottomEnd = if (isCurrentUser) 4.dp else 16.dp
                        )
                    )
                    .background(bubbleColor)
                    .padding(12.dp)
            ) {
                Text(
                    text = message.content,
                    color = textColor,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = message.timestamp.format(DateTimeFormatter.ofPattern("HH:mm")),
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun MessageInput(
    message: String,
    onMessageChanged: (String) -> Unit,
    onSendClick: () -> Unit,
    isSending: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = message,
            onValueChange = onMessageChanged,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                color = Color.Black
            ),
            cursorBrush = SolidColor(BalkanEstatePrimaryBlue),
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFF1F5F9))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            decorationBox = { innerTextField ->
                Box {
                    if (message.isEmpty()) {
                        Text(
                            text = stringResource(R.string.type_message),
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    if (message.isNotBlank() && !isSending) BalkanEstatePrimaryBlue
                    else Color.LightGray
                )
                .clickable(enabled = message.isNotBlank() && !isSending) { onSendClick() },
            contentAlignment = Alignment.Center
        ) {
            if (isSending) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatScreenPreview() {
    BalkanEstateTheme {
        ChatScreen(
            state = ChatState(
                conversation = Conversation(
                    id = "1",
                    participantId = "agent1",
                    participantName = "Sarah Johnson",
                    participantImageUrl = null,
                    propertyId = "prop1",
                    propertyTitle = "Modern Apartment",
                    lastMessage = null,
                    lastMessageTime = null,
                    isOnline = true
                ),
                messages = listOf(
                    Message(
                        id = "1",
                        conversationId = "1",
                        senderId = "current_user",
                        content = "Hi, I'm interested in this property",
                        timestamp = LocalDateTime.now().minusMinutes(10)
                    ),
                    Message(
                        id = "2",
                        conversationId = "1",
                        senderId = "agent1",
                        content = "Hello! The property is still available. Would you like to schedule a viewing?",
                        timestamp = LocalDateTime.now().minusMinutes(5)
                    )
                )
            ),
            onAction = {}
        )
    }
}
