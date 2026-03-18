package com.zanoapps.profile.presentation.subscription

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SubscriptionViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    var state by mutableStateOf(SubscriptionState())
        private set

    private val eventChannel = Channel<SubscriptionEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadCurrentPlan()
    }

    fun onAction(action: SubscriptionAction) {
        when (action) {
            is SubscriptionAction.OnPlanSelected -> state = state.copy(selectedPlan = action.plan)
            is SubscriptionAction.OnBillingToggle -> state = state.copy(isAnnual = action.isAnnual)
            SubscriptionAction.OnSubscribe -> subscribe()
            SubscriptionAction.OnBackClick -> {
                viewModelScope.launch { eventChannel.send(SubscriptionEvent.NavigateBack) }
            }
        }
    }

    private fun loadCurrentPlan() {
        profileRepository.getProfile()
            .let {
                // Current plan defaults to FREE; in production, read from profile
                state = state.copy(currentPlan = SubscriptionPlan.FREE)
            }
    }

    private fun subscribe() {
        if (state.selectedPlan == state.currentPlan) return
        viewModelScope.launch {
            state = state.copy(isProcessing = true, errorMessage = null)
            // In production, this would call a payment API
            // For now, update the profile subscription status
            val result = profileRepository.updateProfile(
                com.zanoapps.profile.domain.model.UserProfile(
                    id = "1",
                    firstName = "",
                    lastName = "",
                    email = "",
                    isPremium = state.selectedPlan != SubscriptionPlan.FREE
                )
            )
            state = state.copy(
                isProcessing = false,
                currentPlan = state.selectedPlan
            )
            eventChannel.send(SubscriptionEvent.SubscriptionSuccess)
        }
    }
}
