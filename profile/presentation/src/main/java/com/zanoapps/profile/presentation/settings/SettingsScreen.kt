package com.zanoapps.profile.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.BellIcon
import com.zanoapps.core.presentation.designsystem.GlobeIcon
import com.zanoapps.core.presentation.designsystem.HelpIcon
import com.zanoapps.core.presentation.designsystem.InfoIcon
import com.zanoapps.core.presentation.designsystem.LockIcon
import com.zanoapps.core.presentation.designsystem.MoonIcon
import com.zanoapps.core.presentation.designsystem.StarIcon
import com.zanoapps.profile.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreenRoot(
    viewModel: SettingsViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    SettingsScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                SettingsAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(SettingsAction.OnBackClick) }) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Preferences Section
            SettingsSection(title = stringResource(R.string.preferences)) {
                SettingsSwitchItem(
                    icon = BellIcon,
                    title = stringResource(R.string.notifications),
                    subtitle = stringResource(R.string.notifications_description),
                    isChecked = state.notificationsEnabled,
                    onCheckedChange = { onAction(SettingsAction.OnToggleNotifications(it)) }
                )

                HorizontalDivider(color = Color(0xFFE5E7EB))

                SettingsSwitchItem(
                    icon = MoonIcon,
                    title = stringResource(R.string.dark_mode),
                    subtitle = stringResource(R.string.dark_mode_description),
                    isChecked = state.darkModeEnabled,
                    onCheckedChange = { onAction(SettingsAction.OnToggleDarkMode(it)) }
                )

                HorizontalDivider(color = Color(0xFFE5E7EB))

                SettingsNavigationItem(
                    icon = GlobeIcon,
                    title = stringResource(R.string.language),
                    subtitle = state.selectedLanguage,
                    onClick = { onAction(SettingsAction.OnLanguageClick) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Privacy & Security Section
            SettingsSection(title = stringResource(R.string.privacy_security)) {
                SettingsNavigationItem(
                    icon = LockIcon,
                    title = stringResource(R.string.privacy_policy),
                    onClick = { onAction(SettingsAction.OnPrivacyClick) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Support Section
            SettingsSection(title = stringResource(R.string.support)) {
                SettingsNavigationItem(
                    icon = HelpIcon,
                    title = stringResource(R.string.help_center),
                    onClick = { onAction(SettingsAction.OnHelpClick) }
                )

                HorizontalDivider(color = Color(0xFFE5E7EB))

                SettingsNavigationItem(
                    icon = StarIcon,
                    title = stringResource(R.string.rate_app),
                    onClick = { onAction(SettingsAction.OnRateAppClick) }
                )

                HorizontalDivider(color = Color(0xFFE5E7EB))

                SettingsNavigationItem(
                    icon = InfoIcon,
                    title = stringResource(R.string.about),
                    subtitle = stringResource(R.string.version_format, state.appVersion),
                    onClick = { onAction(SettingsAction.OnAboutClick) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Version
            Text(
                text = stringResource(R.string.balkan_estate_version, state.appVersion),
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))
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
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun SettingsNavigationItem(
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
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            subtitle?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
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
private fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
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

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            subtitle?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = BalkanEstatePrimaryBlue,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    BalkanEstateTheme {
        SettingsScreen(
            state = SettingsState(),
            onAction = {}
        )
    }
}
