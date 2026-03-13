package com.zanoapps.core.presentation.designsystem.components.animations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry

private const val DURATION_MEDIUM = 350
private const val DURATION_SHORT = 250
private const val SLIDE_OFFSET_FRACTION = 0.25

/**
 * Default page-to-page transitions: fade + horizontal slide.
 */
object DefaultTransitions {
    val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeIn(animationSpec = tween(DURATION_MEDIUM, easing = FastOutSlowInEasing)) +
            slideInHorizontally(
                initialOffsetX = { (it * SLIDE_OFFSET_FRACTION).toInt() },
                animationSpec = tween(DURATION_MEDIUM, easing = FastOutSlowInEasing)
            )
    }

    val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOut(animationSpec = tween(DURATION_SHORT, easing = FastOutSlowInEasing)) +
            slideOutHorizontally(
                targetOffsetX = { -(it * SLIDE_OFFSET_FRACTION).toInt() },
                animationSpec = tween(DURATION_MEDIUM, easing = FastOutSlowInEasing)
            )
    }

    val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeIn(animationSpec = tween(DURATION_MEDIUM, easing = FastOutSlowInEasing)) +
            slideInHorizontally(
                initialOffsetX = { -(it * SLIDE_OFFSET_FRACTION).toInt() },
                animationSpec = tween(DURATION_MEDIUM, easing = FastOutSlowInEasing)
            )
    }

    val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOut(animationSpec = tween(DURATION_SHORT, easing = FastOutSlowInEasing)) +
            slideOutHorizontally(
                targetOffsetX = { (it * SLIDE_OFFSET_FRACTION).toInt() },
                animationSpec = tween(DURATION_MEDIUM, easing = FastOutSlowInEasing)
            )
    }
}

/**
 * Onboarding flow transitions: full horizontal slide like turning pages.
 */
object OnboardingTransitions {
    val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(DURATION_MEDIUM, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(DURATION_MEDIUM))
    }

    val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(DURATION_MEDIUM, easing = FastOutSlowInEasing)
        ) + fadeOut(animationSpec = tween(DURATION_SHORT))
    }

    val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(DURATION_MEDIUM, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(DURATION_MEDIUM))
    }

    val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(DURATION_MEDIUM, easing = FastOutSlowInEasing)
        ) + fadeOut(animationSpec = tween(DURATION_SHORT))
    }
}

/**
 * Tab/bottom-nav transitions: quick crossfade with subtle scale.
 */
object TabTransitions {
    val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeIn(animationSpec = tween(DURATION_SHORT, easing = FastOutSlowInEasing)) +
            scaleIn(
                initialScale = 0.94f,
                animationSpec = tween(DURATION_SHORT, easing = FastOutSlowInEasing)
            )
    }

    val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOut(animationSpec = tween(DURATION_SHORT, easing = FastOutSlowInEasing)) +
            scaleOut(
                targetScale = 0.94f,
                animationSpec = tween(DURATION_SHORT, easing = FastOutSlowInEasing)
            )
    }

    val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeIn(animationSpec = tween(DURATION_SHORT, easing = FastOutSlowInEasing)) +
            scaleIn(
                initialScale = 0.94f,
                animationSpec = tween(DURATION_SHORT, easing = FastOutSlowInEasing)
            )
    }

    val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOut(animationSpec = tween(DURATION_SHORT, easing = FastOutSlowInEasing)) +
            scaleOut(
                targetScale = 0.94f,
                animationSpec = tween(DURATION_SHORT, easing = FastOutSlowInEasing)
            )
    }
}

/**
 * Detail/overlay screen transitions: slide up from bottom with fade.
 */
object DetailTransitions {
    val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInVertically(
            initialOffsetY = { it / 3 },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        ) + fadeIn(animationSpec = tween(DURATION_MEDIUM))
    }

    val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOut(animationSpec = tween(DURATION_SHORT))
    }

    val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeIn(animationSpec = tween(DURATION_MEDIUM))
    }

    val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutVertically(
            targetOffsetY = { it / 3 },
            animationSpec = tween(DURATION_MEDIUM, easing = FastOutSlowInEasing)
        ) + fadeOut(animationSpec = tween(DURATION_SHORT))
    }
}
