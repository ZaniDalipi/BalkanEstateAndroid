package com.zanoapps.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState
import com.zanoapps.auth.domain.validator.PasswordValidationState

data class RegisterState(
    val firstName: TextFieldState = TextFieldState(),
    val lastName: TextFieldState = TextFieldState(),
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val confirmPassword: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val passwordValidationState: PasswordValidationState = PasswordValidationState(),
    val isEmailValid: Boolean = false,
    val isFirstNameValid: Boolean = false,
    val isLastNameValid: Boolean = false,
    val doPasswordsMatch: Boolean = false,
    val canRegister: Boolean = false,
    val isRegistering: Boolean = false
)
