package com.zanoapps.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.topBarGradient(): Modifier {
    return this.background(
        brush = Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF3A4E9E), // Blue
                Color(0xFF7B4E9E), // Purple
                Color(0xFFD96EB5), // Pink
                Color(0xFFE94C61)  // Red
            )
        )
    )
}