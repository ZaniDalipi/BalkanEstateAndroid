package com.zanoapps.search.presentation.search

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.Poppins
import com.zanoapps.core.presentation.designsystem.SearchIcon

data class SavedSearch(
    val id: String,
    val name: String,
    val location: String,
    val propertyType: String?,
    val priceRange: String?,
    val bedrooms: Int?,
    val filters: List<String>,
    val matchingPropertiesCount: Int,
    val alertsEnabled: Boolean,
    val createdAt: String
)

@Composable
fun SavedSearchesScreenRoot(
    savedSearches: List<SavedSearch>,
    onSearchClick: (SavedSearch) -> Unit,
    onDeleteClick: (SavedSearch) -> Unit,
    onToggleAlerts: (SavedSearch, Boolean) -> Unit,
    onCreateSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SavedSearchesScreen(
        savedSearches = savedSearches,
        onSearchClick = onSearchClick,
        onDeleteClick = onDeleteClick,
        onToggleAlerts = onToggleAlerts,
        onCreateSearchClick = onCreateSearchClick,
        modifier = modifier
    )
}

@Composable
fun SavedSearchesScreen(
    savedSearches: List<SavedSearch>,
    onSearchClick: (SavedSearch) -> Unit,
    onDeleteClick: (SavedSearch) -> Unit,
    onToggleAlerts: (SavedSearch, Boolean) -> Unit,
    onCreateSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "Saved Searches",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        fontFamily = Poppins
                    )
                    Text(
                        text = "${savedSearches.size} saved searches",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            if (savedSearches.isEmpty()) {
                // Empty State
                EmptySavedSearchesContent(
                    onCreateSearchClick = onCreateSearchClick,
                    modifier = Modifier.weight(1f)
                )
            } else {
                // Saved Searches List
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = savedSearches,
                        key = { it.id }
                    ) { search ->
                        SavedSearchCard(
                            savedSearch = search,
                            onClick = { onSearchClick(search) },
                            onDeleteClick = { onDeleteClick(search) },
                            onToggleAlerts = { enabled -> onToggleAlerts(search, enabled) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedSearchCard(
    savedSearch: SavedSearch,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onToggleAlerts: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
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
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = savedSearch.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = LocationIcon,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = savedSearch.location,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filters
            if (savedSearch.filters.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(savedSearch.filters) { filter ->
                        FilterChip(text = filter)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Matching properties count
            Box(
                modifier = Modifier
                    .background(
                        BalkanEstatePrimaryBlue.copy(alpha = 0.1f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "${savedSearch.matchingPropertiesCount} matching properties",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = BalkanEstatePrimaryBlue
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Alerts Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = if (savedSearch.alertsEnabled) BalkanEstatePrimaryBlue else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Email alerts",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }

                Switch(
                    checked = savedSearch.alertsEnabled,
                    onCheckedChange = onToggleAlerts,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = BalkanEstatePrimaryBlue,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.LightGray
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Created ${savedSearch.createdAt}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun FilterChip(
    text: String
) {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFF0F0F0),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun EmptySavedSearchesContent(
    onCreateSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = SearchIcon,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No saved searches yet",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Save your search criteria to quickly find properties matching your preferences.",
            fontSize = 14.sp,
            color = Color.Gray,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onCreateSearchClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = BalkanEstatePrimaryBlue
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Start Searching",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

// Mock data
object SavedSearchesMockData {
    val mockSavedSearches = listOf(
        SavedSearch(
            id = "1",
            name = "Tirana Apartments",
            location = "Tirana, Albania",
            propertyType = "Apartment",
            priceRange = "€100,000 - €200,000",
            bedrooms = 2,
            filters = listOf("Apartment", "2+ beds", "€100k-200k", "Parking"),
            matchingPropertiesCount = 45,
            alertsEnabled = true,
            createdAt = "2 days ago"
        ),
        SavedSearch(
            id = "2",
            name = "Belgrade Houses",
            location = "Belgrade, Serbia",
            propertyType = "House",
            priceRange = "€150,000 - €300,000",
            bedrooms = 3,
            filters = listOf("House", "3+ beds", "Garden"),
            matchingPropertiesCount = 28,
            alertsEnabled = true,
            createdAt = "1 week ago"
        ),
        SavedSearch(
            id = "3",
            name = "Sofia Investment",
            location = "Sofia, Bulgaria",
            propertyType = null,
            priceRange = "€50,000 - €100,000",
            bedrooms = null,
            filters = listOf("Any type", "€50k-100k"),
            matchingPropertiesCount = 72,
            alertsEnabled = false,
            createdAt = "2 weeks ago"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun SavedSearchesScreenPreview() {
    BalkanEstateTheme {
        SavedSearchesScreen(
            savedSearches = SavedSearchesMockData.mockSavedSearches,
            onSearchClick = {},
            onDeleteClick = {},
            onToggleAlerts = { _, _ -> },
            onCreateSearchClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SavedSearchesScreenEmptyPreview() {
    BalkanEstateTheme {
        SavedSearchesScreen(
            savedSearches = emptyList(),
            onSearchClick = {},
            onDeleteClick = {},
            onToggleAlerts = { _, _ -> },
            onCreateSearchClick = {}
        )
    }
}
