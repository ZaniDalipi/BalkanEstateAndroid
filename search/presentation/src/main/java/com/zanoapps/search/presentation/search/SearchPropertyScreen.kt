package com.zanoapps.search.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.domain.enums.SortOption
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.designsystem.AddedToFavIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.FiltersIcon
import com.zanoapps.core.presentation.designsystem.KeyboardArrowDownIcon
import com.zanoapps.core.presentation.designsystem.MenuHamburgerIcon
import com.zanoapps.core.presentation.designsystem.NotificationBellIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.R
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import com.zanoapps.core.presentation.designsystem.SparkleIcon
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateNavigationDrawer
import com.zanoapps.core.presentation.designsystem.components.DrawerMenuItem
import com.zanoapps.core.presentation.designsystem.components.EmailSubscriptionBar
import com.zanoapps.core.presentation.designsystem.components.ListMapToggle
import com.zanoapps.core.presentation.designsystem.components.PropertyCard
import com.zanoapps.search.domain.model.MapLocation
import com.zanoapps.search.domain.model.MockData
import com.zanoapps.search.domain.model.SearchFilters
import org.koin.androidx.compose.koinViewModel


@Composable
fun SearchPropertyScreenRoot(
    viewModel: SearchPropertyViewModel = koinViewModel()
) {
    SearchPropertyScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

// Keep the old name for backward compatibility
@Composable
fun SearchPropertyScreenRot(
    viewModel: SearchPropertyViewModel = koinViewModel()
) {
    SearchPropertyScreenRoot(viewModel)
}

@Composable
private fun SearchPropertyScreen(
    state: SearchState,
    onAction: (SearchAction) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color(0xFFF8FAFC),
            topBar = {
                SearchTopBar(
                    state = state,
                    onMenuClick = { onAction(SearchAction.OnOpenDrawer) },
                    onFilterClick = { onAction(SearchAction.OnFilterClick) },
                    onQueryChange = { query -> onAction(SearchAction.OnSearchQueryChanged(query)) },
                    onProfileClick = { /* Navigate to profile */ }
                )
            },
            bottomBar = {
                Column {
                    // List/Map Toggle
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ListMapToggle(
                            isListView = state.isListView,
                            onToggle = { isListView ->
                                onAction(SearchAction.OnViewModeToggle(isListView))
                            }
                        )
                    }

                    // Bottom action buttons
                    BottomActionButtons(
                        onFavoriteClick = { /* Navigate to favorites */ },
                        onNotificationClick = { /* Navigate to notifications */ },
                        onSparkleClick = { /* AI features */ }
                    )

                    // Email subscription bar
                    EmailSubscriptionBar(
                        onSubscribe = { email ->
                            onAction(SearchAction.OnSubscribe(email))
                        }
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Results count and sort
                ResultsHeader(
                    resultsCount = state.filteredProperties.size.takeIf { it > 0 } ?: MockData.getMockProperties().size,
                    sortOption = state.sortOption,
                    onSortChange = { sortOption ->
                        onAction(SearchAction.OnSortChanged(sortOption))
                    }
                )

                // Property list
                if (state.isListView) {
                    PropertyList(
                        properties = state.filteredProperties,
                        favorites = state.favoritePropertyIds,
                        isLoading = state.isLoadingProperties,
                        onPropertyClick = { property ->
                            onAction(SearchAction.OnPropertyClicked(property))
                        },
                        onFavoriteClick = { propertyId ->
                            onAction(SearchAction.OnFavoriteToggle(propertyId))
                        },
                        onViewDetailsClick = { property ->
                            onAction(SearchAction.OnViewDetailsClick(property))
                        }
                    )
                } else {
                    // Map view placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Map View - Coming Soon")
                    }
                }
            }
        }

        // Navigation Drawer
        BalkanEstateNavigationDrawer(
            isOpen = state.isDrawerOpen,
            selectedItem = DrawerMenuItem.Search,
            onItemClick = { item ->
                onAction(SearchAction.OnDrawerItemClick(item.title))
                onAction(SearchAction.OnCloseDrawer)
            },
            onCloseClick = {
                onAction(SearchAction.OnCloseDrawer)
            }
        )
    }
}

@Composable
private fun SearchTopBar(
    state: SearchState,
    onMenuClick: () -> Unit,
    onFilterClick: () -> Unit,
    onQueryChange: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Hamburger menu
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = MenuHamburgerIcon,
                contentDescription = "Menu",
                tint = Color.DarkGray
            )
        }

        // Search field
        BasicTextField(
            state = state.searchQuery,
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            lineLimits = TextFieldLineLimits.SingleLine,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF1F5F9))
                .border(
                    width = 1.dp,
                    color = if (isFocused) BalkanEstatePrimaryBlue else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .onFocusChanged { isFocused = it.isFocused },
            decorator = { innerBox ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = SaveSearchIcon,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (state.searchQuery.text.isEmpty() && !isFocused) {
                            Text(
                                text = stringResource(R.string.search_bar_hint),
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                        innerBox()
                    }
                }
            }
        )

        // Filter button
        IconButton(onClick = onFilterClick) {
            Icon(
                imageVector = FiltersIcon,
                contentDescription = "Filter",
                tint = Color.DarkGray
            )
        }

        // Profile button
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(BalkanEstatePrimaryBlue)
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = PersonIcon,
                contentDescription = "Profile",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResultsHeader(
    resultsCount: Int,
    sortOption: SortOption,
    onSortChange: (SortOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$resultsCount results found",
            color = BalkanEstatePrimaryBlue,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = sortOption.displayName,
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = KeyboardArrowDownIcon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.DarkGray
                )
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.exposedDropdownSize()
            ) {
                SortOption.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.displayName) },
                        onClick = {
                            onSortChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PropertyList(
    properties: List<BalkanEstateProperty>,
    favorites: Set<String>,
    isLoading: Boolean,
    onPropertyClick: (BalkanEstateProperty) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onViewDetailsClick: (BalkanEstateProperty) -> Unit
) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = if (properties.isEmpty()) MockData.getMockProperties() else properties,
                key = { it.id }
            ) { property ->
                PropertyCard(
                    property = property,
                    isFavorite = favorites.contains(property.id),
                    isNew = true,
                    onPropertyClick = { onPropertyClick(property) },
                    onFavoriteClick = { onFavoriteClick(property.id) },
                    onViewDetailsClick = { onViewDetailsClick(property) }
                )
            }
        }
    }
}

@Composable
private fun BottomActionButtons(
    onFavoriteClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSparkleClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Favorite button
        IconButton(
            onClick = onFavoriteClick,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFF1F5F9))
        ) {
            Icon(
                imageVector = AddedToFavIcon,
                contentDescription = "Favorites",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Notification button
        IconButton(
            onClick = onNotificationClick,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFF1F5F9))
        ) {
            Icon(
                imageVector = NotificationBellIcon,
                contentDescription = "Notifications",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Sparkle/AI button
        IconButton(
            onClick = onSparkleClick,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFF1F5F9))
        ) {
            Icon(
                imageVector = SparkleIcon,
                contentDescription = "AI Features",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchPropertyScreenPreview() {
    BalkanEstateTheme {
        SearchPropertyScreen(
            state = SearchState(
                mapLocation = MapLocation(
                    latitude = 41.3275,
                    longitude = 19.8187,
                    zoom = 12f
                ),
                isMapTypeRoad = true,
                properties = MockData.getMockProperties(),
                filteredProperties = MockData.getMockProperties(),
                selectedBalkanEstateProperty = null,
                favoritePropertyIds = emptySet(),
                searchQuery = SearchState().searchQuery,
                filters = SearchFilters(),
                sortOption = SortOption.NEWEST,
                hasActiveFilter = false,
                isLoadingProperties = false,
                isRefreshing = false,
                isSavingSearch = false,
                isBottomSheetExpanded = false,
                savedSearchCount = 0,
                errorMessage = null,
                isDrawerOpen = false,
                isListView = true,
                subscriptionEmail = ""
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchPropertyScreenWithDrawerPreview() {
    BalkanEstateTheme {
        SearchPropertyScreen(
            state = SearchState(
                mapLocation = MapLocation(
                    latitude = 41.3275,
                    longitude = 19.8187,
                    zoom = 12f
                ),
                isMapTypeRoad = true,
                properties = MockData.getMockProperties(),
                filteredProperties = MockData.getMockProperties(),
                selectedBalkanEstateProperty = null,
                favoritePropertyIds = emptySet(),
                searchQuery = SearchState().searchQuery,
                filters = SearchFilters(),
                sortOption = SortOption.NEWEST,
                hasActiveFilter = false,
                isLoadingProperties = false,
                isRefreshing = false,
                isSavingSearch = false,
                isBottomSheetExpanded = false,
                savedSearchCount = 0,
                errorMessage = null,
                isDrawerOpen = true,
                isListView = true,
                subscriptionEmail = ""
            ),
            onAction = {}
        )
    }
}
