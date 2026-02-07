package com.zanoapps.messaging.presentation

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.Poppins
import com.zanoapps.core.presentation.designsystem.VerifiedIcon

data class ChatMessage(
    val id: String,
    val content: String,
    val timestamp: String,
    val isFromCurrentUser: Boolean,
    val isRead: Boolean = false
)

@Composable
fun ChatScreenRoot(
    participantName: String,
    isAgent: Boolean,
    propertyTitle: String?,
    messages: List<ChatMessage>,
    onBackClick: () -> Unit,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ChatScreen(
        participantName = participantName,
        isAgent = isAgent,
        propertyTitle = propertyTitle,
        messages = messages,
        onBackClick = onBackClick,
        onSendMessage = onSendMessage,
        modifier = modifier
    )
}

@Composable
fun ChatScreen(
    participantName: String,
    isAgent: Boolean,
    propertyTitle: String?,
    messages: List<ChatMessage>,
    onBackClick: () -> Unit,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            ChatHeader(
                participantName = participantName,
                isAgent = isAgent,
                propertyTitle = propertyTitle,
                onBackClick = onBackClick
            )

            // Messages
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                reverseLayout = true
            ) {
                items(
                    items = messages.reversed(),
                    key = { it.id }
                ) { message ->
                    MessageBubble(
                        message = message,
                        participantName = participantName
                    )
                }
            }

            // Input Field
            MessageInput(
                value = messageText,
                onValueChange = { messageText = it },
                onSend = {
                    if (messageText.isNotBlank()) {
                        onSendMessage(messageText)
                        messageText = ""
                    }
                }
            )
        }
    }
}

@Composable
private fun ChatHeader(
    participantName: String,
    isAgent: Boolean,
    propertyTitle: String?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.DarkGray
                )
            }

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = PersonIcon,
                    contentDescription = null,
                    tint = BalkanEstatePrimaryBlue,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = participantName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray,
                        fontFamily = Poppins
                    )
                    if (isAgent) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = VerifiedIcon,
                            contentDescription = "Agent",
                            tint = BalkanEstatePrimaryBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                if (propertyTitle != null) {
                    Text(
                        text = propertyTitle,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: ChatMessage,
    participantName: String,
    modifier: Modifier = Modifier
) {
    val isFromCurrentUser = message.isFromCurrentUser

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (isFromCurrentUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (isFromCurrentUser) BalkanEstatePrimaryBlue else Color.White,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isFromCurrentUser) 16.dp else 4.dp,
                        bottomEnd = if (isFromCurrentUser) 4.dp else 16.dp
                    )
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.content,
                fontSize = 14.sp,
                color = if (isFromCurrentUser) Color.White else Color.DarkGray,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = message.timestamp,
            fontSize = 10.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
private fun MessageInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = "Type a message...",
                        color = Color.Gray
                    )
                },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BalkanEstatePrimaryBlue,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5)
                ),
                maxLines = 4
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onSend,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (value.isNotBlank()) BalkanEstatePrimaryBlue else Color.LightGray,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}

// Mock data for preview
object ChatMockData {
    val mockMessages = listOf(
        ChatMessage(
            id = "1",
            content = "Hello! I'm interested in the apartment in Tirana.",
            timestamp = "10:30 AM",
            isFromCurrentUser = true,
            isRead = true
        ),
        ChatMessage(
            id = "2",
            content = "Hi! Thank you for your interest. The apartment is still available. Would you like to schedule a viewing?",
            timestamp = "10:35 AM",
            isFromCurrentUser = false,
            isRead = true
        ),
        ChatMessage(
            id = "3",
            content = "Yes, that would be great! When is a good time?",
            timestamp = "10:40 AM",
            isFromCurrentUser = true,
            isRead = true
        ),
        ChatMessage(
            id = "4",
            content = "I have availability tomorrow afternoon between 2-5 PM, or Saturday morning. Which works better for you?",
            timestamp = "10:45 AM",
            isFromCurrentUser = false,
            isRead = false
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun ChatScreenPreview() {
    BalkanEstateTheme {
        ChatScreen(
            participantName = "John Doe",
            isAgent = true,
            propertyTitle = "Modern Apartment in Tirana Center",
            messages = ChatMockData.mockMessages,
            onBackClick = {},
            onSendMessage = {}
        )
    }
}
