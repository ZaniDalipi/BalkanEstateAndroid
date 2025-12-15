package com.zanoapps.search.presentation.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.zanoapps.core.presentation.designsystem.AddedToFavIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.EnterpriseIcon
import com.zanoapps.core.presentation.designsystem.EyeClosedIcon
import com.zanoapps.core.presentation.designsystem.HomeIcon
import com.zanoapps.core.presentation.designsystem.MessagesBottomBarIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import com.zanoapps.core.presentation.designsystem.SavedHomesIcon
import com.zanoapps.core.presentation.designsystem.VirtualTour2Icon

import com.zanoapps.search.presentation.R

@Composable
fun DrawerContent(
    isLoggedIn: Boolean,
    onSearchClick: () -> Unit,
    onSavedSearchesClick: () -> Unit,
    onSavedPropertiesClick: () -> Unit,
    onTopAgentsClick: () -> Unit,
    onAgenciesClick: () -> Unit,
    onNewListingClick: () -> Unit,
    onSubscriptionClick: () -> Unit,
    onInboxClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo placeholder
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "B",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Row {
                        Text(
                            text = "Balkan",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = " Estate",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFFFFA500) // Orange color
                        )
                    }
                }
                
                IconButton(onClick = onCloseClick) {
                    Icon(
                        imageVector = EyeClosedIcon,
                        contentDescription = "Close"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Navigation Items
            DrawerMenuItem(
                icon = SaveSearchIcon,
                text = stringResource(R.string.menu_search),
                onClick = onSearchClick,
                isSelected = true
            )
            
            DrawerMenuItem(
                icon = SaveSearchIcon,
                text = stringResource(R.string.menu_saved_searches),
                onClick = onSavedSearchesClick
            )
            
            DrawerMenuItem(
                icon = AddedToFavIcon,
                text = stringResource(R.string.menu_saved_properties),
                onClick = onSavedPropertiesClick
            )
            
            DrawerMenuItem(
                icon = PersonIcon,
                text = stringResource(R.string.menu_top_agents),
                onClick = onTopAgentsClick
            )
            
            DrawerMenuItem(
                icon = HomeIcon,
                text = stringResource(R.string.menu_agencies),
                onClick = onAgenciesClick
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // New Listing Button
            Button(
                onClick = onNewListingClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFA500)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = SavedHomesIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.menu_new_listing),
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            DrawerMenuItem(
                icon = EnterpriseIcon,
                text = stringResource(R.string.menu_subscription),
                onClick = onSubscriptionClick
            )
            
            DrawerMenuItem(
                icon = MessagesBottomBarIcon,
                text = stringResource(R.string.menu_inbox),
                onClick = onInboxClick
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Bottom Section
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            DrawerMenuItem(
                icon = PersonIcon,
                text = stringResource(R.string.menu_my_account),
                onClick = onProfileClick
            )
            
            if (isLoggedIn) {
                DrawerMenuItem(
                    icon = VirtualTour2Icon,
                    text = stringResource(R.string.menu_logout),
                    onClick = { /* Handle logout */ },
                    textColor = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean = false,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        } else {
            Color.Transparent
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else textColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) MaterialTheme.colorScheme.primary else textColor,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawerContentPreview() {
    BalkanEstateTheme {
        DrawerContent(
            isLoggedIn = true,
            onSearchClick = {},
            onSavedSearchesClick = {},
            onSavedPropertiesClick = {},
            onTopAgentsClick = {},
            onAgenciesClick = {},
            onNewListingClick = {},
            onSubscriptionClick = {},
            onInboxClick = {},
            onProfileClick = {},
            onCloseClick = {}
        )
    }
}
