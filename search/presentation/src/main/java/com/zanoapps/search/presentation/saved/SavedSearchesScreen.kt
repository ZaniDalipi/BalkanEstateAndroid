package com.zanoapps.search.presentation.saved

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.AddSearchIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.CrossIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.NotificationBellIcon
import org.koin.androidx.compose.koinViewModel

@Composable
fun SavedSearchesScreenRoot(
    viewModel: SavedSearchesViewModel = koinViewModel()
) {
    SavedSearchesScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun SavedSearchesScreen(
    state: SavedSearchesState,
    onAction: (SavedSearchesAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
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
                color = Color.DarkGray
            )
            Text(
                text = "Get notified when new properties match your criteria",
                style = MaterialTheme.typography.bodyMedium,
                color = BalkanEstateGray
            )
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
            }
        } else if (state.savedSearches.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(AddSearchIcon, null, Modifier.size(64.dp), tint = BalkanEstatePrimaryBlue)
                Spacer(Modifier.height(16.dp))
                Text("No Saved Searches", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                Spacer(Modifier.height(8.dp))
                Text("Save your search criteria to get\nnotified about new properties.", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) {
                items(items = state.savedSearches, key = { it.id }) { search ->
                    SavedSearchCard(search = search, onAction = onAction)
                }
            }
        }
    }
}

@Composable
private fun SavedSearchCard(
    search: SavedSearchItem,
    onAction: (SavedSearchesAction) -> Unit
) {
    Card(
        onClick = { onAction(SavedSearchesAction.OnSearchClick(search)) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(search.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                    if (search.newCount > 0) {
                        Spacer(Modifier.width(8.dp))
                        Badge(containerColor = BalkanEstateOrange, contentColor = Color.White) {
                            Text("${search.newCount} new")
                        }
                    }
                }
                IconButton(onClick = { onAction(SavedSearchesAction.OnDeleteSearch(search.id)) }, modifier = Modifier.size(24.dp)) {
                    Icon(CrossIcon, "Delete", tint = BalkanEstateRed, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(LocationIcon, null, tint = BalkanEstateGray, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text(search.location, fontSize = 13.sp, color = BalkanEstateGray)
            }
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SearchDetail("Type", search.propertyType)
                SearchDetail("Price", search.priceRange)
                SearchDetail("Beds", search.bedrooms)
            }
            Spacer(Modifier.height(8.dp))
            Text("${search.matchCount} matching properties", fontSize = 13.sp, color = BalkanEstatePrimaryBlue, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(NotificationBellIcon, null, tint = BalkanEstateGray, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Notifications", fontSize = 13.sp, color = BalkanEstateGray)
                }
                Switch(
                    checked = search.notificationsEnabled,
                    onCheckedChange = { onAction(SavedSearchesAction.OnToggleNotifications(search.id)) },
                    colors = SwitchDefaults.colors(checkedTrackColor = BalkanEstatePrimaryBlue)
                )
            }
            Text("Created ${search.createdAt}", fontSize = 11.sp, color = BalkanEstateGray)
        }
    }
}

@Composable
private fun SearchDetail(label: String, value: String) {
    Column {
        Text(label, fontSize = 11.sp, color = BalkanEstateGray)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
    }
}
