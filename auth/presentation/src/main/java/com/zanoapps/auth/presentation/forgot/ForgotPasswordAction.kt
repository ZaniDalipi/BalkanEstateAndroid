package com.zanoapps.auth.presentation.forgot

sealed interface ForgotPasswordAction {
    data class OnEmailChanged(val email: String) : ForgotPasswordAction
    data object OnSendResetLinkClick : ForgotPasswordAction
    data object OnBackClick : ForgotPasswordAction
    data object OnBackToLoginClick : ForgotPasswordAction
}
