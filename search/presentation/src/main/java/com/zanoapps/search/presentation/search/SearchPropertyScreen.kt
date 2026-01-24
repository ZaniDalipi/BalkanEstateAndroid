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
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.FiltersIcon
import com.zanoapps.core.presentation.designsystem.KeyboardArrowDownIcon
import com.zanoapps.core.presentation.designsystem.MenuHamburgerIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.R
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateNavigationDrawer
import com.zanoapps.core.presentation.designsystem.components.DrawerMenuItem
import com.zanoapps.core.presentation.designsystem.components.ListMapToggle
import com.zanoapps.core.presentation.designsystem.components.PropertyCard
import com.zanoapps.search.domain.model.MapLocation
import com.zanoapps.search.domain.model.MockData
import com.zanoapps.search.domain.model.SearchFilters
import org.koin.androidx.compose.koinViewModel


/**
 * Callback interface for navigation from Search screen
 */
interface SearchNavigationCallback {
    fun onNavigateToSavedSearches()
    fun onNavigateToSavedProperties()
    fun onNavigateToTopAgents()
    fun onNavigateToAgencies()
    fun onNavigateToNewListing()
    fun onNavigateToSubscription()
    fun onNavigateToInbox()
    fun onNavigateToProfile()
    fun onNavigateToFavorites()
    fun onNavigateToNotifications()
    fun onNavigateToPropertyDetails(propertyId: String)
    fun onLogout()
}

/**
 * Root composable for the Search Property screen
 * @param viewModel The ViewModel for managing state
 * @param navigationCallback Callback for navigation events
 * @param showDrawer Whether to show the navigation drawer (typically for tablet mode)
 */
@Composable
fun SearchPropertyScreenRoot(
    viewModel: SearchPropertyViewModel = koinViewModel(),
    navigationCallback: SearchNavigationCallback? = null,
    showDrawer: Boolean = false
) {
    SearchPropertyScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is SearchAction.OnViewDetailsClick -> {
                    navigationCallback?.onNavigateToPropertyDetails(action.property.id)
                }
                is SearchAction.OnPropertyClicked -> {
                    navigationCallback?.onNavigateToPropertyDetails(action.balkanEstateProperty.id)
                }
                else -> viewModel.onAction(action)
            }
        },
        navigationCallback = navigationCallback,
        showDrawer = showDrawer
    )
}

// Keep the old name for backward compatibility
@Composable
fun SearchPropertyScreenRot(
    viewModel: SearchPropertyViewModel = koinViewModel()
) {
    SearchPropertyScreenRoot(viewModel)
}

/**
 * Main Search Property screen content
 * For mobile: No drawer, uses bottom navigation from parent scaffold
 * For tablet: Shows drawer for additional navigation options
 */
@Composable
private fun SearchPropertyScreen(
    state: SearchState,
    onAction: (SearchAction) -> Unit,
    navigationCallback: SearchNavigationCallback? = null,
    showDrawer: Boolean = false
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
        ) {
            // Top Bar
            SearchTopBar(
                state = state,
                showMenuButton = showDrawer, // Only show menu button when drawer is available
                onMenuClick = { onAction(SearchAction.OnOpenDrawer) },
                onFilterClick = { onAction(SearchAction.OnFilterClick) },
                onQueryChange = { query -> onAction(SearchAction.OnSearchQueryChanged(query)) },
                onProfileClick = { navigationCallback?.onNavigateToProfile() }
            )

            // Results count and sort
            ResultsHeader(
                resultsCount = state.filteredProperties.size.takeIf { it > 0 } ?: MockData.getMockProperties().size,
                sortOption = state.sortOption,
                onSortChange = { sortOption ->
                    onAction(SearchAction.OnSortChanged(sortOption))
                }
            )

            // List/Map Toggle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                ListMapToggle(
                    isListView = state.isListView,
                    onToggle = { isListView ->
                        onAction(SearchAction.OnViewModeToggle(isListView))
                    }
                )
            }

            // Property list or map
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
                    },
                    modifier = Modifier.weight(1f)
                )
            } else {
                // Map view placeholder
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Map View - Coming Soon")
                }
            }
        }

        // Navigation Drawer - Only shown for tablet mode
        if (showDrawer) {
            BalkanEstateNavigationDrawer(
                isOpen = state.isDrawerOpen,
                selectedItem = DrawerMenuItem.Search,
                isLoggedIn = true,
                userName = "User",
                onItemClick = { item ->
                    onAction(SearchAction.OnDrawerItemClick(item.title))
                    onAction(SearchAction.OnCloseDrawer)
                    // Handle navigation based on drawer item
                    when (item) {
                        DrawerMenuItem.Search -> { /* Already on search */ }
                        DrawerMenuItem.SavedSearches -> navigationCallback?.onNavigateToSavedSearches()
                        DrawerMenuItem.SavedProperties -> navigationCallback?.onNavigateToSavedProperties()
                        DrawerMenuItem.TopAgents -> navigationCallback?.onNavigateToTopAgents()
                        DrawerMenuItem.Agencies -> navigationCallback?.onNavigateToAgencies()
                        DrawerMenuItem.NewListing -> navigationCallback?.onNavigateToNewListing()
                        DrawerMenuItem.Subscription -> navigationCallback?.onNavigateToSubscription()
                        DrawerMenuItem.Inbox -> navigationCallback?.onNavigateToInbox()
                        DrawerMenuItem.MyAccount -> navigationCallback?.onNavigateToProfile()
                        DrawerMenuItem.Logout -> navigationCallback?.onLogout()
                    }
                },
                onCloseClick = {
                    onAction(SearchAction.OnCloseDrawer)
                }
            )
        }
    }
}

@Composable
private fun SearchTopBar(
    state: SearchState,
    showMenuButton: Boolean = false,
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
            .padding(horizontal = if (showMenuButton) 12.dp else 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Hamburger menu - only shown for tablet mode with drawer
        if (showMenuButton) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = MenuHamburgerIcon,
                    contentDescription = "Menu",
                    tint = Color.DarkGray
                )
            }
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
    onViewDetailsClick: (BalkanEstateProperty) -> Unit,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        Box(
            modifier = modifier
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
            modifier = modifier.fillMaxWidth()
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
            onAction = {},
            showDrawer = false
        )
    }
}

@Preview(showBackground = true, name = "Tablet with Drawer")
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
                isLoadingProperties = true,
                isRefreshing = false,
                isSavingSearch = false,
                isBottomSheetExpanded = false,
                savedSearchCount = 2,
                errorMessage = null,
                isDrawerOpen = true,
                isListView = true,
                subscriptionEmail = ""
            ),
            onAction = {},
            showDrawer = true
        )
    }
}
