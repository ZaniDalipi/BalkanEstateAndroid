package com.zanoapps.search.presentation.savedsearches

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.AddSearchIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.NotificationBellIcon
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import org.koin.androidx.compose.koinViewModel

@Composable
fun SavedSearchesScreenRoot(
    viewModel: SavedSearchesViewModel = koinViewModel(),
    onNavigateToSearchResults: (String) -> Unit,
    onNavigateToCreateSearch: () -> Unit
) {
    SavedSearchesScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is SavedSearchesAction.OnSearchClick -> onNavigateToSearchResults(action.search.id)
                SavedSearchesAction.OnCreateNewSearch -> onNavigateToCreateSearch()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SavedSearchesScreen(
    state: SavedSearchesState,
    onAction: (SavedSearchesAction) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(SavedSearchesAction.OnCreateNewSearch) },
                containerColor = BalkanEstatePrimaryBlue,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Create new search")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8FAFC))
        ) {
            // Header
            SavedSearchesHeader(count = state.savedSearches.size)

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
                    }
                }
                state.isEmpty -> {
                    EmptySavedSearchesContent(
                        onCreateSearch = { onAction(SavedSearchesAction.OnCreateNewSearch) }
                    )
                }
                else -> {
                    PullToRefreshBox(
                        isRefreshing = state.isRefreshing,
                        onRefresh = { onAction(SavedSearchesAction.RefreshSavedSearches) },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        SavedSearchesList(
                            searches = state.savedSearches,
                            onSearchClick = { onAction(SavedSearchesAction.OnSearchClick(it)) },
                            onDeleteClick = { onAction(SavedSearchesAction.OnDeleteClick(it)) },
                            onToggleNotifications = { onAction(SavedSearchesAction.OnToggleNotifications(it)) }
                        )
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (state.isDeleteDialogVisible && state.selectedSearch != null) {
        AlertDialog(
            onDismissRequest = { onAction(SavedSearchesAction.OnDismissDelete) },
            title = { Text("Delete Search") },
            text = { Text("Are you sure you want to delete \"${state.selectedSearch.name}\"?") },
            confirmButton = {
                TextButton(onClick = { onAction(SavedSearchesAction.OnConfirmDelete) }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(SavedSearchesAction.OnDismissDelete) }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SavedSearchesHeader(count: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Saved Searches",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$count searches saved • Get notified when new listings match",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
private fun EmptySavedSearchesContent(onCreateSearch: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = AddSearchIcon,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = BalkanEstatePrimaryBlue.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No Saved Searches",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Save your search criteria to get\nnotified when new listings match.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SavedSearchesList(
    searches: List<SavedSearch>,
    onSearchClick: (SavedSearch) -> Unit,
    onDeleteClick: (SavedSearch) -> Unit,
    onToggleNotifications: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = searches,
            key = { it.id }
        ) { search ->
            SavedSearchCard(
                search = search,
                onClick = { onSearchClick(search) },
                onDeleteClick = { onDeleteClick(search) },
                onToggleNotifications = { onToggleNotifications(search.id) }
            )
        }
    }
}

@Composable
private fun SavedSearchCard(
    search: SavedSearch,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onToggleNotifications: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(BalkanEstatePrimaryBlue.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = SaveSearchIcon,
                            contentDescription = null,
                            tint = BalkanEstatePrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = search.name,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = search.criteria,
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }

                if (search.newListingsCount > 0) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(BalkanEstatePrimaryBlue)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${search.newListingsCount} new",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = NotificationBellIcon,
                        contentDescription = null,
                        tint = if (search.notificationsEnabled) BalkanEstatePrimaryBlue else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Notifications",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = search.notificationsEnabled,
                        onCheckedChange = { onToggleNotifications() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = BalkanEstatePrimaryBlue
                        )
                    )
                }

                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Gray
                    )
                }
            }

            Text(
                text = "Created ${search.createdAt}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SavedSearchesScreenPreview() {
    BalkanEstateTheme {
        SavedSearchesScreen(
            state = SavedSearchesState(
                savedSearches = listOf(
                    SavedSearch(
                        id = "1",
                        name = "Apartments in Tirana",
                        criteria = "2+ bedrooms • €50k-€150k",
                        newListingsCount = 5,
                        notificationsEnabled = true,
                        createdAt = "2 days ago"
                    )
                ),
                isLoading = false,
                isEmpty = false
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptySavedSearchesScreenPreview() {
    BalkanEstateTheme {
        SavedSearchesScreen(
            state = SavedSearchesState(
                savedSearches = emptyList(),
                isLoading = false,
                isEmpty = true
            ),
            onAction = {}
        )
    }
}
