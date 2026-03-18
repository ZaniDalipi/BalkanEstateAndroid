package com.zanoapps.auth.presentation.forgot

import com.zanoapps.presentation.ui.UiText

sealed interface ForgotPasswordEvent {
    data class Error(val error: UiText) : ForgotPasswordEvent
    data object NavigateBack : ForgotPasswordEvent
    data object NavigateToLogin : ForgotPasswordEvent
}
