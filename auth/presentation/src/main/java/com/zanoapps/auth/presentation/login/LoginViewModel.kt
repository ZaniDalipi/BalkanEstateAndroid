package com.zanoapps.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.auth.domain.repository.AuthRepository
import com.zanoapps.auth.domain.validator.UserDataValidator
import com.zanoapps.auth.presentation.R
import com.zanoapps.core.domain.util.DataError
import com.zanoapps.core.domain.util.Result
import com.zanoapps.presentation.ui.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        combine(
            snapshotFlow { state.email.text },
            snapshotFlow { state.password.text }
        ) { email, password ->
            val isEmailValid = UserDataValidator.isValidEmail(email.toString())
            state = state.copy(
                isEmailValid = isEmailValid,
                canLogin = isEmailValid && password.isNotEmpty()
            )
        }.launchIn(viewModelScope)
    }

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnTogglePasswordVisibility -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }
            LoginAction.OnLoginClick -> login()
            LoginAction.OnRegisterClick -> Unit // Handled by navigation
            LoginAction.OnForgotPasswordClick -> Unit // TODO: Implement forgot password
            LoginAction.OnGoogleSignInClick -> Unit // TODO: Implement Google sign in
            LoginAction.OnFacebookSignInClick -> Unit // TODO: Implement Facebook sign in
        }
    }

    private fun login() {
        viewModelScope.launch {
            state = state.copy(isLoggingIn = true)
            val result = authRepository.login(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString()
            )
            state = state.copy(isLoggingIn = false)

            when (result) {
                is Result.Success -> {
                    eventChannel.send(LoginEvent.LoginSuccess)
                }
                is Result.Error -> {
                    val errorMessage = when (result.error) {
                        DataError.Network.UNAUTHORIZED -> UiText.StringResource(R.string.error_invalid_credentials)
                        DataError.Network.NO_INTERNET -> UiText.StringResource(R.string.error_no_internet)
                        DataError.Network.SERVER_ERROR -> UiText.StringResource(R.string.error_server)
                        else -> UiText.StringResource(R.string.error_unknown)
                    }
                    eventChannel.send(LoginEvent.Error(errorMessage))
                }
            }
        }
    }
}
