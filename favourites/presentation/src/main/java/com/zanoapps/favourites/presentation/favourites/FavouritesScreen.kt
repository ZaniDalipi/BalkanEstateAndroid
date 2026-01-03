package com.zanoapps.favourites.presentation.favourites

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.SavedHomesIcon
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateActionButton
import com.zanoapps.favourites.presentation.R
import com.zanoapps.favourites.presentation.favourites.components.FavouritePropertyCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavouritesScreenRoot(
    viewModel: FavouritesViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onPropertyClick: (BalkanEstateProperty) -> Unit,
    onExploreClick: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.events.collect { event ->
            when (event) {
                is FavouritesEvent.Error -> {
                    Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
                }
                FavouritesEvent.PropertyRemoved -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.removed_from_favourites),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    FavouritesScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                FavouritesAction.OnBackClick -> onBackClick()
                is FavouritesAction.OnPropertyClick -> onPropertyClick(action.property)
                else -> viewModel.onAction(action)
            }
        },
        onExploreClick = onExploreClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavouritesScreen(
    state: FavouritesState,
    onAction: (FavouritesAction) -> Unit,
    onExploreClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.saved_properties),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(FavouritesAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
                }
            }
            state.isEmpty -> {
                EmptyFavouritesContent(
                    modifier = Modifier.padding(paddingValues),
                    onExploreClick = onExploreClick
                )
            }
            else -> {
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = { onAction(FavouritesAction.OnRefresh) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = state.properties,
                            key = { it.id }
                        ) { property ->
                            FavouritePropertyCard(
                                property = property,
                                onPropertyClick = {
                                    onAction(FavouritesAction.OnPropertyClick(property))
                                },
                                onRemoveClick = {
                                    onAction(FavouritesAction.OnRemoveFromFavourites(property.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyFavouritesContent(
    modifier: Modifier = Modifier,
    onExploreClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = SavedHomesIcon,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.no_favourites_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.no_favourites_message),
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        BalkanEstateActionButton(
            text = stringResource(R.string.explore_properties),
            isLoading = false,
            modifier = Modifier.fillMaxWidth(),
            onClick = onExploreClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavouritesScreenEmptyPreview() {
    BalkanEstateTheme {
        FavouritesScreen(
            state = FavouritesState(isEmpty = true),
            onAction = {},
            onExploreClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavouritesScreenWithDataPreview() {
    BalkanEstateTheme {
        FavouritesScreen(
            state = FavouritesState(
                properties = listOf(
                    BalkanEstateProperty(
                        id = "1",
                        title = "Modern Apartment in City Center",
                        price = 250000.0,
                        currency = "$",
                        imageUrl = "https://example.com/image.jpg",
                        bedrooms = 3,
                        bathrooms = 2,
                        squareFootage = 1500,
                        address = "123 Main Street",
                        city = "Tirana",
                        country = "Albania",
                        latitude = 41.3275,
                        longitude = 19.8187,
                        propertyType = "Apartment",
                        listingType = "Sale",
                        agentName = "John Doe",
                        isFeatured = true,
                        isUrgent = false
                    )
                ),
                isEmpty = false
            ),
            onAction = {},
            onExploreClick = {}
        )
    }
}
