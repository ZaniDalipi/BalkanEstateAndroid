package com.zanoapps.core.presentation.designsystem

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


val LightColorScheme = lightColorScheme(
    primary = balkanEstateGradientStartPrimary,
    background = balkanEstateWhite,
    surface = balkanEstateDarkGray,
    secondary = balkanEstateWhite,
    tertiary = balkanEstateWhite,
    primaryContainer = balkanEstateGradient30,
    onPrimary = balkanEstateWhite,
    onBackground = balkanEstateBlack,
    onSurface = balkanEstateBlack,
    onSurfaceVariant = balkanEstateGray

)
val DarkColorScheme = darkColorScheme()


@Composable
fun BalkanEstateTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme, typography = Typography, content = content
    )
}
