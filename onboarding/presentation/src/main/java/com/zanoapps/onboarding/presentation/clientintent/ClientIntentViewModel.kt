package com.zanoapps.onboarding.presentation.clientintent

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


class ClientIntentViewModel: ViewModel() {

    var state by mutableStateOf(ClientIntentState())
        private set

    fun onAction(action: ClientIntentAction) {
        when(action) {
            is ClientIntentAction.OnOptionSelected -> {
                // Update the state with selected option
                state = state.copy(clientSelectedOption = action.intent)
                // You can either navigate immediately or wait for user confirmation
            }
            ClientIntentAction.OnSkipClick -> {
                // Handle skip logic
                state = state.copy(clientSelectedOption = null)
            }
            ClientIntentAction.OnNavigateToNextScreen -> {
                // This will be called when you want to navigate after selection
                // The actual navigation will be handled in the composable
            }
        }
    }

}