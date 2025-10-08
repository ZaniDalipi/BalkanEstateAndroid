package com.zanoapps.core.presentation.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.Poppins
import com.zanoapps.core.presentation.designsystem.balkanEstateGradientEnd
import com.zanoapps.core.presentation.designsystem.balkanEstateGradientStartPrimary
import com.zanoapps.core.presentation.designsystem.components.animations.AnimationDefaults
import com.zanoapps.core.presentation.designsystem.components.animations.pressAnimation
import com.zanoapps.core.presentation.designsystem.components.animations.rotateAnimation

@Composable
fun BalkanEstateActionButton(
    text: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary
        ),
        border = if (!enabled) {
            BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        } else null,
        shape = RoundedCornerShape(16f),
        modifier = modifier
            .height(IntrinsicSize.Max)
            .clip(shape = RoundedCornerShape(16f))
            .drawBehind {

                val width = size.width
                val height = size.height

                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            balkanEstateGradientStartPrimary,
                            balkanEstateGradientEnd
                        ),
                        start = Offset(width * 0.3f, height * 0.7f),  // 20% from top-left
                        end = Offset(width * 0.7f, height * 0.7f)     // 20% from bottom-right
                    ),
                    size = size
                )
            }




        ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center

        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(15.dp)
                    .alpha(if (isLoading) 1f else 0f),
                strokeWidth = 1.5.dp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = text,
                modifier = Modifier
                    .alpha(if (isLoading) 0f else 1f),
                fontWeight = FontWeight.Medium,
                fontFamily = Poppins

            )
        }
    }
}


@Composable
fun BalkanEstateOutlinedActionButton(
    text: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16f),
        modifier = modifier.height(IntrinsicSize.Max)

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center

        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(15.dp)
                    .alpha(if (isLoading) 1f else 0f),
                strokeWidth = 1.5.dp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = text,
                modifier = Modifier
                    .alpha(if (isLoading) 0f else 1f),
                fontWeight = FontWeight.Medium,
                fontFamily = Poppins

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EnabledBalkanEstateButtonPreview() {
    BalkanEstateTheme {
        BalkanEstateActionButton(text = "Sign Up", isLoading = false, enabled = true) {
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DisabledBalkanEstateButtonPreview() {
    BalkanEstateTheme {
        BalkanEstateActionButton(text = "Sign In", isLoading = false, enabled = false) {
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OutlinedBalkanEstateEnabledActionButtonPreview() {
    BalkanEstateTheme {
        BalkanEstateOutlinedActionButton(text = "Sign In", isLoading = false, enabled = true) {
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OutlinedBalkanEstateDisabledActionButtonPreview() {
    BalkanEstateTheme {
        BalkanEstateOutlinedActionButton(text = "Sign In", isLoading = false, enabled = false) {
        }
    }
}


