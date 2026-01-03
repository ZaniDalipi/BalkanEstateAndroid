package com.zanoapps.auth.presentation.login

import com.zanoapps.presentation.ui.UiText

sealed interface LoginEvent {
    data class Error(val error: UiText) : LoginEvent
    data object LoginSuccess : LoginEvent
}
