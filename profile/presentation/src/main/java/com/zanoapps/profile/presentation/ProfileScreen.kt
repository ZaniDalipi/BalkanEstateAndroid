package com.zanoapps.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.EditPenIcon
import com.zanoapps.core.presentation.designsystem.EmailIcon
import com.zanoapps.core.presentation.designsystem.HeartFilledIcon
import com.zanoapps.core.presentation.designsystem.InboxIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.LogoutIcon
import com.zanoapps.core.presentation.designsystem.NotificationBellIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.PhoneIcon
import com.zanoapps.core.presentation.designsystem.Poppins
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import com.zanoapps.core.presentation.designsystem.StarIcon
import com.zanoapps.core.presentation.designsystem.VerifiedIcon

data class ProfileState(
    val userName: String = "",
    val email: String = "",
    val phone: String = "",
    val location: String = "",
    val isVerified: Boolean = false,
    val savedPropertiesCount: Int = 0,
    val savedSearchesCount: Int = 0,
    val messagesCount: Int = 0,
    val notificationsEnabled: Boolean = true,
    val emailNotificationsEnabled: Boolean = true,
    val isLoggedIn: Boolean = false
)

@Composable
fun ProfileScreenRoot(
    state: ProfileState,
    onEditProfileClick: () -> Unit,
    onSavedPropertiesClick: () -> Unit,
    onSavedSearchesClick: () -> Unit,
    onMessagesClick: () -> Unit,
    onSubscriptionClick: () -> Unit,
    onNotificationsToggle: (Boolean) -> Unit,
    onEmailNotificationsToggle: (Boolean) -> Unit,
    onLogoutClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ProfileScreen(
        state = state,
        onEditProfileClick = onEditProfileClick,
        onSavedPropertiesClick = onSavedPropertiesClick,
        onSavedSearchesClick = onSavedSearchesClick,
        onMessagesClick = onMessagesClick,
        onSubscriptionClick = onSubscriptionClick,
        onNotificationsToggle = onNotificationsToggle,
        onEmailNotificationsToggle = onEmailNotificationsToggle,
        onLogoutClick = onLogoutClick,
        onLoginClick = onLoginClick,
        modifier = modifier
    )
}

@Composable
fun ProfileScreen(
    state: ProfileState,
    onEditProfileClick: () -> Unit,
    onSavedPropertiesClick: () -> Unit,
    onSavedSearchesClick: () -> Unit,
    onMessagesClick: () -> Unit,
    onSubscriptionClick: () -> Unit,
    onNotificationsToggle: (Boolean) -> Unit,
    onEmailNotificationsToggle: (Boolean) -> Unit,
    onLogoutClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (state.isLoggedIn) {
                // Profile Header
                ProfileHeader(
                    userName = state.userName,
                    email = state.email,
                    isVerified = state.isVerified,
                    onEditClick = onEditProfileClick
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Quick Stats
                QuickStatsSection(
                    savedPropertiesCount = state.savedPropertiesCount,
                    savedSearchesCount = state.savedSearchesCount,
                    messagesCount = state.messagesCount,
                    onSavedPropertiesClick = onSavedPropertiesClick,
                    onSavedSearchesClick = onSavedSearchesClick,
                    onMessagesClick = onMessagesClick
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Account Details
                AccountDetailsSection(
                    email = state.email,
                    phone = state.phone,
                    location = state.location
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Subscription
                SubscriptionSection(
                    onSubscriptionClick = onSubscriptionClick
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Settings
                SettingsSection(
                    notificationsEnabled = state.notificationsEnabled,
                    emailNotificationsEnabled = state.emailNotificationsEnabled,
                    onNotificationsToggle = onNotificationsToggle,
                    onEmailNotificationsToggle = onEmailNotificationsToggle
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Logout Button
                OutlinedButton(
                    onClick = onLogoutClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = BalkanEstateRed
                    )
                ) {
                    Icon(
                        imageVector = LogoutIcon,
                        contentDescription = null,
                        tint = BalkanEstateRed
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Logout",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            } else {
                // Not logged in state
                NotLoggedInContent(onLoginClick = onLoginClick)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ProfileHeader(
    userName: String,
    email: String,
    isVerified: Boolean,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(BalkanEstatePrimaryBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.take(1).uppercase(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name with verified badge
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = userName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    fontFamily = Poppins
                )
                if (isVerified) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = VerifiedIcon,
                        contentDescription = "Verified",
                        tint = BalkanEstatePrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = email,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Profile Button
            OutlinedButton(
                onClick = onEditClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = BalkanEstatePrimaryBlue
                )
            ) {
                Icon(
                    imageVector = EditPenIcon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit Profile",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun QuickStatsSection(
    savedPropertiesCount: Int,
    savedSearchesCount: Int,
    messagesCount: Int,
    onSavedPropertiesClick: () -> Unit,
    onSavedSearchesClick: () -> Unit,
    onMessagesClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            icon = HeartFilledIcon,
            count = savedPropertiesCount,
            label = "Saved",
            onClick = onSavedPropertiesClick,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = SaveSearchIcon,
            count = savedSearchesCount,
            label = "Searches",
            onClick = onSavedSearchesClick,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = InboxIcon,
            count = messagesCount,
            label = "Messages",
            onClick = onMessagesClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    icon: ImageVector,
    count: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BalkanEstatePrimaryBlue,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = count.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun AccountDetailsSection(
    email: String,
    phone: String,
    location: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Account Details",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            DetailRow(icon = EmailIcon, label = "Email", value = email)
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))
            DetailRow(icon = PhoneIcon, label = "Phone", value = phone.ifEmpty { "Not set" })
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))
            DetailRow(icon = LocationIcon, label = "Location", value = location.ifEmpty { "Not set" })
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun SubscriptionSection(
    onSubscriptionClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSubscriptionClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BalkanEstatePrimaryBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = StarIcon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Upgrade to Pro",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Get premium features and more visibility",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    notificationsEnabled: Boolean,
    emailNotificationsEnabled: Boolean,
    onNotificationsToggle: (Boolean) -> Unit,
    onEmailNotificationsToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Settings",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingToggleRow(
                icon = NotificationBellIcon,
                label = "Push Notifications",
                description = "Receive alerts about new properties",
                isEnabled = notificationsEnabled,
                onToggle = onNotificationsToggle
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))

            SettingToggleRow(
                icon = EmailIcon,
                label = "Email Notifications",
                description = "Receive updates via email",
                isEnabled = emailNotificationsEnabled,
                onToggle = onEmailNotificationsToggle
            )
        }
    }
}

@Composable
private fun SettingToggleRow(
    icon: ImageVector,
    label: String,
    description: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = BalkanEstatePrimaryBlue
            )
        )
    }
}

@Composable
private fun NotLoggedInContent(
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = PersonIcon,
            contentDescription = null,
            tint = BalkanEstatePrimaryBlue,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Welcome to BalkanEstate",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            fontFamily = Poppins
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Sign in to save properties, manage your searches, and access personalized recommendations.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BalkanEstatePrimaryBlue
            )
        ) {
            Text(
                text = "Sign In",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenLoggedInPreview() {
    BalkanEstateTheme {
        ProfileScreen(
            state = ProfileState(
                userName = "John Doe",
                email = "john.doe@example.com",
                phone = "+1 234 567 8900",
                location = "Tirana, Albania",
                isVerified = true,
                savedPropertiesCount = 12,
                savedSearchesCount = 5,
                messagesCount = 3,
                notificationsEnabled = true,
                emailNotificationsEnabled = false,
                isLoggedIn = true
            ),
            onEditProfileClick = {},
            onSavedPropertiesClick = {},
            onSavedSearchesClick = {},
            onMessagesClick = {},
            onSubscriptionClick = {},
            onNotificationsToggle = {},
            onEmailNotificationsToggle = {},
            onLogoutClick = {},
            onLoginClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenLoggedOutPreview() {
    BalkanEstateTheme {
        ProfileScreen(
            state = ProfileState(isLoggedIn = false),
            onEditProfileClick = {},
            onSavedPropertiesClick = {},
            onSavedSearchesClick = {},
            onMessagesClick = {},
            onSubscriptionClick = {},
            onNotificationsToggle = {},
            onEmailNotificationsToggle = {},
            onLogoutClick = {},
            onLoginClick = {}
        )
    }
}
