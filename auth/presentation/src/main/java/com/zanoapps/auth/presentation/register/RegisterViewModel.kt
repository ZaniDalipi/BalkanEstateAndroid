package com.zanoapps.auth.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanoapps.auth.domain.repository.AuthRepository
import com.zanoapps.core.domain.util.Result
import com.zanoapps.auth.domain.model.AccountType as DomainAccountType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnFirstNameChanged -> state = state.copy(firstName = action.name, errorMessage = null)
            is RegisterAction.OnLastNameChanged -> state = state.copy(lastName = action.name, errorMessage = null)
            is RegisterAction.OnEmailChanged -> state = state.copy(email = action.email, errorMessage = null)
            is RegisterAction.OnPhoneChanged -> state = state.copy(phone = action.phone, errorMessage = null)
            is RegisterAction.OnPasswordChanged -> state = state.copy(password = action.password, errorMessage = null)
            is RegisterAction.OnConfirmPasswordChanged -> state = state.copy(confirmPassword = action.password, errorMessage = null)
            RegisterAction.OnTogglePasswordVisibility -> state = state.copy(isPasswordVisible = !state.isPasswordVisible)
            RegisterAction.OnToggleAgreeToTerms -> state = state.copy(agreeToTerms = !state.agreeToTerms)
            is RegisterAction.OnAccountTypeChanged -> state = state.copy(accountType = action.type)
            RegisterAction.OnRegisterClick -> register()
            RegisterAction.OnLoginClick -> {
                viewModelScope.launch { eventChannel.send(RegisterEvent.NavigateToLogin) }
            }
            RegisterAction.OnGoogleRegisterClick -> {}
        }
    }

    private fun register() {
        if (state.firstName.isBlank() || state.lastName.isBlank() || state.email.isBlank() || state.password.isBlank()) {
            state = state.copy(errorMessage = "Please fill in all required fields")
            return
        }
        if (state.password != state.confirmPassword) {
            state = state.copy(errorMessage = "Passwords do not match")
            return
        }
        if (!state.agreeToTerms) {
            state = state.copy(errorMessage = "Please agree to the terms and conditions")
            return
        }
        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null)
            val accountType = when (state.accountType) {
                AccountType.BUYER -> DomainAccountType.BUYER
                AccountType.SELLER -> DomainAccountType.SELLER
                AccountType.AGENT -> DomainAccountType.AGENT
            }
            when (val result = authRepository.register(
                firstName = state.firstName,
                lastName = state.lastName,
                email = state.email,
                phone = state.phone,
                password = state.password,
                accountType = accountType
            )) {
                is Result.Success -> {
                    state = state.copy(isLoading = false)
                    eventChannel.send(RegisterEvent.RegisterSuccess)
                }
                is Result.Error -> {
                    state = state.copy(
                        isLoading = false,
                        errorMessage = "Registration failed. Please try again."
                    )
                }
            }
        }
    }
}
