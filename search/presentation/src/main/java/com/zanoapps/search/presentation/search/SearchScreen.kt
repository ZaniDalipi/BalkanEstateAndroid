package com.zanoapps.search.presentation.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.domain.enums.SortOption
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.SortResultsIcon

import com.zanoapps.core.presentation.designsystem.components.BalkanEstateSearchBar
import com.zanoapps.core.presentation.designsystem.components.GradientBackground
import com.zanoapps.core.presentation.designsystem.components.PropertyCard
import com.zanoapps.search.domain.model.MapLocation
import com.zanoapps.search.domain.model.MockData
import com.zanoapps.search.domain.model.SearchFilters
import com.zanoapps.search.presentation.R
import com.zanoapps.search.presentation.search.components.DrawerContent
import com.zanoapps.search.presentation.search.components.MapViewPlaceholder
import com.zanoapps.search.presentation.search.components.NewsletterSection
import com.zanoapps.search.presentation.search.components.ViewModeToggle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreenRoot(
    viewModel: SearchViewModel = koinViewModel(),
    onNavigateToPropertyDetails: (String) -> Unit,
    onNavigateToFilters: () -> Unit,
    onNavigateToSavedSearches: () -> Unit,
    onNavigateToCreateListing: () -> Unit,
    onNavigateToSavedProperties: () -> Unit,
    onNavigateToTopAgents: () -> Unit,
    onNavigateToAgencies: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    onNavigateToInbox: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onLoginRequired: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        viewModel.events.collect { event ->
            when (event) {
                is SearchEvent.NavigateToPropertyDetails -> onNavigateToPropertyDetails(event.propertyId)
                SearchEvent.NavigateToFilters -> onNavigateToFilters()
                SearchEvent.NavigateToSavedSearches -> onNavigateToSavedSearches()
                SearchEvent.NavigateToCreateListing -> onNavigateToCreateListing()
                SearchEvent.NavigateToSavedProperties -> onNavigateToSavedProperties()
                SearchEvent.NavigateToTopAgents -> onNavigateToTopAgents()
                SearchEvent.NavigateToAgencies -> onNavigateToAgencies()
                SearchEvent.NavigateToSubscription -> onNavigateToSubscription()
                SearchEvent.NavigateToInbox -> onNavigateToInbox()
                SearchEvent.NavigateToProfile -> onNavigateToProfile()
                SearchEvent.LoginRequired -> onLoginRequired()
                is SearchEvent.Error -> { /* Handle error toast */ }
                is SearchEvent.Success -> { /* Handle success toast */ }
                SearchEvent.SearchSaved -> { /* Handle search saved toast */ }
                SearchEvent.SearchDeleted -> { /* Handle search deleted toast */ }
                SearchEvent.SubscribedToNewsletter -> { /* Handle newsletter subscription toast */ }
            }
        }
    }
    
    SearchScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreen(
    state: SearchState,
    onAction: (SearchAction) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(state.isMenuOpen) {
        if (state.isMenuOpen) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }
    
    LaunchedEffect(drawerState.isClosed) {
        if (drawerState.isClosed && state.isMenuOpen) {
            onAction(SearchAction.OnCloseMenu)
        }
    }
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                isLoggedIn = state.isLoggedIn,
                onSearchClick = {
                    scope.launch { drawerState.close() }
                    onAction(SearchAction.OnCloseMenu)
                },
                onSavedSearchesClick = {
                    scope.launch { drawerState.close() }
                    onAction(SearchAction.OnViewSavedSearches)
                },
                onSavedPropertiesClick = {
                    scope.launch { drawerState.close() }
                    onAction(SearchAction.OnSavedPropertiesClick)
                },
                onTopAgentsClick = {
                    scope.launch { drawerState.close() }
                    onAction(SearchAction.OnTopAgentsClick)
                },
                onAgenciesClick = {
                    scope.launch { drawerState.close() }
                    onAction(SearchAction.OnAgenciesClick)
                },
                onNewListingClick = {
                    scope.launch { drawerState.close() }
                    onAction(SearchAction.OnCreateListingClick)
                },
                onSubscriptionClick = {
                    scope.launch { drawerState.close() }
                    onAction(SearchAction.OnSubscriptionClick)
                },
                onInboxClick = {
                    scope.launch { drawerState.close() }
                    onAction(SearchAction.OnInboxClick)
                },
                onProfileClick = {
                    scope.launch { drawerState.close() }
                    onAction(SearchAction.OnProfileClick)
                },
                onCloseClick = {
                    scope.launch { drawerState.close() }
                    onAction(SearchAction.OnCloseMenu)
                }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Main Content
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Search Bar
                SearchTopBar(
                    searchQuery = state.searchQuery,
                    hasActiveFilters = state.hasActiveFilters,
                    onMenuClick = { onAction(SearchAction.OnMenuClick) },
                    onQueryChange = { onAction(SearchAction.OnSearchQueryChanged(it)) },
                    onFilterClick = { onAction(SearchAction.OnFilterClick) },
                    onProfileClick = { onAction(SearchAction.OnProfileClick) }
                )
                
                // Map or List View
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    when (state.viewMode) {
                        ViewMode.MAP -> {
                            MapViewPlaceholder(
                                properties = state.filteredProperties,
                                mapLocation = state.mapLocation,
                                isDrawModeEnabled = state.isDrawModeEnabled,
                                onMarkerClick = { onAction(SearchAction.OnMarkerClicked(it)) },
                                onMapMove = { onAction(SearchAction.OnMapMoved(it)) },
                                onDrawModeToggle = { onAction(SearchAction.OnDrawModeToggle) }
                            )
                        }
                        ViewMode.LIST -> {
                            PropertyListView(
                                properties = state.filteredProperties,
                                favorites = state.favoritePropertyIds,
                                sortOption = state.sortOption,
                                isLoading = state.isLoadingProperties,
                                resultsCount = state.resultsCount,
                                onPropertyClick = { onAction(SearchAction.OnPropertyClicked(it)) },
                                onFavoriteClick = { onAction(SearchAction.OnFavoriteToggle(it)) },
                                onSortChange = { onAction(SearchAction.OnSortChanged(it)) }
                            )
                        }
                    }
                    
                    // Floating Action Buttons
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // View Mode Toggle
                        ViewModeToggle(
                            currentMode = state.viewMode,
                            onToggle = { onAction(SearchAction.OnToggleViewMode) }
                        )
                    }
                }
                
                // Newsletter Section
                NewsletterSection(
                    email = state.newsletterEmail,
                    isSubscribing = state.isSubscribing,
                    onSubscribe = { onAction(SearchAction.OnSubscribeNewsletter(it)) }
                )
            }
        }
    }
}

@Composable
private fun SearchTopBar(
    searchQuery: androidx.compose.foundation.text.input.TextFieldState,
    hasActiveFilters: Boolean,
    onMenuClick: () -> Unit,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Menu Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { onMenuClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = SortResultsIcon,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Search Bar
            BalkanEstateSearchBar(
                state = searchQuery,
                hint = stringResource(R.string.search_hint),
                query = searchQuery.text.toString(),
                onQueryChange = onQueryChange,
                onFilterClick = onFilterClick,
                hasActiveFilters = hasActiveFilters,
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Profile Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = PersonIcon,
                    contentDescription = "Profile",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PropertyListView(
    properties: List<BalkanEstateProperty>,
    favorites: Set<String>,
    sortOption: SortOption,
    isLoading: Boolean,
    resultsCount: Int,
    onPropertyClick: (BalkanEstateProperty) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onSortChange: (SortOption) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Results Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$resultsCount results found",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
            
            SortDropdown(
                currentSort = sortOption,
                onSortChange = onSortChange
            )
        }
        
        // Property List
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = properties,
                    key = { it.id }
                ) { property ->
                    PropertyCard(
                        property = property,
                        isFavorite = favorites.contains(property.id),
                        onPropertyClick = { onPropertyClick(property) },
                        onFavoriteClick = { onFavoriteClick(property.id) }
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortDropdown(
    currentSort: SortOption,
    onSortChange: (SortOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.menuAnchor(),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = SortResultsIcon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = currentSort.displayName,
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
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

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    BalkanEstateTheme {
        SearchScreen(
            state = SearchState(
                mapLocation = MapLocation(
                    latitude = 41.3275,
                    longitude = 19.8187,
                    zoom = 12f
                ),
                viewMode = ViewMode.LIST,
                properties = MockData.getMockProperties(),
                filteredProperties = MockData.getMockProperties(),
                favoritePropertyIds = setOf("1", "3"),
                filters = SearchFilters(),
                sortOption = SortOption.NEWEST,
                hasActiveFilters = false,
                isLoadingProperties = false,
                resultsCount = 5
            ),
            onAction = {}
        )
    }
}
