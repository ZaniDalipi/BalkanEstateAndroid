package com.zanoapps.core.presentation.designsystem.components.animations

import kotlinx.coroutines.delay

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

// Standard animation specs
object AnimationDefaults {
    val FastSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )

    val MediumSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )

    val SlowSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessVeryLow
    )

    val BouncySpec = spring<Float>(
        dampingRatio = Spring.DampingRatioHighBouncy,
        stiffness = Spring.StiffnessMediumLow
    )

    val TweenFast = tween<Float>(durationMillis = 200, easing = FastOutSlowInEasing)
    val TweenMedium = tween<Float>(durationMillis = 300, easing = FastOutSlowInEasing)
    val TweenSlow = tween<Float>(durationMillis = 500, easing = FastOutSlowInEasing)
}

// Expansion animations
@Composable
fun animateExpansion(
    expanded: Boolean,
    spec: FiniteAnimationSpec<Float> = AnimationDefaults.MediumSpec
): State<Float> {
    return animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = spec,
        label = "expansion"
    )
}

// Fade and slide animations
fun Modifier.fadeInSlideIn(
    visible: Boolean,
    initialOffsetY: Dp = 20.dp,
    dpSpec: FiniteAnimationSpec<Dp> = spring(),
    spec: FiniteAnimationSpec<Float> = AnimationDefaults.MediumSpec
): Modifier = composed {
    val offsetY by animateDpAsState(
        targetValue = if (visible) 0.dp else initialOffsetY,
        animationSpec = dpSpec,
        label = "offsetY"
    )
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spec,
        label = "alpha"
    )

    this.graphicsLayer {
        translationY = offsetY.toPx()
        this.alpha = alpha
    }
}

// Scale and fade animation
fun Modifier.scaleInFadeIn(
    visible: Boolean,
    initialScale: Float = 0.8f,
    spec: FiniteAnimationSpec<Float> = AnimationDefaults.BouncySpec
): Modifier = composed {
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else initialScale,
        animationSpec = spec,
        label = "scale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spec,
        label = "alpha"
    )

    this.graphicsLayer {
        scaleX = scale
        scaleY = scale
        this.alpha = alpha
    }
}

// Press animation
fun Modifier.pressAnimation(
    enabled: Boolean = true,
    pressScale: Float = 0.95f,
    spec: FiniteAnimationSpec<Float> = AnimationDefaults.FastSpec
): Modifier = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) pressScale else 1f,
        animationSpec = spec,
        label = "pressScale"
    )

    this
        .scale(scale)
        .pointerInput(enabled) {
            if (enabled) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    }
                )
            }
        }
}

// Shimmer animation
fun Modifier.shimmer(
    visible: Boolean = true,
    duration: Int = 1000
): Modifier = composed {
    var shimmerOffset by remember { mutableStateOf(0f) }

    LaunchedEffect(visible) {
        if (visible) {
            while (true) {
                animate(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(duration, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                ) { value, _ ->
                    shimmerOffset = value
                }
            }
        }
    }

    this.graphicsLayer {
        alpha = if (visible) 0.5f + (shimmerOffset * 0.5f) else 1f
    }
}

// Rotate animation
fun Modifier.rotateAnimation(
    rotated: Boolean,
    degrees: Float = 180f,
    spec: FiniteAnimationSpec<Float> = AnimationDefaults.MediumSpec
): Modifier = composed {
    val rotation by animateFloatAsState(
        targetValue = if (rotated) degrees else 0f,
        animationSpec = spec,
        label = "rotation"
    )

    this.graphicsLayer {
        rotationZ = rotation
    }
}

// Slide animation
fun Modifier.slideInOut(
    visible: Boolean,
    slideDirection: SlideDirection = SlideDirection.Bottom,
    distance: Dp = 50.dp,
    spec: FiniteAnimationSpec<Dp> = spring()
): Modifier = composed {
    val offset by animateDpAsState(
        targetValue = if (visible) 0.dp else distance,
        animationSpec = spec,
        label = "slide"
    )

    this.graphicsLayer {
        when (slideDirection) {
            SlideDirection.Left -> translationX = -offset.toPx()
            SlideDirection.Right -> translationX = offset.toPx()
            SlideDirection.Top -> translationY = -offset.toPx()
            SlideDirection.Bottom -> translationY = offset.toPx()
        }
    }
}

enum class SlideDirection {
    Left, Right, Top, Bottom
}

// Stagger animation helper
@Composable
fun rememberStaggeredVisibility(
    itemCount: Int,
    visible: Boolean,
    delayMillis: Int = 50
): List<State<Boolean>> {
    return List(itemCount) { index ->
        var itemVisible by remember { mutableStateOf(false) }

        LaunchedEffect(visible) {
            if (visible) {
                delay((index * delayMillis).toLong())
                itemVisible = true
            } else {
                itemVisible = false
            }
        }

        remember { derivedStateOf { itemVisible } }
    }
}

// Content size animation
@OptIn(ExperimentalAnimationApi::class)
fun expandVertically(
    spec: FiniteAnimationSpec<IntSize> = tween(300, easing = FastOutSlowInEasing)
) = expandVertically(
    animationSpec = spec,
    expandFrom = Alignment.Top
)

@OptIn(ExperimentalAnimationApi::class)
fun shrinkVertically(
    spec: FiniteAnimationSpec<IntSize> = tween(300, easing = FastOutSlowInEasing)
) = shrinkVertically(
    animationSpec = spec,
    shrinkTowards = Alignment.Top
)

// Bounce effect
fun Modifier.bounceClick(
    enabled: Boolean = true
): Modifier = composed {
    var bounceState by remember { mutableStateOf(BounceState.Idle) }

    val scale by animateFloatAsState(
        targetValue = when (bounceState) {
            BounceState.Pressed -> 0.9f
            BounceState.Released -> 1.1f
            BounceState.Idle -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        finishedListener = {
            if (bounceState == BounceState.Released) {
                bounceState = BounceState.Idle
            }
        },
        label = "bounce"
    )

    this
        .scale(scale)
        .pointerInput(enabled) {
            if (enabled) {
                detectTapGestures(
                    onPress = {
                        bounceState = BounceState.Pressed
                        tryAwaitRelease()
                        bounceState = BounceState.Released
                    }
                )
            }
        }
}

private enum class BounceState {
    Idle, Pressed, Released
}

// Pulsing animation
fun Modifier.pulse(
    enabled: Boolean = true,
    minScale: Float = 0.95f,
    maxScale: Float = 1.05f,
    duration: Int = 1000
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val scale by infiniteTransition.animateFloat(
        initialValue = minScale,
        targetValue = maxScale,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    if (enabled) {
        this.scale(scale)
    } else {
        this
    }
}