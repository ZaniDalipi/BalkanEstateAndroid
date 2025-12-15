package com.zanoapps.core.presentation.designsystem.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.HomeIcon
import com.zanoapps.core.presentation.designsystem.InboxIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import com.zanoapps.core.presentation.designsystem.SavedHomesIcon

/**
 * Represents a navigation item in the bottom navigation bar
 */
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val iconComposable: @Composable () -> ImageVector
) {
    data object Home : BottomNavItem(
        route = "home",
        title = "Home",
        iconComposable = { HomeIcon }
    )

    data object Search : BottomNavItem(
        route = "search",
        title = "Search",
        iconComposable = { SaveSearchIcon }
    )

    data object Saved : BottomNavItem(
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
        val items = listOf(Home, Search, Saved, Inbox, Profile)
    }
}

/**
 * Material 3 Bottom Navigation Bar for Balkan Estate
 *
 * @param selectedItem The currently selected navigation item
 * @param onItemSelected Callback when a navigation item is selected
 * @param modifier Optional modifier for the navigation bar
 */
@Composable
fun BalkanEstateBottomNavigationBar(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
        contentColor = BalkanEstatePrimaryBlue,
        tonalElevation = 8.dp,
        windowInsets = WindowInsets(0.dp)
    ) {
        BottomNavItem.items.forEach { item ->
            val isSelected = selectedItem.route == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(item) },
                icon = {
                    Icon(
                        imageVector = item.iconComposable(),
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BalkanEstatePrimaryBlue,
                    selectedTextColor = BalkanEstatePrimaryBlue,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = BalkanEstatePrimaryBlue.copy(alpha = 0.1f)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BalkanEstateBottomNavigationBarPreview() {
    BalkanEstateTheme {
        BalkanEstateBottomNavigationBar(
            selectedItem = BottomNavItem.Home,
            onItemSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BalkanEstateBottomNavigationBarSearchSelectedPreview() {
    BalkanEstateTheme {
        BalkanEstateBottomNavigationBar(
            selectedItem = BottomNavItem.Search,
            onItemSelected = {}
        )
    }
}
