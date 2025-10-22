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
                state = state.copy(
                    clientSelectedOption = action.intent,
                    shouldNavigate = true,
                    navigationPath = action.intent.navigationPath
                )
            }
            ClientIntentAction.OnSkipClick -> {
                state = state.copy(
                    clientSelectedOption = null,
                    shouldNavigate = true,
                    navigationPath = "skip_path"
                )
            }
            else -> Unit
        }
    }

    fun resetNavigation() {
        state = state.copy(shouldNavigate = false, navigationPath = null)
    }
}