package com.zanoapps.auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChanged -> state = state.copy(email = action.email, errorMessage = null)
            is LoginAction.OnPasswordChanged -> state = state.copy(password = action.password, errorMessage = null)
            LoginAction.OnTogglePasswordVisibility -> state = state.copy(isPasswordVisible = !state.isPasswordVisible)
            LoginAction.OnToggleRememberMe -> state = state.copy(rememberMe = !state.rememberMe)
            LoginAction.OnLoginClick -> login()
            LoginAction.OnGoogleLoginClick -> { /* Google login */ }
            LoginAction.OnFacebookLoginClick -> { /* Facebook login */ }
            LoginAction.OnForgotPasswordClick -> {
                viewModelScope.launch { eventChannel.send(LoginEvent.NavigateToForgotPassword) }
            }
            LoginAction.OnRegisterClick -> {
                viewModelScope.launch { eventChannel.send(LoginEvent.NavigateToRegister) }
            }
        }
    }

    private fun login() {
        if (state.email.isBlank() || state.password.isBlank()) {
            state = state.copy(errorMessage = "Please fill in all fields")
            return
        }
        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null)
            delay(1500)
            state = state.copy(isLoading = false)
            eventChannel.send(LoginEvent.LoginSuccess)
        }
    }
}
