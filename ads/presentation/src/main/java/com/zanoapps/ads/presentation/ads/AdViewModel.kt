package com.zanoapps.ads.presentation.ads

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.ads.domain.repository.AdRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AdViewModel(
    private val adRepository: AdRepository
) : ViewModel() {

    var state by mutableStateOf(AdState())
        private set

    private val eventChannel = Channel<AdEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: AdAction) {
        when (action) {
            is AdAction.OnLoadAds -> loadAds(action.placement)
            is AdAction.OnAdClick -> {
                viewModelScope.launch {
                    adRepository.recordClick(action.adId)
                    val ad = state.ads.find { it.id == action.adId }
                    if (ad != null && ad.actionUrl.isNotEmpty()) {
                        eventChannel.send(AdEvent.OpenAdUrl(ad.actionUrl))
                    }
                }
            }
            is AdAction.OnAdImpression -> {
                viewModelScope.launch {
                    adRepository.recordImpression(action.adId)
                }
            }
            AdAction.OnDismissAd -> {
                state = state.copy(currentAd = null)
            }
        }
    }

    private fun loadAds(placement: com.zanoapps.ads.domain.model.AdPlacement) {
        state = state.copy(isLoading = true)
        adRepository.getAdsForPlacement(placement)
            .onEach { ads ->
                state = state.copy(
                    ads = ads,
                    currentAd = ads.firstOrNull(),
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }
}
