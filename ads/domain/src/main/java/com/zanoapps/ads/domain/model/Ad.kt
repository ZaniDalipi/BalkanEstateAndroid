package com.zanoapps.ads.domain.model

data class Ad(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String = "",
    val actionUrl: String = "",
    val type: AdType = AdType.BANNER,
    val placement: AdPlacement = AdPlacement.HOME_FEED,
    val isActive: Boolean = true,
    val impressions: Int = 0,
    val clicks: Int = 0,
    val startDate: Long = System.currentTimeMillis(),
    val endDate: Long = System.currentTimeMillis() + 2592000000 // 30 days
)

enum class AdType {
    BANNER,
    FEATURED_LISTING,
    SPONSORED_AGENT,
    INTERSTITIAL
}

enum class AdPlacement {
    HOME_FEED,
    SEARCH_RESULTS,
    PROPERTY_DETAIL,
    AGENT_LIST,
    INBOX
}
