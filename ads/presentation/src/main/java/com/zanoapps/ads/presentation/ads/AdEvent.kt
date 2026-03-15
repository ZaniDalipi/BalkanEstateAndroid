package com.zanoapps.ads.presentation.ads

import com.zanoapps.presentation.ui.UiText

sealed interface AdEvent {
    data class Error(val error: UiText) : AdEvent
    data class OpenAdUrl(val url: String) : AdEvent
}
