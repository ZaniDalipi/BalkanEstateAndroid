package com.zanoapps.auth.presentation.login

sealed interface LoginAction {
    data object OnTogglePasswordVisibility : LoginAction
    data object OnLoginClick : LoginAction
    data object OnRegisterClick : LoginAction
    data object OnForgotPasswordClick : LoginAction
    data object OnGoogleSignInClick : LoginAction
    data object OnFacebookSignInClick : LoginAction
}
