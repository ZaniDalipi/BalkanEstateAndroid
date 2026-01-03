package com.zanoapps.auth.presentation.register

import com.zanoapps.presentation.ui.UiText

sealed interface RegisterEvent {
    data class Error(val error: UiText) : RegisterEvent
    data object RegistrationSuccess : RegisterEvent
}
