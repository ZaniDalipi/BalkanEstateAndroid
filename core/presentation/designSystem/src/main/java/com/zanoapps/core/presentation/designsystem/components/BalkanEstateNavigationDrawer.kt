package com.zanoapps.core.presentation.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import com.zanoapps.core.presentation.designsystem.AddSearchIcon
import com.zanoapps.core.presentation.designsystem.AgencyIcon
import com.zanoapps.core.presentation.designsystem.AnalyticsIcon
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
import com.zanoapps.core.presentation.designsystem.MapViewIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import com.zanoapps.core.presentation.designsystem.SavedHomesIcon
import com.zanoapps.core.presentation.designsystem.SparkleIcon
import com.zanoapps.core.presentation.designsystem.StarIcon

sealed interface DrawerMenuItem {
    val title: String
    val icon: @Composable () -> ImageVector

    data object Search : DrawerMenuItem {
        override val title = "Search"
        override val icon: @Composable () -> ImageVector = { SaveSearchIcon }
    }

    data object ExploreCities : DrawerMenuItem {
        override val title = "Explore Cities"
        override val icon: @Composable () -> ImageVector = { SparkleIcon }
    }

    data object Map : DrawerMenuItem {
        override val title = "Map"
        override val icon: @Composable () -> ImageVector = { MapViewIcon }
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

    data object Tools : DrawerMenuItem {
        override val title = "Tools"
        override val icon: @Composable () -> ImageVector = { Icons.Default.Build }
    }

    data object NewListing : DrawerMenuItem {
        override val title = "+ New Listing"
        override val icon: @Composable () -> ImageVector = { EditPenIcon }
    }

    data object Subscription : DrawerMenuItem {
        override val title = "Subscription"
        override val icon: @Composable () -> ImageVector = { StarIcon }
    }

    data object Analytics : DrawerMenuItem {
        override val title = "Analytics"
        override val icon: @Composable () -> ImageVector = { AnalyticsIcon }
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
        override val title = "logout"
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
    var isToolsExpanded by remember { mutableStateOf(false) }

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
                    // Header with AI badge
                    DrawerHeader(onCloseClick = onCloseClick)

                    // Scrollable Menu Items
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 8.dp)
                    ) {
                        // Search
                        DrawerItem(
                            item = DrawerMenuItem.Search,
                            isSelected = selectedItem == DrawerMenuItem.Search,
                            onClick = { onItemClick(DrawerMenuItem.Search) }
                        )

                        // Explore Cities (AI feature)
                        DrawerItem(
                            item = DrawerMenuItem.ExploreCities,
                            isSelected = selectedItem == DrawerMenuItem.ExploreCities,
                            onClick = { onItemClick(DrawerMenuItem.ExploreCities) }
                        )

                        // Saved Searches
                        DrawerItem(
                            item = DrawerMenuItem.SavedSearches,
                            isSelected = selectedItem == DrawerMenuItem.SavedSearches,
                            onClick = { onItemClick(DrawerMenuItem.SavedSearches) }
                        )

                        // Saved Properties
                        DrawerItem(
                            item = DrawerMenuItem.SavedProperties,
                            isSelected = selectedItem == DrawerMenuItem.SavedProperties,
                            onClick = { onItemClick(DrawerMenuItem.SavedProperties) }
                        )

                        // Top Agents
                        DrawerItem(
                            item = DrawerMenuItem.TopAgents,
                            isSelected = selectedItem == DrawerMenuItem.TopAgents,
                            onClick = { onItemClick(DrawerMenuItem.TopAgents) }
                        )

                        // Agencies
                        DrawerItem(
                            item = DrawerMenuItem.Agencies,
                            isSelected = selectedItem == DrawerMenuItem.Agencies,
                            onClick = { onItemClick(DrawerMenuItem.Agencies) }
                        )

                        // Tools (expandable)
                        DrawerItemExpandable(
                            item = DrawerMenuItem.Tools,
                            isSelected = selectedItem == DrawerMenuItem.Tools,
                            isExpanded = isToolsExpanded,
                            onClick = { isToolsExpanded = !isToolsExpanded }
                        )

                        // Tools sub-items (when expanded)
                        AnimatedVisibility(
                            visible = isToolsExpanded,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Column(
                                modifier = Modifier.padding(start = 24.dp)
                            ) {
                                DrawerSubItem(
                                    title = "Mortgage Calculator",
                                    onClick = { /* TODO */ }
                                )
                                DrawerSubItem(
                                    title = "Property Valuation",
                                    onClick = { /* TODO */ }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // New Listing Button
                        NewListingButton(
                            onClick = { onItemClick(DrawerMenuItem.NewListing) }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Subscription
                        DrawerItem(
                            item = DrawerMenuItem.Subscription,
                            isSelected = selectedItem == DrawerMenuItem.Subscription,
                            onClick = { onItemClick(DrawerMenuItem.Subscription) }
                        )

                        // Analytics
                        DrawerItem(
                            item = DrawerMenuItem.Analytics,
                            isSelected = selectedItem == DrawerMenuItem.Analytics,
                            onClick = { onItemClick(DrawerMenuItem.Analytics) }
                        )

                        // Inbox
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

                        // Language selector
                        LanguageSelector(
                            currentLanguage = "English",
                            flagEmoji = "\uD83C\uDDEC\uD83C\uDDE7", // UK flag
                            onClick = { /* TODO: Open language picker */ }
                        )

                        // My Account
                        DrawerItem(
                            item = DrawerMenuItem.MyAccount,
                            isSelected = selectedItem == DrawerMenuItem.MyAccount,
                            onClick = { onItemClick(DrawerMenuItem.MyAccount) }
                        )

                        // Logout
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
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "Balkan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BalkanEstatePrimaryBlue
                )
                Text(
                    text = "Estate",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BalkanEstateBlue
                )
                Text(
                    text = "AI",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BalkanEstatePrimaryBlue,
                    modifier = Modifier.padding(start = 2.dp)
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
private fun DrawerItemExpandable(
    item: DrawerMenuItem,
    isSelected: Boolean,
    isExpanded: Boolean,
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
            color = if (isSelected) BalkanEstatePrimaryBlue else Color.DarkGray,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = if (isExpanded) "Collapse" else "Expand",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun DrawerSubItem(
    title: String,
    onClick: () -> Unit
) {
    Text(
        text = title,
        fontSize = 14.sp,
        color = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    )
}

@Composable
private fun LanguageSelector(
    currentLanguage: String,
    flagEmoji: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = flagEmoji,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = currentLanguage,
            fontSize = 16.sp,
            color = Color.DarkGray
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
            text = "logout",
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
