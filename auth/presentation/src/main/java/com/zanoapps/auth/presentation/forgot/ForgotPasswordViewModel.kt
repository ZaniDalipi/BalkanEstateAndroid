package com.zanoapps.auth.presentation.forgot

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {

    var state by mutableStateOf(ForgotPasswordState())
        private set

    private val eventChannel = Channel<ForgotPasswordEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: ForgotPasswordAction) {
        when (action) {
            is ForgotPasswordAction.OnEmailChanged -> {
                state = state.copy(email = action.email, errorMessage = null)
            }
            ForgotPasswordAction.OnSendResetLinkClick -> sendResetLink()
            ForgotPasswordAction.OnBackClick -> {
                viewModelScope.launch {
                    eventChannel.send(ForgotPasswordEvent.NavigateBack)
                }
            }
            ForgotPasswordAction.OnBackToLoginClick -> {
                viewModelScope.launch {
                    eventChannel.send(ForgotPasswordEvent.NavigateToLogin)
                }
            }
        }
    }

    private fun sendResetLink() {
        val email = state.email.trim()
        if (email.isBlank()) {
            state = state.copy(errorMessage = "Please enter your email address")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            state = state.copy(errorMessage = "Please enter a valid email address")
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null)
            // Simulate network request
            delay(1500L)
            state = state.copy(
                isLoading = false,
                isEmailSent = true
            )
        }
    }
}
