package com.zanoapps.property_details.presentation.listings

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.core.presentation.designsystem.BackIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyListingsScreenRoot(
    viewModel: MyListingsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToCreateListing: () -> Unit = {},
    onNavigateToEditListing: (String) -> Unit = {}
) {
    MyListingsScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                MyListingsAction.OnBackClick -> onNavigateBack()
                MyListingsAction.OnAddNewListing -> onNavigateToCreateListing()
                is MyListingsAction.OnEditListing -> onNavigateToEditListing(action.listingId)
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun MyListingsScreen(
    state: MyListingsState,
    onAction: (MyListingsAction) -> Unit
) {
    val tabs = ListingTab.entries
    val filteredListings = state.listings.filter { listing ->
        when (state.selectedTab) {
            ListingTab.ACTIVE -> listing.status == ListingStatus.ACTIVE
            ListingTab.PENDING -> listing.status == ListingStatus.PENDING
            ListingTab.SOLD -> listing.status == ListingStatus.SOLD
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(MyListingsAction.OnAddNewListing) },
                containerColor = BalkanEstatePrimaryBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add New Listing"
                )
            }
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onAction(MyListingsAction.OnBackClick) }) {
                    Icon(BackIcon, "Back", Modifier.size(20.dp))
                }
                Text(
                    text = "My Listings",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // Tab Row
            TabRow(
                selectedTabIndex = tabs.indexOf(state.selectedTab),
                containerColor = Color.White,
                contentColor = BalkanEstatePrimaryBlue,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[tabs.indexOf(state.selectedTab)]),
                        color = BalkanEstatePrimaryBlue
                    )
                }
            ) {
                tabs.forEach { tab ->
                    Tab(
                        selected = state.selectedTab == tab,
                        onClick = { onAction(MyListingsAction.OnTabSelected(tab)) },
                        text = {
                            Text(
                                text = tab.label,
                                fontWeight = if (state.selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selectedContentColor = BalkanEstatePrimaryBlue,
                        unselectedContentColor = BalkanEstateGray
                    )
                }
            }

            // Content
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = BalkanEstatePrimaryBlue)
                    }
                }
                filteredListings.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No listings yet",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Create your first listing!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = BalkanEstateGray
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedButton(
                                onClick = { onAction(MyListingsAction.OnAddNewListing) },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Add New Listing")
                            }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = filteredListings,
                            key = { it.property.id }
                        ) { listing ->
                            ListingCard(
                                listing = listing,
                                onEditClick = { onAction(MyListingsAction.OnEditListing(listing.property.id)) },
                                onDeleteClick = { onAction(MyListingsAction.OnDeleteListing(listing.property.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ListingCard(
    listing: MyListing,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val statusColor = when (listing.status) {
        ListingStatus.ACTIVE -> BalkanEstateGreen
        ListingStatus.PENDING -> BalkanEstateOrange
        ListingStatus.SOLD -> BalkanEstateGray
    }
    val statusLabel = when (listing.status) {
        ListingStatus.ACTIVE -> "Active"
        ListingStatus.PENDING -> "Pending"
        ListingStatus.SOLD -> "Sold"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title and Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = listing.property.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Status Badge
                Box(
                    modifier = Modifier
                        .background(
                            color = statusColor.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Price
            Text(
                text = "${listing.property.currency} ${"%,.0f".format(listing.property.price)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BalkanEstatePrimaryBlue
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Location
            Text(
                text = "${listing.property.city}, ${listing.property.country}",
                style = MaterialTheme.typography.bodySmall,
                color = BalkanEstateGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                StatBadge(label = "Views", value = listing.viewsCount.toString())
                StatBadge(label = "Inquiries", value = listing.inquiriesCount.toString())
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onEditClick,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(16.dp),
                        tint = BalkanEstatePrimaryBlue
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Edit",
                        fontSize = 13.sp,
                        color = BalkanEstatePrimaryBlue
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    onClick = onDeleteClick,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.size(16.dp),
                        tint = BalkanEstateRed
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Delete",
                        fontSize = 13.sp,
                        color = BalkanEstateRed
                    )
                }
            }
        }
    }
}

@Composable
private fun StatBadge(
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = BalkanEstateGray
        )
    }
}

@Preview
@Composable
private fun MyListingsScreenPreview() {
    BalkanEstateTheme {
        MyListingsScreen(
            state = MyListingsState(
                listings = listOf(
                    MyListing(
                        property = BalkanEstateProperty(
                            id = "1",
                            title = "Modern Apartment in Tirana Center",
                            price = 85000.0,
                            currency = "EUR",
                            imageUrl = "",
                            bedrooms = 2,
                            bathrooms = 1,
                            squareFootage = 85,
                            address = "Rruga Ismail Qemali",
                            city = "Tirana",
                            country = "Albania",
                            latitude = 41.3275,
                            longitude = 19.8187,
                            propertyType = "Apartment",
                            listingType = "Sale",
                            agentName = "Agent Smith"
                        ),
                        status = ListingStatus.ACTIVE,
                        viewsCount = 245,
                        inquiriesCount = 12
                    ),
                    MyListing(
                        property = BalkanEstateProperty(
                            id = "2",
                            title = "Seaside Villa in Saranda",
                            price = 250000.0,
                            currency = "EUR",
                            imageUrl = "",
                            bedrooms = 4,
                            bathrooms = 3,
                            squareFootage = 220,
                            address = "Rruga e Plazhit",
                            city = "Saranda",
                            country = "Albania",
                            latitude = 39.8661,
                            longitude = 20.0050,
                            propertyType = "Villa",
                            listingType = "Sale",
                            agentName = "Agent Johnson"
                        ),
                        status = ListingStatus.PENDING,
                        viewsCount = 89,
                        inquiriesCount = 5
                    ),
                    MyListing(
                        property = BalkanEstateProperty(
                            id = "3",
                            title = "Cozy Studio in Pristina",
                            price = 45000.0,
                            currency = "EUR",
                            imageUrl = "",
                            bedrooms = 1,
                            bathrooms = 1,
                            squareFootage = 45,
                            address = "Bulevardi Nene Tereza",
                            city = "Pristina",
                            country = "Kosovo",
                            latitude = 42.6629,
                            longitude = 21.1655,
                            propertyType = "Apartment",
                            listingType = "Sale",
                            agentName = "Agent Brown"
                        ),
                        status = ListingStatus.SOLD,
                        viewsCount = 432,
                        inquiriesCount = 28
                    )
                )
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun MyListingsScreenEmptyPreview() {
    BalkanEstateTheme {
        MyListingsScreen(
            state = MyListingsState(),
            onAction = {}
        )
    }
}
