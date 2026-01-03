package com.zanoapps.auth.presentation.register

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

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        combine(
            snapshotFlow { state.firstName.text },
            snapshotFlow { state.lastName.text },
            snapshotFlow { state.email.text },
            snapshotFlow { state.password.text },
            snapshotFlow { state.confirmPassword.text }
        ) { firstName, lastName, email, password, confirmPassword ->
            val isFirstNameValid = UserDataValidator.isValidName(firstName.toString())
            val isLastNameValid = UserDataValidator.isValidName(lastName.toString())
            val isEmailValid = UserDataValidator.isValidEmail(email.toString())
            val passwordValidation = UserDataValidator.isValidPassword(password.toString())
            val doPasswordsMatch = password.toString() == confirmPassword.toString() &&
                                   password.isNotEmpty()

            state = state.copy(
                isFirstNameValid = isFirstNameValid,
                isLastNameValid = isLastNameValid,
                isEmailValid = isEmailValid,
                passwordValidationState = passwordValidation,
                doPasswordsMatch = doPasswordsMatch,
                canRegister = isFirstNameValid &&
                             isLastNameValid &&
                             isEmailValid &&
                             passwordValidation.isValid &&
                             doPasswordsMatch
            )
        }.launchIn(viewModelScope)
    }

    fun onAction(action: RegisterAction) {
        when (action) {
            RegisterAction.OnTogglePasswordVisibility -> {
                state = state.copy(isPasswordVisible = !state.isPasswordVisible)
            }
            RegisterAction.OnToggleConfirmPasswordVisibility -> {
                state = state.copy(isConfirmPasswordVisible = !state.isConfirmPasswordVisible)
            }
            RegisterAction.OnRegisterClick -> register()
            RegisterAction.OnLoginClick -> Unit // Handled by navigation
            RegisterAction.OnTermsClick -> Unit // TODO: Open terms
            RegisterAction.OnPrivacyPolicyClick -> Unit // TODO: Open privacy policy
        }
    }

    private fun register() {
        viewModelScope.launch {
            state = state.copy(isRegistering = true)
            val result = authRepository.register(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString(),
                firstName = state.firstName.text.toString().trim(),
                lastName = state.lastName.text.toString().trim()
            )
            state = state.copy(isRegistering = false)

            when (result) {
                is Result.Success -> {
                    eventChannel.send(RegisterEvent.RegistrationSuccess)
                }
                is Result.Error -> {
                    val errorMessage = when (result.error) {
                        DataError.Network.CONFLICT -> UiText.StringResource(R.string.error_email_taken)
                        DataError.Network.NO_INTERNET -> UiText.StringResource(R.string.error_no_internet)
                        DataError.Network.SERVER_ERROR -> UiText.StringResource(R.string.error_server)
                        else -> UiText.StringResource(R.string.error_unknown)
                    }
                    eventChannel.send(RegisterEvent.Error(errorMessage))
                }
            }
        }
    }
}
