package com.zanoapps.ads.data.repository

import com.zanoapps.ads.domain.model.Ad
import com.zanoapps.ads.domain.model.AdPlacement
import com.zanoapps.ads.domain.model.AdType
import com.zanoapps.ads.domain.repository.AdRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class AdRepositoryImpl : AdRepository {

    private val ads = MutableStateFlow(generateMockAds())

    override fun getAdsForPlacement(placement: AdPlacement): Flow<List<Ad>> {
        return ads.map { list ->
            list.filter { it.placement == placement && it.isActive }
        }
    }

    override suspend fun recordImpression(adId: String) {
        ads.value = ads.value.map {
            if (it.id == adId) it.copy(impressions = it.impressions + 1) else it
        }
    }

    override suspend fun recordClick(adId: String) {
        ads.value = ads.value.map {
            if (it.id == adId) it.copy(clicks = it.clicks + 1) else it
        }
    }

    override fun getActiveAds(): Flow<List<Ad>> {
        return ads.map { list -> list.filter { it.isActive } }
    }

    private fun generateMockAds(): List<Ad> {
        return listOf(
            Ad(id = "ad1", title = "Premium Listings", description = "Get 3x more visibility for your property listings", type = AdType.BANNER, placement = AdPlacement.HOME_FEED),
            Ad(id = "ad2", title = "Featured: Luxury Villa in Ohrid", description = "Stunning lakefront property with panoramic views", type = AdType.FEATURED_LISTING, placement = AdPlacement.SEARCH_RESULTS),
            Ad(id = "ad3", title = "Top Agent: Marko Petrovic", description = "15 years of experience in Belgrade real estate", type = AdType.SPONSORED_AGENT, placement = AdPlacement.AGENT_LIST),
            Ad(id = "ad4", title = "Upgrade to Pro", description = "Unlock advanced search filters and market insights", type = AdType.BANNER, placement = AdPlacement.SEARCH_RESULTS),
            Ad(id = "ad5", title = "Featured: Modern Penthouse", description = "Brand new penthouse in downtown Skopje", type = AdType.FEATURED_LISTING, placement = AdPlacement.HOME_FEED),
            Ad(id = "ad6", title = "Home Insurance Partner", description = "Protect your investment with our trusted partner", type = AdType.BANNER, placement = AdPlacement.PROPERTY_DETAIL)
        )
    }
}
