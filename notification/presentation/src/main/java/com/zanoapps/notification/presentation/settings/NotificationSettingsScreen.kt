package com.zanoapps.notification.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BackIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme

@Composable
fun NotificationSettingsScreenRoot(
    onNavigateBack: () -> Unit
) {
    NotificationSettingsScreen(
        onBackClick = onNavigateBack
    )
}

@Composable
private fun NotificationSettingsScreen(
    onBackClick: () -> Unit
) {
    var pushNotifications by remember { mutableStateOf(true) }
    var newListings by remember { mutableStateOf(true) }
    var priceDrops by remember { mutableStateOf(true) }
    var newMessages by remember { mutableStateOf(true) }
    var propertyUpdates by remember { mutableStateOf(true) }
    var marketing by remember { mutableStateOf(false) }
    var quietHoursEnabled by remember { mutableStateOf(false) }
    var quietHoursStart by remember { mutableStateOf("22:00") }
    var quietHoursEnd by remember { mutableStateOf("07:00") }
    var weeklyDigest by remember { mutableStateOf(true) }
    var marketReports by remember { mutableStateOf(false) }

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
            IconButton(onClick = onBackClick) {
                Icon(BackIcon, "Back", Modifier.size(20.dp))
            }
            Text(
                text = "Notification Settings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Push Notifications Section
            Text(
                text = "Push Notifications",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    NotificationToggleItem(
                        title = "Push Notifications",
                        subtitle = "Master toggle for all push notifications",
                        checked = pushNotifications,
                        onCheckedChange = { pushNotifications = it }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    NotificationToggleItem(
                        title = "New Listings",
                        subtitle = "Matching your saved searches",
                        checked = newListings && pushNotifications,
                        enabled = pushNotifications,
                        onCheckedChange = { newListings = it }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    NotificationToggleItem(
                        title = "Price Drops",
                        subtitle = "On saved properties",
                        checked = priceDrops && pushNotifications,
                        enabled = pushNotifications,
                        onCheckedChange = { priceDrops = it }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    NotificationToggleItem(
                        title = "New Messages",
                        subtitle = "From agents",
                        checked = newMessages && pushNotifications,
                        enabled = pushNotifications,
                        onCheckedChange = { newMessages = it }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    NotificationToggleItem(
                        title = "Property Updates",
                        subtitle = "Status changes on properties you follow",
                        checked = propertyUpdates && pushNotifications,
                        enabled = pushNotifications,
                        onCheckedChange = { propertyUpdates = it }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    NotificationToggleItem(
                        title = "Marketing",
                        subtitle = "Offers and promotions",
                        checked = marketing && pushNotifications,
                        enabled = pushNotifications,
                        onCheckedChange = { marketing = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quiet Hours Section
            Text(
                text = "Quiet Hours",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    NotificationToggleItem(
                        title = "Enable Quiet Hours",
                        subtitle = "Mute notifications during set hours",
                        checked = quietHoursEnabled,
                        onCheckedChange = { quietHoursEnabled = it }
                    )
                    if (quietHoursEnabled) {
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Start Time",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = quietHoursStart,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BalkanEstatePrimaryBlue,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "End Time",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = quietHoursEnd,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BalkanEstatePrimaryBlue,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Email Notifications Section
            Text(
                text = "Email Notifications",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    NotificationToggleItem(
                        title = "Weekly Property Digest",
                        subtitle = "Summary of new listings matching your criteria",
                        checked = weeklyDigest,
                        onCheckedChange = { weeklyDigest = it }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    NotificationToggleItem(
                        title = "Market Reports",
                        subtitle = "Monthly market analysis and trends",
                        checked = marketReports,
                        onCheckedChange = { marketReports = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save Button
            Button(
                onClick = { onBackClick() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Save Settings",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun NotificationToggleItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = if (enabled) Color.DarkGray else BalkanEstateGray
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = BalkanEstateGray
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = BalkanEstatePrimaryBlue,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE2E8F0)
            )
        )
    }
}

@Preview
@Composable
private fun NotificationSettingsScreenPreview() {
    BalkanEstateTheme {
        NotificationSettingsScreen(
            onBackClick = {}
        )
    }
}
