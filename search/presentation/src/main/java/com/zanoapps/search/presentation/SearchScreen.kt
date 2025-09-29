//package com.zanoapps.search.presentation
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import kotlinx.coroutines.flow.collectLatest
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PropertyMapScreen(
//   // viewModel: PropertyMapViewModel = hiltViewModel(),
//    onPropertyClick: (Property) -> Unit = {},
//    onNavigateBack: () -> Unit = {}
//) {
//    val state by viewModel.state.collectAsStateWithLifecycle()
//    val context = LocalContext.current
//    val bottomSheetState = rememberBottomSheetScaffoldState()
//
//    // Handle events
//    LaunchedEffect(viewModel.events) {
//        viewModel.events.collectLatest { event ->
//            when (event) {
//                is PropertyMapContract.Event.ShowError -> {
//                    // Show snackbar or toast
//                }
//                is PropertyMapContract.Event.NavigateToPropertyDetail -> {
//                    onPropertyClick(event.property)
//                }
//                is PropertyMapContract.Event.SaveSearchSuccess -> {
//                    // Show success message
//                }
//            }
//        }
//    }
//
//    // Load properties on first composition
//    LaunchedEffect(Unit) {
//        viewModel.handleAction(PropertyMapContract.Action.LoadProperties)
//    }
//
//    BottomSheetScaffold(
//        scaffoldState = bottomSheetState,
//        sheetPeekHeight = 120.dp,
//        sheetContent = {
//            PropertyListBottomSheet(
//                properties = state.filteredProperties,
//                searchQuery = state.searchQuery,
//                sortBy = state.sortBy,
//                favorites = state.favorites,
//                isLoading = state.isLoading,
//                onSearchQueryChange = { query ->
//                    viewModel.handleAction(PropertyMapContract.Action.SearchProperties(query))
//                },
//                onSortChange = { sortBy ->
//                    viewModel.handleAction(PropertyMapContract.Action.SortProperties(sortBy))
//                },
//                onPropertyClick = { property ->
//                    viewModel.handleAction(PropertyMapContract.Action.SelectProperty(property))
//                },
//                onFavoriteClick = { propertyId ->
//                    viewModel.handleAction(PropertyMapContract.Action.ToggleFavorite(propertyId))
//                },
//                modifier = Modifier.fillMaxSize()
//            )
//        },
//        modifier = Modifier.fillMaxSize()
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            // Google Maps
//            GoogleMap(
//                modifier = Modifier.fillMaxSize(),
//                cameraPositionState = rememberCameraPositionState {
//                    position = CameraPosition.fromLatLngZoom(
//                        LatLng(state.mapCenter.latitude, state.mapCenter.longitude),
//                        10f
//                    )
//                },
//                onMapClick = { latLng ->
//                    viewModel.handleAction(
//                        PropertyMapContract.Action.UpdateMapCenter(
//                            PropertyMapContract.LatLng(latLng.latitude, latLng.longitude)
//                        )
//                    )
//                }
//            ) {
//                // Property markers
//                state.properties.forEach { property ->
//                    Marker(
//                        state = MarkerState(
//                            position = LatLng(property.latitude, property.longitude)
//                        ),
//                        title = property.title,
//                        snippet = "${property.currency}${property.price.toInt()}",
//                        onClick = {
//                            viewModel.handleAction(PropertyMapContract.Action.SelectProperty(property))
//                            true
//                        }
//                    )
//                }
//            }
//
//            // Search bar overlay
//            SearchBarOverlay(
//                query = state.searchQuery,
//                onQueryChange = { query ->
//                    viewModel.handleAction(PropertyMapContract.Action.SearchProperties(query))
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//                    .align(Alignment.TopCenter)
//            )
//
//            // Map controls
//            Row(
//                modifier = Modifier
//                    .align(Alignment.BottomStart)
//                    .padding(16.dp),
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                MapControlButton(
//                    icon = Icons.Default.Map,
//                    onClick = { /* Switch map type */ }
//                )
//
//                MapControlButton(
//                    icon = Icons.Default.Edit,
//                    onClick = { /* Draw on map */ }
//                )
//
//                MapControlButton(
//                    icon = Icons.Default.LocationOn,
//                    onClick = { /* Current location */ }
//                )
//            }
//
//            // Save search button
//            ExtendedFloatingActionButton(
//                onClick = {
//                    // Save search functionality
//                    viewModel.handleAction(PropertyMapContract.Action.LoadProperties)
//                },
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//                    .padding(16.dp),
//                containerColor = MaterialTheme.colorScheme.primary
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Save,
//                    contentDescription = "Save Search"
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("Save Search")
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun SearchBarOverlay(
//    query: String,
//    onQueryChange: (String) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    OutlinedTextField(
//        value = query,
//        onValueChange = onQueryChange,
//        placeholder = {
//            Text("urrésit, Nd. 45, H. 12, Ap. 8, Tirana 100")
//        },
//        leadingIcon = {
//            Icon(
//                imageVector = Icons.Default.Search,
//                contentDescription = "Search"
//            )
//        },
//        trailingIcon = {
//            if (query.isNotEmpty()) {
//                IconButton(onClick = { onQueryChange("") }) {
//                    Icon(
//                        imageVector = Icons.Default.Clear,
//                        contentDescription = "Clear"
//                    )
//                }
//            }
//        },
//        modifier = modifier,
//        shape = RoundedCornerShape(24.dp),
//        colors = OutlinedTextFieldDefaults.colors(
//            focusedContainerColor = MaterialTheme.colorScheme.surface,
//            unfocusedContainerColor = MaterialTheme.colorScheme.surface
//        )
//    )
//}
//
//@Composable
//private fun MapControlButton(
//    icon: androidx.compose.ui.graphics.vector.ImageVector,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    FloatingActionButton(
//        onClick = onClick,
//        modifier = modifier.size(40.dp),
//        containerColor = MaterialTheme.colorScheme.surface,
//        contentColor = MaterialTheme.colorScheme.onSurface
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = null,
//            modifier = Modifier.size(20.dp)
//        )
//    }
//}
//
//@Composable
//private fun PropertyListBottomSheet(
//    properties: List<Property>,
//    searchQuery: String,
//    sortBy: PropertyMapContract.SortOption,
//    favorites: Set<String>,
//    isLoading: Boolean,
//    onSearchQueryChange: (String) -> Unit,
//    onSortChange: (PropertyMapContract.SortOption) -> Unit,
//    onPropertyClick: (Property) -> Unit,
//    onFavoriteClick: (String) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier.padding(horizontal = 16.dp)
//    ) {
//        // Handle indicator
//        Box(
//            modifier = Modifier
//                .size(40.dp, 4.dp)
//                .background(
//                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
//                    shape = RoundedCornerShape(2.dp)
//                )
//                .align(Alignment.CenterHorizontally)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Header with location and sort
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column {
//                Text(
//                    text = if (searchQuery.isNotEmpty()) searchQuery else "Properties in Area",
//                    style = MaterialTheme.typography.headlineSmall,
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.onSurface
//                )
//                Text(
//                    text = "${properties.size} properties found",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//
//            // Sort options
//            SortDropdown(
//                currentSort = sortBy,
//                onSortChange = onSortChange
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Properties list
//        if (isLoading) {
//            Box(
//                modifier = Modifier.fillMaxWidth().padding(32.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        } else {
//            LazyColumn(
//                verticalArrangement = Arrangement.spacedBy(16.dp),
//                contentPadding = PaddingValues(bottom = 16.dp)
//            ) {
//                items(properties) { property ->
//                    EnhancedPropertyCard(
//                        property = property,
//                        isFavorite = favorites.contains(property.id),
//                        onPropertyClick = onPropertyClick,
//                        onFavoriteClick = { onFavoriteClick(property.id) }
//                    )
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun SortDropdown(
//    currentSort: PropertyMapContract.SortOption,
//    onSortChange: (PropertyMapContract.SortOption) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    var expanded by remember { mutableStateOf(false) }
//
//    ExposedDropdownMenuBox(
//        expanded = expanded,
//        onExpandedChange = { expanded = it },
//        modifier = modifier
//    ) {
//        OutlinedTextField(
//            value = "Sort: ${currentSort.name.lowercase().replaceFirstChar { it.uppercase() }}",
//            onValueChange = { },
//            readOnly = true,
//            trailingIcon = {
//                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
//            },
//            modifier = Modifier.menuAnchor(),
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedContainerColor = MaterialTheme.colorScheme.surface,
//                unfocusedContainerColor = MaterialTheme.colorScheme.surface
//            )
//        )
//
//        ExposedDropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            PropertyMapContract.SortOption.values().forEach { sortOption ->
//                DropdownMenuItem(
//                    text = {
//                        Text(sortOption.name.lowercase().replaceFirstChar { it.uppercase() })
//                    },
//                    onClick = {
//                        onSortChange(sortOption)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}