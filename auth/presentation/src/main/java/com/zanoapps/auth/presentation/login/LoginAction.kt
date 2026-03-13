package com.zanoapps.auth.presentation.login

sealed interface LoginAction {
    data class OnEmailChanged(val email: String) : LoginAction
    data class OnPasswordChanged(val password: String) : LoginAction
    data object OnTogglePasswordVisibility : LoginAction
    data object OnToggleRememberMe : LoginAction
    data object OnLoginClick : LoginAction
    data object OnGoogleLoginClick : LoginAction
    data object OnFacebookLoginClick : LoginAction
    data object OnForgotPasswordClick : LoginAction
    data object OnRegisterClick : LoginAction
}
