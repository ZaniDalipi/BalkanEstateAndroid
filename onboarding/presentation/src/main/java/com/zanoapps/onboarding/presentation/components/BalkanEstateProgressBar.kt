package com.zanoapps.onboarding.presentation.components


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zanoapps.core.presentation.designsystem.balkanEstateGradientEnd
import com.zanoapps.core.presentation.designsystem.balkanEstateGradientStartPrimary

@Composable
fun ProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    startingColor: Color = balkanEstateGradientStartPrimary,
    endingColor: Color = balkanEstateGradientEnd,
    animationDurationMs: Int = 600
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = animationDurationMs),
        label = "progress_animation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .height(height)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(startingColor, endingColor))
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressBarPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ProgressBar(
                progress = 0.8f,
                startingColor = balkanEstateGradientStartPrimary,
                endingColor = balkanEstateGradientEnd,
                backgroundColor = MaterialTheme.colorScheme.background,
                height = 6.dp
            )
        }
    }
}