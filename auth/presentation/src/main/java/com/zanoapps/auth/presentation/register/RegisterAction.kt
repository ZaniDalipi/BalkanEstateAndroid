package com.zanoapps.auth.presentation.register

sealed interface RegisterAction {
    data class OnFirstNameChanged(val name: String) : RegisterAction
    data class OnLastNameChanged(val name: String) : RegisterAction
    data class OnEmailChanged(val email: String) : RegisterAction
    data class OnPhoneChanged(val phone: String) : RegisterAction
    data class OnPasswordChanged(val password: String) : RegisterAction
    data class OnConfirmPasswordChanged(val password: String) : RegisterAction
    data object OnTogglePasswordVisibility : RegisterAction
    data object OnToggleAgreeToTerms : RegisterAction
    data class OnAccountTypeChanged(val type: AccountType) : RegisterAction
    data object OnRegisterClick : RegisterAction
    data object OnLoginClick : RegisterAction
    data object OnGoogleRegisterClick : RegisterAction
}
