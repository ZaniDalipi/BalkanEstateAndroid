package com.zanoapps.map.presentation.map

import com.zanoapps.presentation.ui.UiText

sealed interface MapEvent {
    data class Error(val error: UiText) : MapEvent
    data object LocationPermissionRequired : MapEvent
}
