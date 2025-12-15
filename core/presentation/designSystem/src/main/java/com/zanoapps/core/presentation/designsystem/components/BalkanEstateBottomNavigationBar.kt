package com.zanoapps.core.presentation.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.AddSearchIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.EditPenIcon
import com.zanoapps.core.presentation.designsystem.InboxIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import com.zanoapps.core.presentation.designsystem.SavedHomesIcon

/**
 * Represents a navigation item in the bottom navigation bar
 * Updated for Material 3 Expressive design
 */
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val iconComposable: @Composable () -> ImageVector
) {
    data object Search : BottomNavItem(
        route = "search",
        title = "Search",
        iconComposable = { SaveSearchIcon }
    )

    data object SavedSearches : BottomNavItem(
        route = "saved_searches",
        title = "Saved Searches",
        iconComposable = { AddSearchIcon }
    )

    data object SavedProperties : BottomNavItem(
        route = "saved",
        title = "Saved",
        iconComposable = { SavedHomesIcon }
    )

    data object Inbox : BottomNavItem(
        route = "inbox",
        title = "Inbox",
        iconComposable = { InboxIcon }
    )

    data object Profile : BottomNavItem(
        route = "profile",
        title = "Profile",
        iconComposable = { PersonIcon }
    )

    companion object {
        val items = listOf(Search, SavedSearches, SavedProperties, Inbox, Profile)
    }
}

/**
 * Material 3 Expressive Bottom Navigation Bar for Balkan Estate
 * Features animated selection indicator and expressive styling
 *
 * @param selectedItem The currently selected navigation item
 * @param onItemSelected Callback when a navigation item is selected
 * @param onFabClick Callback when the FAB is clicked (create new listing)
 * @param showFab Whether to show the FAB
 * @param modifier Optional modifier for the navigation bar
 */
@Composable
fun BalkanEstateBottomNavigationBar(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit,
    onFabClick: () -> Unit = {},
    showFab: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        // Main Navigation Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            color = Color.White,
            shadowElevation = 12.dp,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            NavigationBar(
                containerColor = Color.Transparent,
                contentColor = BalkanEstatePrimaryBlue,
                tonalElevation = 0.dp,
                windowInsets = WindowInsets(0.dp),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                BottomNavItem.items.forEach { item ->
                    val isSelected = selectedItem.route == item.route

                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.1f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "scale"
                    )

                    val iconColor by animateColorAsState(
                        targetValue = if (isSelected) BalkanEstatePrimaryBlue else Color.Gray,
                        animationSpec = spring(stiffness = Spring.StiffnessLow),
                        label = "iconColor"
                    )

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onItemSelected(item) },
                        icon = {
                            Box(
                                modifier = Modifier.scale(scale),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.iconComposable(),
                                    contentDescription = item.title,
                                    tint = iconColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1,
                                color = iconColor
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BalkanEstatePrimaryBlue,
                            selectedTextColor = BalkanEstatePrimaryBlue,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = BalkanEstatePrimaryBlue.copy(alpha = 0.12f)
                        ),
                        alwaysShowLabel = true
                    )
                }
            }
        }

        // Floating Action Button for New Listing
        if (showFab) {
            FloatingActionButton(
                onClick = onFabClick,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-28).dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = BalkanEstateOrange.copy(alpha = 0.4f),
                        spotColor = BalkanEstateOrange.copy(alpha = 0.4f)
                    ),
                containerColor = BalkanEstateOrange,
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 4.dp
                )
            ) {
                Icon(
                    imageVector = EditPenIcon,
                    contentDescription = "New Listing",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

/**
 * Material 3 Expressive Navigation Rail for tablet/larger screens
 * Contains 3-7 destinations plus an optional FAB
 *
 * @param selectedItem The currently selected navigation item
 * @param onItemSelected Callback when a navigation item is selected
 * @param onFabClick Callback when the FAB is clicked (create new listing)
 * @param modifier Optional modifier for the navigation rail
 */
@Composable
fun BalkanEstateNavigationRail(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit,
    onFabClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier,
        containerColor = Color.White,
        contentColor = BalkanEstatePrimaryBlue,
        header = {
            // FAB at the top of the rail
            FloatingActionButton(
                onClick = onFabClick,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .size(56.dp),
                containerColor = BalkanEstateOrange,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Icon(
                    imageVector = EditPenIcon,
                    contentDescription = "New Listing",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        BottomNavItem.items.forEach { item ->
            val isSelected = selectedItem.route == item.route

            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.05f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "scale"
            )

            val iconColor by animateColorAsState(
                targetValue = if (isSelected) BalkanEstatePrimaryBlue else Color.Gray,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "iconColor"
            )

            NavigationRailItem(
                selected = isSelected,
                onClick = { onItemSelected(item) },
                icon = {
                    Box(
                        modifier = Modifier.scale(scale),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.iconComposable(),
                            contentDescription = item.title,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1,
                        color = iconColor
                    )
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = BalkanEstatePrimaryBlue,
                    selectedTextColor = BalkanEstatePrimaryBlue,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = BalkanEstatePrimaryBlue.copy(alpha = 0.12f)
                ),
                alwaysShowLabel = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BalkanEstateBottomNavigationBarPreview() {
    BalkanEstateTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color(0xFFF8FAFC)),
            contentAlignment = Alignment.BottomCenter
        ) {
            BalkanEstateBottomNavigationBar(
                selectedItem = BottomNavItem.Search,
                onItemSelected = {},
                onFabClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BalkanEstateBottomNavigationBarSavedSelectedPreview() {
    BalkanEstateTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color(0xFFF8FAFC)),
            contentAlignment = Alignment.BottomCenter
        ) {
            BalkanEstateBottomNavigationBar(
                selectedItem = BottomNavItem.SavedProperties,
                onItemSelected = {},
                onFabClick = {}
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 80, heightDp = 600)
@Composable
private fun BalkanEstateNavigationRailPreview() {
    BalkanEstateTheme {
        BalkanEstateNavigationRail(
            selectedItem = BottomNavItem.Search,
            onItemSelected = {},
            onFabClick = {}
        )
    }
}
