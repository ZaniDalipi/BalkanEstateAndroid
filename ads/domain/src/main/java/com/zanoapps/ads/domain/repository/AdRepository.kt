package com.zanoapps.ads.domain.repository

import com.zanoapps.ads.domain.model.Ad
import com.zanoapps.ads.domain.model.AdPlacement
import kotlinx.coroutines.flow.Flow

interface AdRepository {
    fun getAdsForPlacement(placement: AdPlacement): Flow<List<Ad>>
    suspend fun recordImpression(adId: String)
    suspend fun recordClick(adId: String)
    fun getActiveAds(): Flow<List<Ad>>
}
