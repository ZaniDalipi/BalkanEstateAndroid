package com.zanoapps.map.presentation.map

import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.designsystem.BackIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateLogo
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.BedroomsIcon
import com.zanoapps.core.presentation.designsystem.BathroomsIcon
import com.zanoapps.core.presentation.designsystem.HomeIcon
import com.zanoapps.core.presentation.designsystem.LocationIcon
import com.zanoapps.core.presentation.designsystem.LogoutIcon
import com.zanoapps.core.presentation.designsystem.MapViewIcon
import com.zanoapps.core.presentation.designsystem.SquareMetersIcon
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
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(state.cameraLatitude, state.cameraLongitude),
            state.cameraZoom
        )
    }

    val mapProperties = remember(state.isMapTypeNormal) {
        MapProperties(
            mapType = if (state.isMapTypeNormal) MapType.NORMAL else MapType.SATELLITE
        )
    }

    val mapUiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false,
            compassEnabled = true
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Google Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings
        ) {
            // Property Markers
            state.markers.forEach { marker ->
                Marker(
                    state = MarkerState(position = LatLng(marker.latitude, marker.longitude)),
                    title = marker.title,
                    snippet = marker.price,
                    onClick = {
                        val property = properties.find { it.id == marker.id }
                        property?.let {
                            onAction(MapAction.OnPropertySelected(it))
                        }
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
                    imageVector = HomeIcon,
                    contentDescription = stringResource(R.string.zoom_in),
                    tint = Color.Black
                )
            }

            FloatingActionButton(
                onClick = { onAction(MapAction.OnZoomOut) },
                containerColor = Color.White,
                modifier = Modifier.size(40.dp)
            ) {
                Text(
                    text = "−",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        // Property Card (when selected)
        if (state.showPropertyCard && state.selectedProperty != null) {
            MapPropertyCard(
                property = state.selectedProperty,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                onViewDetails = {
                    onPropertyClick(state.selectedProperty)
                },
                onDismiss = {
                    onAction(MapAction.OnDismissPropertyCard)
                }
            )
        }

        // Properties count
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = "${state.markers.size} ${stringResource(R.string.properties_in_area)}",
                fontSize = 12.sp,
                color = Color.Black
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
                    PropertyMarker("1", 41.3275, 19.8187, "Modern Apartment", "$250,000", "Apartment")
                )
            ),
            properties = emptyList(),
            onAction = {},
            onPropertyClick = {}
        )
    }
}
