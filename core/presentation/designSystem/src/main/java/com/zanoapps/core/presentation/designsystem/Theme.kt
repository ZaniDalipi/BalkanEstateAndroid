package com.zanoapps.core.presentation.designsystem

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


val LightColorScheme = lightColorScheme(
    primary = balkanEstateGradientStartPrimary,
    background = BalkanEstateWhite95Background,
    surface = BalkanEstateBlack,
    secondary = BalkanEstateWhite95,
    tertiary = BalkanEstateWhite95Background,
    primaryContainer = BalkanEstateGradient30,
    onPrimary = BalkanEstateWhite95Background,
    onPrimaryContainer = BalkanEstateWhite95Background,
    onBackground = BalkanEstateBlack,
    onSurface = BalkanEstateBlack,
    onSurfaceVariant = BalkanEstateGray

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
