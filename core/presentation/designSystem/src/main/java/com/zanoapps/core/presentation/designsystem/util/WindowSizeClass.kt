package com.zanoapps.core.presentation.designsystem.util

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.window.layout.WindowMetricsCalculator

/**
 * Window size classes based on Material 3 guidelines
 * https://m3.material.io/foundations/layout/applying-layout/window-size-classes
 */
enum class WindowWidthSizeClass {
    /** Compact width - typically phones in portrait (< 600dp) */
    Compact,
    /** Medium width - typically tablets in portrait or foldables (600dp - 840dp) */
    Medium,
    /** Expanded width - typically tablets in landscape or desktops (> 840dp) */
    Expanded
}

enum class WindowHeightSizeClass {
    /** Compact height (< 480dp) */
    Compact,
    /** Medium height (480dp - 900dp) */
    Medium,
    /** Expanded height (> 900dp) */
    Expanded
}

/**
 * Represents the window size class for both width and height
 */
data class WindowSizeClass(
    val widthSizeClass: WindowWidthSizeClass,
    val heightSizeClass: WindowHeightSizeClass
) {
    /**
     * Returns true if the device should use Navigation Rail instead of Bottom Navigation
     * Navigation Rail is used in medium, expanded, large, or extra-large window sizes
     */
    val shouldUseNavigationRail: Boolean
        get() = widthSizeClass != WindowWidthSizeClass.Compact

    /**
     * Returns true if the device should show the sidebar/drawer
     * Drawer is shown in expanded (tablet landscape/desktop) mode
     */
    val shouldShowDrawer: Boolean
        get() = widthSizeClass == WindowWidthSizeClass.Expanded

    /**
     * Returns true if the device is in compact/mobile mode
     */
    val isCompact: Boolean
        get() = widthSizeClass == WindowWidthSizeClass.Compact
}

/**
 * Calculate the window size class based on the window width
 */
fun calculateWindowSizeClass(widthDp: Dp, heightDp: Dp): WindowSizeClass {
    val widthSizeClass = when {
        widthDp < 600.dp -> WindowWidthSizeClass.Compact
        widthDp < 840.dp -> WindowWidthSizeClass.Medium
        else -> WindowWidthSizeClass.Expanded
    }

    val heightSizeClass = when {
        heightDp < 480.dp -> WindowHeightSizeClass.Compact
        heightDp < 900.dp -> WindowHeightSizeClass.Medium
        else -> WindowHeightSizeClass.Expanded
    }

    return WindowSizeClass(widthSizeClass, heightSizeClass)
}

/**
 * Composable to remember and calculate the window size class
 */
@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    return remember(configuration.screenWidthDp, configuration.screenHeightDp) {
        calculateWindowSizeClass(
            widthDp = configuration.screenWidthDp.dp,
            heightDp = configuration.screenHeightDp.dp
        )
    }
}

/**
 * Calculate window size class from Activity using WindowMetricsCalculator
 * This is more accurate for multi-window and foldable scenarios
 */
@Composable
fun Activity.rememberWindowSizeClass(): WindowSizeClass {
    val density = LocalDensity.current
    val metrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
    val size = with(density) {
        metrics.bounds.toComposeRect().size.toDpSize()
    }

    return remember(size) {
        calculateWindowSizeClass(size.width, size.height)
    }
}

/**
 * Navigation type based on window size
 */
enum class NavigationType {
    /** Bottom navigation bar for compact screens (phones) */
    BottomNavigation,
    /** Navigation rail for medium screens (tablets portrait, foldables) */
    NavigationRail,
    /** Permanent navigation drawer for expanded screens (tablets landscape, desktops) */
    PermanentNavigationDrawer
}

/**
 * Get the recommended navigation type based on window size class
 */
fun WindowSizeClass.getNavigationType(): NavigationType {
    return when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> NavigationType.BottomNavigation
        WindowWidthSizeClass.Medium -> NavigationType.NavigationRail
        WindowWidthSizeClass.Expanded -> NavigationType.NavigationRail // or PermanentNavigationDrawer
    }
}
