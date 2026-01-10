package com.zanoapps.favourites.presentation

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.GridViewIcon
import com.zanoapps.core.presentation.designsystem.ListViewIcon
import com.zanoapps.core.presentation.designsystem.SavedHomesIcon
import com.zanoapps.core.presentation.designsystem.components.PropertyCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavouritesScreenRoot(
    viewModel: FavouritesViewModel = koinViewModel(),
    onNavigateToPropertyDetails: (String) -> Unit
) {
    FavouritesScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is FavouritesAction.OnViewDetails -> onNavigateToPropertyDetails(action.property.id)
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavouritesScreen(
    state: FavouritesState,
    onAction: (FavouritesAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        FavouritesHeader(
            count = state.favouriteProperties.size,
            isGridView = state.isGridView,
            onToggleView = { onAction(FavouritesAction.OnToggleViewMode) },
            onClearAll = { onAction(FavouritesAction.OnClearAll) }
        )

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
                EmptyFavouritesContent()
            }
            else -> {
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = { onAction(FavouritesAction.RefreshFavourites) },
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (state.isGridView) {
                        FavouritesGrid(
                            properties = state.favouriteProperties,
                            onPropertyClick = { onAction(FavouritesAction.OnViewDetails(it)) },
                            onRemoveFavourite = { onAction(FavouritesAction.OnRemoveFavourite(it)) }
                        )
                    } else {
                        FavouritesList(
                            properties = state.favouriteProperties,
                            onPropertyClick = { onAction(FavouritesAction.OnViewDetails(it)) },
                            onRemoveFavourite = { onAction(FavouritesAction.OnRemoveFavourite(it)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavouritesHeader(
    count: Int,
    isGridView: Boolean,
    onToggleView: () -> Unit,
    onClearAll: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Saved Properties",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$count properties saved",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (count > 0) {
                    TextButton(onClick = onClearAll) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear all",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Clear all",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }

                IconButton(onClick = onToggleView) {
                    Icon(
                        imageVector = if (isGridView) ListViewIcon else GridViewIcon,
                        contentDescription = if (isGridView) "List view" else "Grid view",
                        tint = BalkanEstatePrimaryBlue
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyFavouritesContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = SavedHomesIcon,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = BalkanEstatePrimaryBlue.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No Saved Properties",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Properties you save will appear here.\nTap the heart icon on any property to save it.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FavouritesList(
    properties: List<BalkanEstateProperty>,
    onPropertyClick: (BalkanEstateProperty) -> Unit,
    onRemoveFavourite: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = properties,
            key = { it.id }
        ) { property ->
            PropertyCard(
                property = property,
                isFavorite = true,
                isNew = false,
                onPropertyClick = { onPropertyClick(property) },
                onFavoriteClick = { onRemoveFavourite(property.id) },
                onViewDetailsClick = { onPropertyClick(property) }
            )
        }
    }
}

@Composable
private fun FavouritesGrid(
    properties: List<BalkanEstateProperty>,
    onPropertyClick: (BalkanEstateProperty) -> Unit,
    onRemoveFavourite: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = properties,
            key = { it.id }
        ) { property ->
            PropertyCard(
                property = property,
                isFavorite = true,
                isNew = false,
                onPropertyClick = { onPropertyClick(property) },
                onFavoriteClick = { onRemoveFavourite(property.id) },
                onViewDetailsClick = { onPropertyClick(property) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavouritesScreenPreview() {
    BalkanEstateTheme {
        FavouritesScreen(
            state = FavouritesState(
                favouriteProperties = listOf(
                    BalkanEstateProperty(
                        id = "1",
                        title = "Modern Apartment",
                        price = 185000.0,
                        currency = "EUR",
                        imageUrl = "https://example.com/image.jpg",
                        bedrooms = 3,
                        bathrooms = 2,
                        squareFootage = 120,
                        address = "123 Main St",
                        city = "Tirana",
                        country = "Albania",
                        latitude = 41.3275,
                        longitude = 19.8187,
                        propertyType = "Apartment",
                        listingType = "Sale",
                        agentName = "Agent",
                        isFeatured = false,
                        isUrgent = false
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
private fun EmptyFavouritesScreenPreview() {
    BalkanEstateTheme {
        FavouritesScreen(
            state = FavouritesState(
                favouriteProperties = emptyList(),
                isLoading = false,
                isEmpty = true
            ),
            onAction = {}
        )
    }
}
