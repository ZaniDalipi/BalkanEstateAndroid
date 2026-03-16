package com.zanoapps.profile.presentation.profile

import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.EditPenIcon
import com.zanoapps.core.presentation.designsystem.EmailIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.LogoutIcon
import com.zanoapps.core.presentation.designsystem.NotificationBellIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.SavedHomesIcon
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import com.zanoapps.core.presentation.designsystem.StarIcon
import androidx.compose.ui.tooling.preview.Preview
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreenRoot(
    viewModel: ProfileViewModel = koinViewModel(),
    onLogout: () -> Unit,
    onNavigateToSavedProperties: () -> Unit,
    onNavigateToSavedSearches: () -> Unit,
    onNavigateToSubscription: () -> Unit
) {
    ProfileScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                ProfileAction.OnConfirmLogout -> {
                    viewModel.onAction(action)
                    onLogout()
                }
                ProfileAction.OnSavedPropertiesClick -> onNavigateToSavedProperties()
                ProfileAction.OnSavedSearchesClick -> onNavigateToSavedSearches()
                ProfileAction.OnSubscriptionClick -> onNavigateToSubscription()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
    ) {
        // Profile Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(BalkanEstatePrimaryBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${state.userProfile.firstName.take(1)}${state.userProfile.lastName.take(1)}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${state.userProfile.firstName} ${state.userProfile.lastName}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Text(
                text = state.userProfile.email,
                style = MaterialTheme.typography.bodyMedium,
                color = BalkanEstateGray
            )
            if (state.userProfile.location.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = LocationIcon,
                        contentDescription = null,
                        tint = BalkanEstateGray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = state.userProfile.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = BalkanEstateGray
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Member since ${state.userProfile.memberSince}",
                style = MaterialTheme.typography.bodySmall,
                color = BalkanEstateGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(value = state.userProfile.listingsCount.toString(), label = "Listings")
                StatItem(value = state.userProfile.savedCount.toString(), label = "Saved")
                StatItem(value = state.userProfile.reviewsCount.toString(), label = "Reviews")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!state.isEditing) {
                OutlinedButton(
                    onClick = { onAction(ProfileAction.OnEditProfileClick) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = EditPenIcon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Profile")
                }
            }
        }

        // Edit Form
        if (state.isEditing) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Edit Profile",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = state.editFirstName,
                            onValueChange = { onAction(ProfileAction.OnFirstNameChanged(it)) },
                            label = { Text("First Name") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue)
                        )
                        OutlinedTextField(
                            value = state.editLastName,
                            onValueChange = { onAction(ProfileAction.OnLastNameChanged(it)) },
                            label = { Text("Last Name") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.editEmail,
                        onValueChange = { onAction(ProfileAction.OnEmailChanged(it)) },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.editPhone,
                        onValueChange = { onAction(ProfileAction.OnPhoneChanged(it)) },
                        label = { Text("Phone") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.editLocation,
                        onValueChange = { onAction(ProfileAction.OnLocationChanged(it)) },
                        label = { Text("Location") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BalkanEstatePrimaryBlue)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onAction(ProfileAction.OnCancelEdit) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) { Text("Cancel") }
                        Button(
                            onClick = { onAction(ProfileAction.OnSaveProfile) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
                            enabled = !state.isSaving
                        ) {
                            if (state.isSaving) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Save")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Menu Items
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                ProfileMenuItem(
                    icon = EditPenIcon,
                    title = "My Listings",
                    subtitle = "${state.userProfile.listingsCount} active listings",
                    onClick = { onAction(ProfileAction.OnMyListingsClick) }
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                ProfileMenuItem(
                    icon = SavedHomesIcon,
                    title = "Saved Properties",
                    subtitle = "${state.userProfile.savedCount} saved",
                    onClick = { onAction(ProfileAction.OnSavedPropertiesClick) }
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                ProfileMenuItem(
                    icon = SaveSearchIcon,
                    title = "Saved Searches",
                    subtitle = "Get alerts for new listings",
                    onClick = { onAction(ProfileAction.OnSavedSearchesClick) }
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                ProfileMenuItem(
                    icon = NotificationBellIcon,
                    title = "Notification Settings",
                    subtitle = "Manage your alerts",
                    onClick = { onAction(ProfileAction.OnNotificationSettingsClick) }
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                ProfileMenuItem(
                    icon = StarIcon,
                    title = "Subscription",
                    subtitle = if (state.userProfile.isPremium) "Premium Plan" else "Free Plan",
                    onClick = { onAction(ProfileAction.OnSubscriptionClick) },
                    trailingColor = if (state.userProfile.isPremium) BalkanEstateOrange else null
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Support & Legal
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                ProfileMenuItem(
                    icon = PersonIcon,
                    title = "Help & Support",
                    onClick = { onAction(ProfileAction.OnHelpClick) }
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                ProfileMenuItem(
                    icon = PersonIcon,
                    title = "Privacy Policy",
                    onClick = { onAction(ProfileAction.OnPrivacyPolicyClick) }
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                ProfileMenuItem(
                    icon = PersonIcon,
                    title = "Terms of Service",
                    onClick = { onAction(ProfileAction.OnTermsClick) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Account Actions
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                ProfileMenuItem(
                    icon = PersonIcon,
                    title = "Change Password",
                    onClick = { onAction(ProfileAction.OnChangePasswordClick) }
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                ProfileMenuItem(
                    icon = LogoutIcon,
                    title = "Logout",
                    titleColor = BalkanEstateRed,
                    onClick = { onAction(ProfileAction.OnLogoutClick) }
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                ProfileMenuItem(
                    icon = PersonIcon,
                    title = "Delete Account",
                    titleColor = BalkanEstateRed,
                    onClick = { onAction(ProfileAction.OnDeleteAccountClick) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Balkan Estate AI v1.0.0",
            style = MaterialTheme.typography.bodySmall,
            color = BalkanEstateGray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
    }

    // Logout Dialog
    if (state.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { onAction(ProfileAction.OnDismissLogout) },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(onClick = { onAction(ProfileAction.OnConfirmLogout) }) {
                    Text("Logout", color = BalkanEstateRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(ProfileAction.OnDismissLogout) }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Delete Account Dialog
    if (state.showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { onAction(ProfileAction.OnDismissDeleteAccount) },
            title = { Text("Delete Account") },
            text = { Text("This action is permanent and cannot be undone. All your data will be lost.") },
            confirmButton = {
                TextButton(onClick = { onAction(ProfileAction.OnConfirmDeleteAccount) }) {
                    Text("Delete", color = BalkanEstateRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(ProfileAction.OnDismissDeleteAccount) }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = BalkanEstatePrimaryBlue
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = BalkanEstateGray
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    titleColor: Color = Color.DarkGray,
    trailingColor: Color? = null,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = trailingColor ?: titleColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = titleColor
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = BalkanEstateGray
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    BalkanEstateTheme {
        ProfileScreen(
            state = ProfileState(),
            onAction = {}
        )
    }
}
