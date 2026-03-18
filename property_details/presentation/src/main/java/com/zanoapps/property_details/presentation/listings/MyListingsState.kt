package com.zanoapps.property_details.presentation.listings

import com.zanoapps.core.domain.model.BalkanEstateProperty

data class MyListingsState(
    val isLoading: Boolean = false,
    val listings: List<MyListing> = emptyList(),
    val selectedTab: ListingTab = ListingTab.ACTIVE,
    val errorMessage: String? = null
)

data class MyListing(
    val property: BalkanEstateProperty,
    val status: ListingStatus = ListingStatus.ACTIVE,
    val viewsCount: Int = 0,
    val inquiriesCount: Int = 0
)

enum class ListingTab(val label: String) {
    ACTIVE("Active"),
    PENDING("Pending"),
    SOLD("Sold")
}

enum class ListingStatus {
    ACTIVE,
    PENDING,
    SOLD
}
