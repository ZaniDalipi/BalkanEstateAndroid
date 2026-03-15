package com.zanoapps.ads.presentation.ads

import com.zanoapps.ads.domain.model.AdPlacement

sealed interface AdAction {
    data class OnLoadAds(val placement: AdPlacement) : AdAction
    data class OnAdClick(val adId: String) : AdAction
    data class OnAdImpression(val adId: String) : AdAction
    data object OnDismissAd : AdAction
}
