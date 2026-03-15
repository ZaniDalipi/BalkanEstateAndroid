package com.zanoapps.ads.presentation.ads

import com.zanoapps.ads.domain.model.Ad

data class AdState(
    val ads: List<Ad> = emptyList(),
    val currentAd: Ad? = null,
    val isLoading: Boolean = false
)
