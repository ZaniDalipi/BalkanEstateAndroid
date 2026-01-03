package com.zanoapps.auth.presentation.register

sealed interface RegisterAction {
    data object OnTogglePasswordVisibility : RegisterAction
    data object OnToggleConfirmPasswordVisibility : RegisterAction
    data object OnRegisterClick : RegisterAction
    data object OnLoginClick : RegisterAction
    data object OnTermsClick : RegisterAction
    data object OnPrivacyPolicyClick : RegisterAction
}
