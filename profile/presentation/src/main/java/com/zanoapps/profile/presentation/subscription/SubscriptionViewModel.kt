package com.zanoapps.profile.presentation.subscription

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SubscriptionViewModel : ViewModel() {

    var state by mutableStateOf(SubscriptionState())
        private set

    private val eventChannel = Channel<SubscriptionEvent>()
    val events = eventChannel.receiveAsFlow()

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

    private fun subscribe() {
        viewModelScope.launch {
            state = state.copy(isProcessing = true)
            delay(2000)
            state = state.copy(isProcessing = false, currentPlan = state.selectedPlan)
            eventChannel.send(SubscriptionEvent.SubscriptionSuccess)
        }
    }
}
