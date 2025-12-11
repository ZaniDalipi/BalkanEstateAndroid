package com.zanoapps.search.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.domain.enums.SortOption
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.R
import com.zanoapps.core.presentation.designsystem.SortResultsIcon
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateSearchBar
import com.zanoapps.core.presentation.designsystem.components.GradientBackground
import com.zanoapps.core.presentation.designsystem.components.PropertyCard
import com.zanoapps.search.domain.model.MapLocation
import com.zanoapps.search.domain.model.MockData
import com.zanoapps.search.domain.model.SearchFilters
import org.koin.androidx.compose.koinViewModel


@Composable

fun SearchPropertyScreenRot(

    viewModel: SearchPropertyViewModel = koinViewModel()

) {

    SearchPropertyScreen(

        state = viewModel.state,

        onAction = viewModel::onAction

    )

}

@Composable

private fun SearchPropertyScreen(

    state: SearchState,

    onAction: (SearchAction) -> Unit

) {

    GradientBackground {


        Box(modifier = Modifier.fillMaxWidth()) {

            BalkanEstateSearchBar(
                state = state.searchQuery,
                hint = stringResource(R.string.search_bar_hint),
                query = state.searchQuery.text.toString(),
                onQueryChange = { query ->
                    onAction(SearchAction.OnSearchQueryChanged(query))

                },
                onFilterClick = {
                    onAction(SearchAction.OnFilterClick)

                },
                hasActiveFilters = state.hasActiveFilter,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopCenter)
            )
        }

        PropertyListBottomSheet(
            properties = state.filteredProperties,
            favorites = state.favoritePropertyIds,
            sortOption = state.sortOption,
            isLoading = state.isLoadingProperties,
            onPropertyClick = { property ->
                onAction(SearchAction.OnPropertyClicked(property))
            },
            onFavoriteClick = { propertyId ->
                onAction(SearchAction.OnFavoriteToggle(propertyId))
            },
            onSortChange = { sortOption ->
                onAction(SearchAction.OnSortChanged(sortOption))
            }
        )
    }
}


@Preview
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
                selectedBalkanEstateProperty = MockData.getMockProperties()[2],
                favoritePropertyIds = emptySet(),
                searchQuery = SearchState().searchQuery,
                filters = SearchFilters(),
                sortOption = SortOption.FEATURED,
                hasActiveFilter = false,
                isLoadingProperties = false,
                isRefreshing = false,
                isSavingSearch = false,
                isBottomSheetExpanded = false,
                savedSearchCount = 1,
                errorMessage = null
            ),
            onAction = {

            }

        )
    }

}


@Composable
private fun PropertyListBottomSheet(
    properties: List<BalkanEstateProperty>,
    favorites: Set<String>,
    sortOption: SortOption,
    isLoading: Boolean,
    onPropertyClick: (BalkanEstateProperty) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onSortChange: (SortOption) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Properties in Area",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${properties.size} properties found",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(16.dp))

                SortDropdown(
                    currentSort = sortOption,
                    onSortChange = onSortChange
                )
            }

        }

        Spacer(Modifier.height(16.dp))

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(MockData.getMockProperties()) { property ->

                    PropertyCard(
                        property = property,
                        isFavorite = favorites.contains(property.id),
                        onPropertyClick = { onPropertyClick(property) },
                        onFavoriteClick = { onFavoriteClick(property.id) },

                        )
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
        // FIX: Remove the 'Ex' argument from Modifier.menuAnchor().
        // It should be called with no arguments when inside ExposedDropdownMenuBox.
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable,enabled = true, )
        ) {
            Icon(SortResultsIcon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                currentSort.displayName,
                fontSize = 12.sp
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            // Pass the scope's Modifier to the menu to ensure correct sizing and positioning
            modifier = Modifier.exposedDropdownSize()
        ) {
            SortOption.entries.forEach { sortOption ->
                DropdownMenuItem(
                    text = { Text(sortOption.displayName) },
                    onClick = {
                        onSortChange(sortOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

