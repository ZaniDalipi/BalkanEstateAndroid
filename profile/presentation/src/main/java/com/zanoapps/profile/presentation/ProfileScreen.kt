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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.zanoapps.core.presentation.designsystem.HelpIcon
import com.zanoapps.core.presentation.designsystem.LogoutIcon
import com.zanoapps.core.presentation.designsystem.NotificationBellIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.PrivacyIcon
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import com.zanoapps.core.presentation.designsystem.SavedHomesIcon
import com.zanoapps.core.presentation.designsystem.SettingsIcon
import com.zanoapps.core.presentation.designsystem.TermsIcon
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreenRoot(
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateToFavourites: () -> Unit,
    onNavigateToSavedSearches: () -> Unit,
    onNavigateToMyListings: () -> Unit,
    onLogout: () -> Unit
) {
    ProfileScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                ProfileAction.OnMyFavourites -> onNavigateToFavourites()
                ProfileAction.OnMySavedSearches -> onNavigateToSavedSearches()
                ProfileAction.OnMyListings -> onNavigateToMyListings()
                ProfileAction.OnConfirmLogout -> {
                    viewModel.onAction(action)
                    onLogout()
                }
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
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
    ) {
        // Profile Header
        ProfileHeader(
            user = state.user,
            onEditClick = { onAction(ProfileAction.OnEditProfile) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Stats
        state.user?.let { user ->
            QuickStatsSection(
                listingsCount = user.listingsCount,
                favouritesCount = user.favouritesCount,
                savedSearchesCount = user.savedSearchesCount
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // My Content Section
        ProfileSection(title = "My Content") {
            ProfileMenuItem(
                icon = EditPenIcon,
                title = "My Listings",
                subtitle = "${state.user?.listingsCount ?: 0} active listings",
                onClick = { onAction(ProfileAction.OnMyListings) }
            )
            ProfileMenuItem(
                icon = SavedHomesIcon,
                title = "Saved Properties",
                subtitle = "${state.user?.favouritesCount ?: 0} properties saved",
                onClick = { onAction(ProfileAction.OnMyFavourites) }
            )
            ProfileMenuItem(
                icon = SaveSearchIcon,
                title = "Saved Searches",
                subtitle = "${state.user?.savedSearchesCount ?: 0} searches saved",
                onClick = { onAction(ProfileAction.OnMySavedSearches) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Settings Section
        ProfileSection(title = "Settings") {
            ProfileToggleItem(
                icon = NotificationBellIcon,
                title = "Push Notifications",
                isEnabled = state.notificationsEnabled,
                onToggle = { onAction(ProfileAction.OnToggleNotifications) }
            )
            ProfileMenuItem(
                icon = SettingsIcon,
                title = "Account Settings",
                onClick = { onAction(ProfileAction.OnChangePassword) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Support Section
        ProfileSection(title = "Support") {
            ProfileMenuItem(
                icon = HelpIcon,
                title = "Help Center",
                onClick = { onAction(ProfileAction.OnHelpCenter) }
            )
            ProfileMenuItem(
                icon = PrivacyIcon,
                title = "Privacy Policy",
                onClick = { onAction(ProfileAction.OnPrivacyPolicy) }
            )
            ProfileMenuItem(
                icon = TermsIcon,
                title = "Terms of Service",
                onClick = { onAction(ProfileAction.OnTermsOfService) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { onAction(ProfileAction.OnLogoutClick) },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = LogoutIcon,
                    contentDescription = null,
                    tint = BalkanEstateRed,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Log Out",
                    color = BalkanEstateRed,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    // Logout confirmation dialog
    if (state.isLogoutDialogVisible) {
        AlertDialog(
            onDismissRequest = { onAction(ProfileAction.OnDismissLogout) },
            title = { Text("Log Out") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = { onAction(ProfileAction.OnConfirmLogout) }) {
                    Text("Log Out", color = BalkanEstateRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(ProfileAction.OnDismissLogout) }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ProfileHeader(
    user: UserProfile?,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
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
                    text = user?.name?.take(1)?.uppercase() ?: "U",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = user?.name ?: "User",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = user?.email ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Member since ${user?.memberSince ?: ""}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onEditClick) {
                Icon(
                    imageVector = EditPenIcon,
                    contentDescription = null,
                    tint = BalkanEstatePrimaryBlue,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Profile", color = BalkanEstatePrimaryBlue)
            }
        }
    }
}

@Composable
private fun QuickStatsSection(
    listingsCount: Int,
    favouritesCount: Int,
    savedSearchesCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            value = listingsCount.toString(),
            label = "Listings",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            value = favouritesCount.toString(),
            label = "Saved",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            value = savedSearchesCount.toString(),
            label = "Searches",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = BalkanEstatePrimaryBlue
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun ProfileSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BalkanEstatePrimaryBlue,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
    HorizontalDivider(modifier = Modifier.padding(start = 56.dp))
}

@Composable
private fun ProfileToggleItem(
    icon: ImageVector,
    title: String,
    isEnabled: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BalkanEstatePrimaryBlue,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = isEnabled,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = BalkanEstatePrimaryBlue
            )
        )
    }
    HorizontalDivider(modifier = Modifier.padding(start = 56.dp))
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    BalkanEstateTheme {
        ProfileScreen(
            state = ProfileState(
                user = UserProfile(
                    id = "1",
                    name = "Besmir Kola",
                    email = "besmir@example.com",
                    phone = "+355 69 123 4567",
                    avatarUrl = null,
                    memberSince = "January 2024",
                    listingsCount = 5,
                    favouritesCount = 12,
                    savedSearchesCount = 3
                ),
                isLoading = false
            ),
            onAction = {}
        )
    }
}
