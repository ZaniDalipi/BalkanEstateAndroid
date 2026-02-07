package com.zanoapps.profile.presentation

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.BellIcon
import com.zanoapps.core.presentation.designsystem.LockIcon
import com.zanoapps.core.presentation.designsystem.MailIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.Poppins

data class SettingsState(
    val pushNotificationsEnabled: Boolean = true,
    val emailNotificationsEnabled: Boolean = true,
    val newListingsAlerts: Boolean = true,
    val priceDropAlerts: Boolean = true,
    val messageNotifications: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val language: String = "English",
    val currency: String = "EUR"
)

@Composable
fun SettingsScreenRoot(
    state: SettingsState,
    onBackClick: () -> Unit,
    onAccountClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onTermsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onAboutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onTogglePushNotifications: (Boolean) -> Unit,
    onToggleEmailNotifications: (Boolean) -> Unit,
    onToggleNewListingsAlerts: (Boolean) -> Unit,
    onTogglePriceDropAlerts: (Boolean) -> Unit,
    onToggleMessageNotifications: (Boolean) -> Unit,
    onToggleDarkMode: (Boolean) -> Unit,
    onLanguageClick: () -> Unit,
    onCurrencyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsScreen(
        state = state,
        onBackClick = onBackClick,
        onAccountClick = onAccountClick,
        onPrivacyClick = onPrivacyClick,
        onTermsClick = onTermsClick,
        onHelpClick = onHelpClick,
        onAboutClick = onAboutClick,
        onDeleteAccountClick = onDeleteAccountClick,
        onTogglePushNotifications = onTogglePushNotifications,
        onToggleEmailNotifications = onToggleEmailNotifications,
        onToggleNewListingsAlerts = onToggleNewListingsAlerts,
        onTogglePriceDropAlerts = onTogglePriceDropAlerts,
        onToggleMessageNotifications = onToggleMessageNotifications,
        onToggleDarkMode = onToggleDarkMode,
        onLanguageClick = onLanguageClick,
        onCurrencyClick = onCurrencyClick,
        modifier = modifier
    )
}

@Composable
fun SettingsScreen(
    state: SettingsState,
    onBackClick: () -> Unit,
    onAccountClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onTermsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onAboutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onTogglePushNotifications: (Boolean) -> Unit,
    onToggleEmailNotifications: (Boolean) -> Unit,
    onToggleNewListingsAlerts: (Boolean) -> Unit,
    onTogglePriceDropAlerts: (Boolean) -> Unit,
    onToggleMessageNotifications: (Boolean) -> Unit,
    onToggleDarkMode: (Boolean) -> Unit,
    onLanguageClick: () -> Unit,
    onCurrencyClick: () -> Unit,
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
                        text = "Settings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        fontFamily = Poppins
                    )
                }
            }

            // Settings Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Account Section
                SettingsSection(title = "Account") {
                    SettingsItem(
                        icon = PersonIcon,
                        title = "Account Settings",
                        onClick = onAccountClick
                    )
                    SettingsItem(
                        icon = LockIcon,
                        title = "Privacy & Security",
                        onClick = onPrivacyClick
                    )
                }

                // Notifications Section
                SettingsSection(title = "Notifications") {
                    SettingsToggleItem(
                        title = "Push Notifications",
                        subtitle = "Receive push notifications",
                        isEnabled = state.pushNotificationsEnabled,
                        onToggle = onTogglePushNotifications
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    SettingsToggleItem(
                        title = "Email Notifications",
                        subtitle = "Receive updates via email",
                        isEnabled = state.emailNotificationsEnabled,
                        onToggle = onToggleEmailNotifications
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    SettingsToggleItem(
                        title = "New Listings Alerts",
                        subtitle = "Get notified about new properties",
                        isEnabled = state.newListingsAlerts,
                        onToggle = onToggleNewListingsAlerts
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    SettingsToggleItem(
                        title = "Price Drop Alerts",
                        subtitle = "Get notified when prices drop",
                        isEnabled = state.priceDropAlerts,
                        onToggle = onTogglePriceDropAlerts
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    SettingsToggleItem(
                        title = "Message Notifications",
                        subtitle = "Get notified about new messages",
                        isEnabled = state.messageNotifications,
                        onToggle = onToggleMessageNotifications
                    )
                }

                // Preferences Section
                SettingsSection(title = "Preferences") {
                    SettingsValueItem(
                        title = "Language",
                        value = state.language,
                        onClick = onLanguageClick
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    SettingsValueItem(
                        title = "Currency",
                        value = state.currency,
                        onClick = onCurrencyClick
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    SettingsToggleItem(
                        title = "Dark Mode",
                        subtitle = "Use dark theme",
                        isEnabled = state.darkModeEnabled,
                        onToggle = onToggleDarkMode
                    )
                }

                // Support Section
                SettingsSection(title = "Support") {
                    SettingsItem(
                        title = "Help Center",
                        onClick = onHelpClick
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    SettingsItem(
                        title = "Terms of Service",
                        onClick = onTermsClick
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    SettingsItem(
                        title = "About",
                        onClick = onAboutClick
                    )
                }

                // Danger Zone
                SettingsSection(title = "Danger Zone") {
                    SettingsItem(
                        title = "Delete Account",
                        titleColor = BalkanEstateRed,
                        onClick = onDeleteAccountClick
                    )
                }

                // App Version
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Balkan Estate v1.0.0",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    icon: ImageVector? = null,
    titleColor: Color = Color.DarkGray,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = BalkanEstatePrimaryBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            Text(
                text = title,
                fontSize = 16.sp,
                color = titleColor,
                fontWeight = FontWeight.Medium
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun SettingsToggleItem(
    title: String,
    subtitle: String? = null,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = BalkanEstatePrimaryBlue,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}

@Composable
private fun SettingsValueItem(
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.Medium
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    BalkanEstateTheme {
        SettingsScreen(
            state = SettingsState(),
            onBackClick = {},
            onAccountClick = {},
            onPrivacyClick = {},
            onTermsClick = {},
            onHelpClick = {},
            onAboutClick = {},
            onDeleteAccountClick = {},
            onTogglePushNotifications = {},
            onToggleEmailNotifications = {},
            onToggleNewListingsAlerts = {},
            onTogglePriceDropAlerts = {},
            onToggleMessageNotifications = {},
            onToggleDarkMode = {},
            onLanguageClick = {},
            onCurrencyClick = {}
        )
    }
}
