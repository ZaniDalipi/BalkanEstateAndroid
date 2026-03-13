package com.zanoapps.auth.presentation.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val rememberMe: Boolean = false,
    val errorMessage: String? = null
)
