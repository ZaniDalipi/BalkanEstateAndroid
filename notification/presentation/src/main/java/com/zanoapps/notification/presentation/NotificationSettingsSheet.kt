package com.zanoapps.notification.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.notification.domain.model.NotificationPreferences

/**
 * Settings bottom sheet for notification preferences.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsSheet(
    preferences: NotificationPreferences,
    isSaving: Boolean,
    onAction: (NotificationAction) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var email by remember { mutableStateOf(preferences.notificationEmail ?: "") }
    var priceDropPercentage by remember { mutableFloatStateOf(preferences.minPriceDropPercentage.toFloat()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notification Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Master toggle
            SettingsSection(title = "General") {
                SettingsSwitch(
                    title = "Push Notifications",
                    subtitle = "Receive push notifications on this device",
                    checked = preferences.pushNotificationsEnabled,
                    onCheckedChange = { onAction(NotificationAction.OnTogglePushNotifications(it)) }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Notification types
            SettingsSection(title = "Notification Types") {
                SettingsSwitch(
                    title = "Price Drops",
                    subtitle = "Get notified when saved properties reduce their price",
                    checked = preferences.priceDropsEnabled,
                    enabled = preferences.pushNotificationsEnabled,
                    onCheckedChange = { onAction(NotificationAction.OnTogglePriceDrops(it)) }
                )

                SettingsSwitch(
                    title = "New Listings",
                    subtitle = "Get notified when new properties match your saved searches",
                    checked = preferences.newListingsEnabled,
                    enabled = preferences.pushNotificationsEnabled,
                    onCheckedChange = { onAction(NotificationAction.OnToggleNewListings(it)) }
                )

                SettingsSwitch(
                    title = "Messages",
                    subtitle = "Get notified when agents reply to your inquiries",
                    checked = preferences.messagesEnabled,
                    enabled = preferences.pushNotificationsEnabled,
                    onCheckedChange = { onAction(NotificationAction.OnToggleMessages(it)) }
                )

                SettingsSwitch(
                    title = "Viewing Reminders",
                    subtitle = "Get reminded about upcoming property viewings",
                    checked = preferences.viewingRemindersEnabled,
                    enabled = preferences.pushNotificationsEnabled,
                    onCheckedChange = { onAction(NotificationAction.OnToggleViewingReminders(it)) }
                )

                SettingsSwitch(
                    title = "Property Status Changes",
                    subtitle = "Get notified when saved properties change status",
                    checked = preferences.propertyStatusEnabled,
                    enabled = preferences.pushNotificationsEnabled,
                    onCheckedChange = { onAction(NotificationAction.OnTogglePropertyStatus(it)) }
                )

                SettingsSwitch(
                    title = "Saved Search Alerts",
                    subtitle = "Weekly summaries of new properties matching your searches",
                    checked = preferences.savedSearchAlertsEnabled,
                    enabled = preferences.pushNotificationsEnabled,
                    onCheckedChange = { onAction(NotificationAction.OnToggleSavedSearchAlerts(it)) }
                )

                SettingsSwitch(
                    title = "Promotional",
                    subtitle = "Special offers and marketing updates",
                    checked = preferences.promotionalEnabled,
                    enabled = preferences.pushNotificationsEnabled,
                    onCheckedChange = { onAction(NotificationAction.OnTogglePromotional(it)) }
                )

                SettingsSwitch(
                    title = "System Announcements",
                    subtitle = "Important updates about the app",
                    checked = preferences.systemAnnouncementsEnabled,
                    enabled = preferences.pushNotificationsEnabled,
                    onCheckedChange = { onAction(NotificationAction.OnToggleSystemAnnouncements(it)) }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Price drop settings
            SettingsSection(title = "Price Drop Threshold") {
                Column {
                    Text(
                        text = "Minimum price drop: ${priceDropPercentage.toInt()}%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Only notify when price drops by at least this percentage",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Slider(
                        value = priceDropPercentage,
                        onValueChange = { priceDropPercentage = it },
                        onValueChangeFinished = {
                            onAction(NotificationAction.OnSetMinPriceDropPercentage(priceDropPercentage.toInt()))
                        },
                        valueRange = 0f..50f,
                        steps = 9,
                        colors = SliderDefaults.colors(
                            thumbColor = BalkanEstatePrimaryBlue,
                            activeTrackColor = BalkanEstatePrimaryBlue
                        ),
                        enabled = preferences.priceDropsEnabled && preferences.pushNotificationsEnabled
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Any", style = MaterialTheme.typography.labelSmall)
                        Text("50%", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Sound & vibration
            SettingsSection(title = "Sound & Vibration") {
                SettingsSwitch(
                    title = "Sound",
                    subtitle = "Play a sound for notifications",
                    checked = preferences.soundEnabled,
                    enabled = preferences.pushNotificationsEnabled,
                    onCheckedChange = { onAction(NotificationAction.OnToggleSound(it)) }
                )

                SettingsSwitch(
                    title = "Vibration",
                    subtitle = "Vibrate for notifications",
                    checked = preferences.vibrationEnabled,
                    enabled = preferences.pushNotificationsEnabled,
                    onCheckedChange = { onAction(NotificationAction.OnToggleVibration(it)) }
                )

                SettingsSwitch(
                    title = "Show on Lock Screen",
                    subtitle = "Display notifications on the lock screen",
                    checked = preferences.showOnLockScreen,
                    enabled = preferences.pushNotificationsEnabled,
                    onCheckedChange = { onAction(NotificationAction.OnToggleLockScreen(it)) }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Email notifications
            SettingsSection(title = "Email Notifications") {
                SettingsSwitch(
                    title = "Email Notifications",
                    subtitle = "Receive important updates via email",
                    checked = preferences.emailNotificationsEnabled,
                    onCheckedChange = { onAction(NotificationAction.OnToggleEmailNotifications(it)) }
                )

                if (preferences.emailNotificationsEnabled) {
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        placeholder = { Text("your@email.com") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (email.isNotBlank() && email != preferences.notificationEmail) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { onAction(NotificationAction.OnUpdateEmail(email)) },
                            colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Save Email")
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { onAction(NotificationAction.OnResetPreferences) },
                    enabled = !isSaving
                ) {
                    Text("Reset to Defaults")
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
                    enabled = !isSaving
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Done")
                    }
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
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = BalkanEstatePrimaryBlue
        )

        Spacer(modifier = Modifier.height(12.dp))

        content()
    }
}

@Composable
private fun SettingsSwitch(
    title: String,
    subtitle: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (enabled)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = if (enabled)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = BalkanEstatePrimaryBlue,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}
