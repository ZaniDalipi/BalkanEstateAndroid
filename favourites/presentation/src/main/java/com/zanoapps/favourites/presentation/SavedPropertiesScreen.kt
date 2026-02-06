package com.zanoapps.favourites.presentation

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.zanoapps.core.presentation.designsystem.HeartOutlineIcon
import com.zanoapps.core.presentation.designsystem.Poppins
import com.zanoapps.core.presentation.designsystem.components.PropertyCard
import com.zanoapps.core.presentation.designsystem.components.MockData

@Composable
fun SavedPropertiesScreenRoot(
    savedProperties: List<BalkanEstateProperty>,
    onPropertyClick: (BalkanEstateProperty) -> Unit,
    onRemoveFromFavorites: (String) -> Unit,
    onBrowsePropertiesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SavedPropertiesScreen(
        savedProperties = savedProperties,
        onPropertyClick = onPropertyClick,
        onRemoveFromFavorites = onRemoveFromFavorites,
        onBrowsePropertiesClick = onBrowsePropertiesClick,
        modifier = modifier
    )
}

@Composable
fun SavedPropertiesScreen(
    savedProperties: List<BalkanEstateProperty>,
    onPropertyClick: (BalkanEstateProperty) -> Unit,
    onRemoveFromFavorites: (String) -> Unit,
    onBrowsePropertiesClick: () -> Unit,
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
                        text = "Saved Properties",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        fontFamily = Poppins
                    )
                    Text(
                        text = "${savedProperties.size} properties saved",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            if (savedProperties.isEmpty()) {
                // Empty State
                EmptyStateContent(
                    onBrowsePropertiesClick = onBrowsePropertiesClick,
                    modifier = Modifier.weight(1f)
                )
            } else {
                // Properties List
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = savedProperties,
                        key = { it.id }
                    ) { property ->
                        PropertyCard(
                            property = property,
                            isFavorite = true,
                            isNew = false,
                            onPropertyClick = { onPropertyClick(property) },
                            onFavoriteClick = { onRemoveFromFavorites(property.id) },
                            onViewDetailsClick = { onPropertyClick(property) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyStateContent(
    onBrowsePropertiesClick: () -> Unit,
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
            imageVector = HeartOutlineIcon,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No saved properties yet",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Start exploring and save properties you like to view them later.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onBrowsePropertiesClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = BalkanEstatePrimaryBlue
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Browse Properties",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SavedPropertiesScreenWithDataPreview() {
    BalkanEstateTheme {
        SavedPropertiesScreen(
            savedProperties = MockData.getMockProperties().take(3),
            onPropertyClick = {},
            onRemoveFromFavorites = {},
            onBrowsePropertiesClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SavedPropertiesScreenEmptyPreview() {
    BalkanEstateTheme {
        SavedPropertiesScreen(
            savedProperties = emptyList(),
            onPropertyClick = {},
            onRemoveFromFavorites = {},
            onBrowsePropertiesClick = {}
        )
    }
}
