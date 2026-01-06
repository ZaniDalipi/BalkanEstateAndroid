package com.zanoapps.profile.presentation.profile

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zanoapps.core.presentation.designsystem.AddedToFavIcon
import com.zanoapps.core.presentation.designsystem.BackIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.EditPenIcon
import com.zanoapps.core.presentation.designsystem.HomeIcon
import com.zanoapps.core.presentation.designsystem.KeyboardArrowUpIcon
import com.zanoapps.core.presentation.designsystem.LogoutIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import com.zanoapps.profile.domain.model.UserProfile
import com.zanoapps.profile.presentation.R
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreenRoot(
    viewModel: ProfileViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onFavouritesClick: () -> Unit,
    onSavedSearchesClick: () -> Unit,
    onMyListingsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.events.collect { event ->
            when (event) {
                is ProfileEvent.Error -> {
                    Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
                }
                ProfileEvent.LogoutSuccess -> {
                    onLogout()
                }
            }
        }
    }

    ProfileScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                ProfileAction.OnBackClick -> onBackClick()
                ProfileAction.OnEditProfileClick -> onEditProfileClick()
                ProfileAction.OnFavouritesClick -> onFavouritesClick()
                ProfileAction.OnSavedSearchesClick -> onSavedSearchesClick()
                ProfileAction.OnMyListingsClick -> onMyListingsClick()
                ProfileAction.OnSettingsClick -> onSettingsClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.my_profile),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(ProfileAction.OnBackClick) }) {
                        Icon(
                            imageVector = BackIcon,
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
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { onAction(ProfileAction.OnRefresh) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Profile Header Card
                    state.profile?.let { profile ->
                        ProfileHeaderCard(
                            profile = profile,
                            onEditClick = { onAction(ProfileAction.OnEditProfileClick) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Stats Card
                        ProfileStatsCard(profile = profile)

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Menu Items
                    ProfileMenuCard(
                        items = listOf(
                            ProfileMenuItem(
                                icon = SaveSearchIcon,
                                title = stringResource(R.string.saved_searches),
                                onClick = { onAction(ProfileAction.OnSavedSearchesClick) }
                            ),
                            ProfileMenuItem(
                                icon = AddedToFavIcon,
                                title = stringResource(R.string.favourites),
                                onClick = { onAction(ProfileAction.OnFavouritesClick) }
                            ),
                            ProfileMenuItem(
                                icon = HomeIcon,
                                title = stringResource(R.string.my_listings),
                                onClick = { onAction(ProfileAction.OnMyListingsClick) }
                            )
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Settings and Logout
                    ProfileMenuCard(
                        items = listOf(
                            ProfileMenuItem(
                                icon = LogoutIcon,
                                title = stringResource(R.string.logout),
                                onClick = { onAction(ProfileAction.OnLogoutClick) },
                                tint = Color.Red
                            )
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileHeaderCard(
    profile: UserProfile,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                if (profile.profileImageUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(profile.profileImageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = profile.initials,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = BalkanEstatePrimaryBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name
            Text(
                text = profile.fullName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Email
            Text(
                text = profile.email,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Member Since
            Text(
                text = stringResource(
                    R.string.member_since,
                    formatDate(profile.memberSince)
                ),
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Profile Button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f))
                    .clickable(onClick = onEditClick)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = EditPenIcon,
                    contentDescription = null,
                    tint = BalkanEstatePrimaryBlue,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.edit_profile),
                    color = BalkanEstatePrimaryBlue,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ProfileStatsCard(profile: UserProfile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                value = profile.savedSearchesCount.toString(),
                label = stringResource(R.string.searches)
            )
            StatItem(
                value = profile.favouritesCount.toString(),
                label = stringResource(R.string.favourites)
            )
            StatItem(
                value = profile.listingsCount.toString(),
                label = stringResource(R.string.listings)
            )
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

data class ProfileMenuItem(
    val icon: ImageVector,
    val title: String,
    val onClick: () -> Unit,
    val tint: Color = Color.Black
)

@Composable
private fun ProfileMenuCard(items: List<ProfileMenuItem>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = item.onClick)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = item.tint,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = item.title,
                        fontSize = 16.sp,
                        color = item.tint,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = KeyboardArrowUpIcon,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
                if (index < items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFEEEEEE)
                    )
                }
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    BalkanEstateTheme {
        ProfileScreen(
            state = ProfileState(
                profile = UserProfile(
                    id = "1",
                    email = "john.doe@example.com",
                    firstName = "John",
                    lastName = "Doe",
                    phoneNumber = "+1234567890",
                    profileImageUrl = null,
                    bio = "Looking for my dream home",
                    savedSearchesCount = 5,
                    favouritesCount = 12,
                    listingsCount = 0
                )
            ),
            onAction = {}
        )
    }
}
