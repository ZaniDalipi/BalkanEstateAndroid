package com.zanoapps.favourites.presentation.favourites

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.CompareIcon
import com.zanoapps.core.presentation.designsystem.KeyboardArrowDownIcon
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import com.zanoapps.core.presentation.designsystem.SavedHomesIcon
import com.zanoapps.core.presentation.designsystem.components.PropertyCard
import androidx.compose.ui.tooling.preview.Preview
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavouritesScreenRoot(
    viewModel: FavouritesViewModel = koinViewModel(),
    onNavigateToPropertyDetail: (String) -> Unit
) {
    FavouritesScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is FavouritesAction.OnPropertyClick -> onNavigateToPropertyDetail(action.property.id)
                is FavouritesAction.OnViewDetails -> onNavigateToPropertyDetail(action.property.id)
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Saved Properties",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                OutlinedButton(
                    onClick = { onAction(FavouritesAction.OnToggleCompareMode) },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = CompareIcon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (state.isCompareMode) "Cancel" else "Compare",
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { onAction(FavouritesAction.OnSearchQueryChanged(it)) },
                placeholder = { Text("Search saved properties...") },
                leadingIcon = {
                    Icon(
                        imageVector = SaveSearchIcon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BalkanEstatePrimaryBlue,
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sort and count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${state.filteredProperties.size} saved",
                    color = BalkanEstatePrimaryBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.menuAnchor(
                            ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = state.selectedSortOption.displayName,
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
                        onDismissRequest = { expanded = false }
                    ) {
                        FavouritesSortOption.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.displayName) },
                                onClick = {
                                    onAction(FavouritesAction.OnSortChanged(option))
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Compare action bar
        AnimatedVisibility(visible = state.isCompareMode && state.selectedForCompare.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BalkanEstatePrimaryBlue)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${state.selectedForCompare.size} selected (max 3)",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
                Button(
                    onClick = { onAction(FavouritesAction.OnCompareSelected) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    enabled = state.selectedForCompare.size >= 2
                ) {
                    Text(
                        text = "Compare Now",
                        color = BalkanEstatePrimaryBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Content
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
            }
        } else if (state.filteredProperties.isEmpty()) {
            // Empty state
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
                    modifier = Modifier.size(64.dp),
                    tint = BalkanEstatePrimaryBlue
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No Saved Properties",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Start browsing and save properties you like.\nThey'll appear here for easy access.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = state.filteredProperties,
                    key = { it.id }
                ) { property ->
                    Box {
                        PropertyCard(
                            property = property,
                            isFavorite = true,
                            isNew = false,
                            onPropertyClick = { onAction(FavouritesAction.OnPropertyClick(property)) },
                            onFavoriteClick = { onAction(FavouritesAction.OnRemoveFavourite(property.id)) },
                            onViewDetailsClick = { onAction(FavouritesAction.OnViewDetails(property)) }
                        )

                        if (state.isCompareMode) {
                            Checkbox(
                                checked = state.selectedForCompare.contains(property.id),
                                onCheckedChange = {
                                    onAction(FavouritesAction.OnToggleCompareSelection(property.id))
                                },
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(8.dp),
                                colors = CheckboxDefaults.colors(
                                    checkedColor = BalkanEstatePrimaryBlue
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun FavouritesScreenPreview() {
    BalkanEstateTheme {
        FavouritesScreen(
            state = FavouritesState(),
            onAction = {}
        )
    }
}
