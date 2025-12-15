package com.zanoapps.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.zanoapps.core.presentation.designsystem.AddSearchIcon
import com.zanoapps.core.presentation.designsystem.AgencyIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateLogo
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.CrossIcon
import com.zanoapps.core.presentation.designsystem.EditPenIcon
import com.zanoapps.core.presentation.designsystem.HomeIcon
import com.zanoapps.core.presentation.designsystem.InboxIcon
import com.zanoapps.core.presentation.designsystem.LogoutIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import com.zanoapps.core.presentation.designsystem.SavedHomesIcon
import com.zanoapps.core.presentation.designsystem.StarIcon

sealed interface DrawerMenuItem {
    val title: String
    val icon: @Composable () -> ImageVector

    data object Search : DrawerMenuItem {
        override val title = "Search"
        override val icon: @Composable () -> ImageVector = { SaveSearchIcon }
    }

    data object SavedSearches : DrawerMenuItem {
        override val title = "Saved Searches"
        override val icon: @Composable () -> ImageVector = { AddSearchIcon }
    }

    data object SavedProperties : DrawerMenuItem {
        override val title = "Saved Properties"
        override val icon: @Composable () -> ImageVector = { SavedHomesIcon }
    }

    data object TopAgents : DrawerMenuItem {
        override val title = "Top Agents"
        override val icon: @Composable () -> ImageVector = { HomeIcon }
    }

    data object Agencies : DrawerMenuItem {
        override val title = "Agencies"
        override val icon: @Composable () -> ImageVector = { AgencyIcon }
    }

    data object NewListing : DrawerMenuItem {
        override val title = "+ New Listing"
        override val icon: @Composable () -> ImageVector = { EditPenIcon }
    }

    data object Subscription : DrawerMenuItem {
        override val title = "Subscription"
        override val icon: @Composable () -> ImageVector = { StarIcon }
    }

    data object Inbox : DrawerMenuItem {
        override val title = "Inbox"
        override val icon: @Composable () -> ImageVector = { InboxIcon }
    }

    data object MyAccount : DrawerMenuItem {
        override val title = "My Account"
        override val icon: @Composable () -> ImageVector = { PersonIcon }
    }

    data object Logout : DrawerMenuItem {
        override val title = "Logout"
        override val icon: @Composable () -> ImageVector = { LogoutIcon }
    }
}

@Composable
fun BalkanEstateNavigationDrawer(
    isOpen: Boolean,
    selectedItem: DrawerMenuItem = DrawerMenuItem.Search,
    onItemClick: (DrawerMenuItem) -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isOpen) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onCloseClick() }
        ) {
            Surface(
                modifier = modifier
                    .fillMaxHeight()
                    .width(300.dp)
                    .clickable(enabled = false) {},
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header
                    DrawerHeader(onCloseClick = onCloseClick)

                    // Menu Items
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp)
                    ) {
                        DrawerItem(
                            item = DrawerMenuItem.Search,
                            isSelected = selectedItem == DrawerMenuItem.Search,
                            onClick = { onItemClick(DrawerMenuItem.Search) }
                        )
                        DrawerItem(
                            item = DrawerMenuItem.SavedSearches,
                            isSelected = selectedItem == DrawerMenuItem.SavedSearches,
                            onClick = { onItemClick(DrawerMenuItem.SavedSearches) }
                        )
                        DrawerItem(
                            item = DrawerMenuItem.SavedProperties,
                            isSelected = selectedItem == DrawerMenuItem.SavedProperties,
                            onClick = { onItemClick(DrawerMenuItem.SavedProperties) }
                        )
                        DrawerItem(
                            item = DrawerMenuItem.TopAgents,
                            isSelected = selectedItem == DrawerMenuItem.TopAgents,
                            onClick = { onItemClick(DrawerMenuItem.TopAgents) }
                        )
                        DrawerItem(
                            item = DrawerMenuItem.Agencies,
                            isSelected = selectedItem == DrawerMenuItem.Agencies,
                            onClick = { onItemClick(DrawerMenuItem.Agencies) }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // New Listing Button
                        NewListingButton(
                            onClick = { onItemClick(DrawerMenuItem.NewListing) }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        DrawerItem(
                            item = DrawerMenuItem.Subscription,
                            isSelected = selectedItem == DrawerMenuItem.Subscription,
                            onClick = { onItemClick(DrawerMenuItem.Subscription) }
                        )
                        DrawerItem(
                            item = DrawerMenuItem.Inbox,
                            isSelected = selectedItem == DrawerMenuItem.Inbox,
                            onClick = { onItemClick(DrawerMenuItem.Inbox) }
                        )
                    }

                    // Bottom Items
                    Column(
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color.LightGray.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        DrawerItem(
                            item = DrawerMenuItem.MyAccount,
                            isSelected = selectedItem == DrawerMenuItem.MyAccount,
                            onClick = { onItemClick(DrawerMenuItem.MyAccount) }
                        )
                        LogoutItem(
                            onClick = { onItemClick(DrawerMenuItem.Logout) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawerHeader(
    onCloseClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = BalkanEstateLogo,
                contentDescription = "Balkan Estate Logo",
                tint = BalkanEstatePrimaryBlue,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Row {
                Text(
                    text = "Balkan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BalkanEstatePrimaryBlue
                )
                Text(
                    text = " Estate",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BalkanEstateBlue
                )
            }
        }
        IconButton(onClick = onCloseClick) {
            Icon(
                imageVector = CrossIcon,
                contentDescription = "Close",
                tint = Color.Gray
            )
        }
    }
}

@Composable
private fun DrawerItem(
    item: DrawerMenuItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) BalkanEstatePrimaryBlue.copy(alpha = 0.1f) else Color.Transparent
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon(),
            contentDescription = item.title,
            tint = if (isSelected) BalkanEstatePrimaryBlue else Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = item.title,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) BalkanEstatePrimaryBlue else Color.DarkGray
        )
    }
}

@Composable
private fun NewListingButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(BalkanEstateOrange)
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = EditPenIcon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "+ New Listing",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun LogoutItem(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = LogoutIcon,
            contentDescription = "Logout",
            tint = BalkanEstateRed,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Logout",
            fontSize = 16.sp,
            color = BalkanEstateRed
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BalkanEstateNavigationDrawerPreview() {
    BalkanEstateTheme {
        BalkanEstateNavigationDrawer(
            isOpen = true,
            selectedItem = DrawerMenuItem.Search,
            onItemClick = {},
            onCloseClick = {}
        )
    }
}
