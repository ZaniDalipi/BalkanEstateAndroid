package com.zanoapps.map.presentation.map

import android.graphics.drawable.Icon
import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.designsystem.BackIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateLogo
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.BedroomsIcon
import com.zanoapps.core.presentation.designsystem.BathroomsIcon
import com.zanoapps.core.presentation.designsystem.FiltersIcon
import com.zanoapps.core.presentation.designsystem.HomeIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.LogoutIcon
import com.zanoapps.core.presentation.designsystem.MenuHamburgerIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import com.zanoapps.core.presentation.designsystem.SquareMetersIcon
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateNavigationDrawer
import com.zanoapps.core.presentation.designsystem.components.DrawerMenuItem
import com.zanoapps.core.presentation.designsystem.components.EmailSubscriptionBar
import com.zanoapps.core.presentation.designsystem.components.ListMapToggle
import com.zanoapps.core.presentation.designsystem.components.PropertyCard
import com.zanoapps.core.presentation.designsystem.ZoomIn
import com.zanoapps.core.presentation.designsystem.ZoomOut
import com.zanoapps.map.domain.model.PropertyMarker
import com.zanoapps.map.presentation.R
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PropertyMapScreenRoot(
    viewModel: MapViewModel = koinViewModel(),
    properties: List<BalkanEstateProperty>,
    onBackClick: () -> Unit,
    onPropertyClick: (BalkanEstateProperty) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(properties) {
        viewModel.setProperties(properties)
    }

    LaunchedEffect(key1 = true) {
        viewModel.events.collect { event ->
            when (event) {
                is MapEvent.Error -> {
                    Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
                }
                MapEvent.LocationPermissionRequired -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.location_permission_required),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                MapEvent.OpenFilters -> {
                    // Handle filter navigation
                }
                MapEvent.NavigateToProfile -> {
                    // Handle profile navigation
                }
                MapEvent.SubscriptionSuccess -> {
                    Toast.makeText(context, "Subscribed successfully!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    PropertyMapScreen(
        state = viewModel.state,
        properties = properties,
        onAction = { action ->
            when (action) {
                MapAction.OnBackClick -> onBackClick()
                is MapAction.OnPropertySelected -> {
                    viewModel.onAction(action)
                }
                else -> viewModel.onAction(action)
            }
        },
        onPropertyClick = onPropertyClick
    )
}

@Composable
private fun PropertyMapScreen(
    state: MapState,
    properties: List<BalkanEstateProperty>,
    onAction: (MapAction) -> Unit,
    onPropertyClick: (BalkanEstateProperty) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Search Bar
            MapTopBar(
                state = state,
                onMenuClick = { onAction(MapAction.OnOpenDrawer) },
                onFilterClick = { onAction(MapAction.OnFilterClick) },
                onProfileClick = { onAction(MapAction.OnProfileClick) }
            )

            // Main Content
            Box(modifier = Modifier.weight(1f)) {
                if (state.isListView) {
                    // List View
                    PropertyListView(
                        properties = properties,
                        onPropertyClick = onPropertyClick
                    )
                } else {
                    // Map View - Leaflet with OSM
                    LeafletMapView(
                        modifier = Modifier.fillMaxSize(),
                        markers = state.markers,
                        properties = properties,
                        mapLayerType = state.mapLayerType,
                        isDrawingMode = state.isDrawingMode,
                        show3DBuildings = state.show3DBuildings,
                        cameraLatitude = state.cameraLatitude,
                        cameraLongitude = state.cameraLongitude,
                        cameraZoom = state.cameraZoom,
                        onMarkerClick = { propertyId ->
                            onAction(MapAction.OnMarkerClick(propertyId))
                        },
                        onMapMoved = { lat, lng, zoom ->
                            onAction(MapAction.OnCameraMove(lat, lng, zoom))
                        },
                        onAreaDrawn = { south, west, north, east ->
                            onAction(MapAction.OnAreaDrawn(south, west, north, east))
                        }
                    )
                        true
                    }
                )
            }
        }

        // Back Button
        IconButton(
            onClick = { onAction(MapAction.OnBackClick) },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White)
        ) {
            Icon(
                imageVector = BackIcon,
                contentDescription = "Back"
            )
        }

        // Map Controls
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Map Type Toggle
            FloatingActionButton(
                onClick = { onAction(MapAction.OnMapTypeToggle) },
                containerColor = Color.White,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = MapViewIcon,
                    contentDescription = stringResource(R.string.map_type),
                    tint = BalkanEstatePrimaryBlue
                )
            }

            // My Location
            FloatingActionButton(
                onClick = { onAction(MapAction.OnMyLocationClick) },
                containerColor = Color.White,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = LocationIcon,
                    contentDescription = stringResource(R.string.my_location),
                    tint = BalkanEstatePrimaryBlue
                )
            }
        }

                    // Map Controls Overlay
                    MapControlsOverlay(
                        state = state,
                        onAction = onAction
                    )
        // Zoom Controls
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FloatingActionButton(
                onClick = { onAction(MapAction.OnZoomIn) },
                containerColor = Color.White,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = ZoomIn,
                    contentDescription = stringResource(R.string.zoom_in),
                    tint = Color.Black
                )
            }

            FloatingActionButton(
                onClick = { onAction(MapAction.OnZoomOut) },
                containerColor = Color.White,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = ZoomOut,
                    contentDescription = stringResource(R.string.zoom_out),
                    tint = Color.Black
                )
            }
        }

                    // Property Card (when selected)
                    if (state.showPropertyCard && state.selectedProperty != null) {
                        MapPropertyCard(
                            property = state.selectedProperty,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(horizontal = 16.dp, vertical = 140.dp),
                            onViewDetails = {
                                onPropertyClick(state.selectedProperty)
                            },
                            onDismiss = {
                                onAction(MapAction.OnDismissPropertyCard)
                            }
                        )
                    }
                }

                // Category Filters - only show in map view
                if (!state.isListView) {
                    CategoryFilters(
                        selectedCategory = state.selectedCategory,
                        onCategorySelected = { onAction(MapAction.OnCategorySelected(it)) },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    )
                }
            }

            // Bottom Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
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
                            onAction(MapAction.OnViewModeToggle(isListView))
                        }
                    )
                }

                // Leaflet Attribution
                Text(
                    text = "Leaflet | © OpenStreetMap contributors",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )

                // Email Subscription Bar
                EmailSubscriptionBar(
                    onSubscribe = { email ->
                        onAction(MapAction.OnSubscribe(email))
                    }
                )
            }
        }

        // Navigation Drawer
        BalkanEstateNavigationDrawer(
            isOpen = state.isDrawerOpen,
            selectedItem = DrawerMenuItem.Map,
            onItemClick = { item ->
                onAction(MapAction.OnDrawerItemClick(item.title))
                onAction(MapAction.OnCloseDrawer)
            },
            onCloseClick = {
                onAction(MapAction.OnCloseDrawer)
            }
        )
    }
}

@Composable
private fun MapTopBar(
    state: MapState,
    onMenuClick: () -> Unit,
    onFilterClick: () -> Unit,
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
                                text = stringResource(R.string.search_city_address),
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

@Composable
private fun MapControlsOverlay(
    state: MapState,
    onAction: (MapAction) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Top Center - Map Layer Toggle
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Street View - "Pamje Rruge"
            Surface(
                onClick = { if (state.mapLayerType != MapLayerType.STREET) onAction(MapAction.OnToggleMapLayer) },
                color = if (state.mapLayerType == MapLayerType.STREET) BalkanEstatePrimaryBlue.copy(alpha = 0.1f) else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Pamje Rruge",
                    color = if (state.mapLayerType == MapLayerType.STREET) BalkanEstatePrimaryBlue else Color.Gray,
                    fontSize = 13.sp,
                    fontWeight = if (state.mapLayerType == MapLayerType.STREET) FontWeight.Medium else FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Satellite View - "Satelitor"
            Surface(
                onClick = { if (state.mapLayerType != MapLayerType.SATELLITE) onAction(MapAction.OnToggleMapLayer) },
                color = if (state.mapLayerType == MapLayerType.SATELLITE) BalkanEstatePrimaryBlue.copy(alpha = 0.1f) else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Satelitor",
                    color = if (state.mapLayerType == MapLayerType.SATELLITE) BalkanEstatePrimaryBlue else Color.Gray,
                    fontSize = 13.sp,
                    fontWeight = if (state.mapLayerType == MapLayerType.SATELLITE) FontWeight.Medium else FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Location button
            IconButton(
                onClick = { onAction(MapAction.OnMyLocationClick) },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "My Location",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Draw/Vizato button
            Surface(
                onClick = { onAction(MapAction.OnToggleDrawingMode) },
                color = if (state.isDrawingMode) BalkanEstatePrimaryBlue else Color(0xFF3D4852),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Draw",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Vizato",
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Bottom Right - Property Count Badge
        if (state.markers.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 80.dp)
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, Color(0xFFE0E0E0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "${state.markers.size}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = BalkanEstatePrimaryBlue
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryFilters(
    selectedCategory: PropertyCategory,
    onCategorySelected: (PropertyCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        CategoryFilterButton(
            icon = Icons.Default.Apartment,
            isSelected = selectedCategory == PropertyCategory.ALL,
            onClick = { onCategorySelected(PropertyCategory.ALL) }
        )
        CategoryFilterButton(
            icon = Icons.Default.Home,
            isSelected = selectedCategory == PropertyCategory.RESIDENTIAL,
            onClick = { onCategorySelected(PropertyCategory.RESIDENTIAL) }
        )
        CategoryFilterButton(
            icon = Icons.Default.Business,
            isSelected = selectedCategory == PropertyCategory.COMMERCIAL,
            onClick = { onCategorySelected(PropertyCategory.COMMERCIAL) }
        )
        CategoryFilterButton(
            icon = Icons.Default.Landscape,
            isSelected = selectedCategory == PropertyCategory.LAND,
            onClick = { onCategorySelected(PropertyCategory.LAND) }
        )
    }
}

@Composable
private fun CategoryFilterButton(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = if (isSelected) BalkanEstatePrimaryBlue else Color.White,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.size(48.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun PropertyListView(
    properties: List<BalkanEstateProperty>,
    onPropertyClick: (BalkanEstateProperty) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = properties,
            key = { it.id }
        ) { property ->
            PropertyCard(
                property = property,
                isFavorite = false,
                isNew = true,
                onPropertyClick = { onPropertyClick(property) },
                onFavoriteClick = { },
                onViewDetailsClick = { onPropertyClick(property) }
            )
        }
    }
}

@Composable
private fun MapPropertyCard(
    property: BalkanEstateProperty,
    modifier: Modifier = Modifier,
    onViewDetails: () -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onViewDetails),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Property Image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(property.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = property.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = formatPrice(property.price, property.currency),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BalkanEstatePrimaryBlue
                )

                Text(
                    text = property.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = LocationIcon,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${property.city}, ${property.country}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PropertyFeatureSmall(
                        icon = { Icon(BedroomsIcon, null, Modifier.size(14.dp), Color.Gray) },
                        value = "${property.bedrooms}"
                    )
                    PropertyFeatureSmall(
                        icon = { Icon(BathroomsIcon, null, Modifier.size(14.dp), Color.Gray) },
                        value = "${property.bathrooms}"
                    )
                    PropertyFeatureSmall(
                        icon = { Icon(SquareMetersIcon, null, Modifier.size(14.dp), Color.Gray) },
                        value = "${property.squareFootage}"
                    )
                }
            }
        }
    }
}

@Composable
private fun PropertyFeatureSmall(
    icon: @Composable () -> Unit,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

private fun formatPrice(price: Double, currency: String): String {
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    return "$currency${formatter.format(price.toLong())}"
}

@Preview(showBackground = true)
@Composable
private fun PropertyMapScreenPreview() {
    BalkanEstateTheme {
        PropertyMapScreen(
            state = MapState(
                markers = listOf(
                    PropertyMarker("1", 42.0, 20.5, "Modern Apartment", "€250,000", "Apartment")
                )
            ),
            properties = emptyList(),
            onAction = { },
            onPropertyClick = {}
        )
    }
}
