package com.zanoapps.core.presentation.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.BathroomsIcon
import com.zanoapps.core.presentation.designsystem.BedroomsIcon
import com.zanoapps.core.presentation.designsystem.Poppins

/**
 * Options for room count selection
 */
data class RoomOption(
    val value: Int?,
    val label: String
)

/**
 * Default room options for bedrooms and bathrooms
 */
object RoomOptions {
    val bedroomOptions = listOf(
        RoomOption(null, "Any"),
        RoomOption(1, "1+"),
        RoomOption(2, "2+"),
        RoomOption(3, "3+"),
        RoomOption(4, "4+"),
        RoomOption(5, "5+")
    )

    val bathroomOptions = listOf(
        RoomOption(null, "Any"),
        RoomOption(1, "1+"),
        RoomOption(2, "2+"),
        RoomOption(3, "3+"),
        RoomOption(4, "4+")
    )
}

/**
 * A selector for room counts (bedrooms/bathrooms) with chip-style buttons
 */
@Composable
fun BalkanEstateRoomSelector(
    label: String,
    options: List<RoomOption>,
    selectedValue: Int?,
    onValueSelected: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = Poppins,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                RoomOptionChip(
                    label = option.label,
                    isSelected = selectedValue == option.value,
                    onClick = { onValueSelected(option.value) }
                )
            }
        }
    }
}

/**
 * Combined bedroom and bathroom selector
 */
@Composable
fun BalkanEstateBedroomBathroomSelector(
    minBedrooms: Int?,
    minBathrooms: Int?,
    onBedroomsChanged: (Int?) -> Unit,
    onBathroomsChanged: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        BalkanEstateRoomSelector(
            label = "Bedrooms",
            options = RoomOptions.bedroomOptions,
            selectedValue = minBedrooms,
            onValueSelected = onBedroomsChanged,
            icon = BedroomsIcon
        )

        Spacer(modifier = Modifier.height(20.dp))

        BalkanEstateRoomSelector(
            label = "Bathrooms",
            options = RoomOptions.bathroomOptions,
            selectedValue = minBathrooms,
            onValueSelected = onBathroomsChanged,
            icon = BathroomsIcon
        )
    }
}

/**
 * Individual room option chip
 */
@Composable
private fun RoomOptionChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 200),
        label = "roomChipBackground"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        },
        animationSpec = tween(durationMillis = 200),
        label = "roomChipBorder"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        animationSpec = tween(durationMillis = 200),
        label = "roomChipText"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            fontFamily = Poppins,
            color = textColor
        )
    }
}

/**
 * Stepper component for incrementing/decrementing room count
 */
@Composable
fun BalkanEstateRoomStepper(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    minValue: Int = 0,
    maxValue: Int = 10,
    icon: ImageVector? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = Poppins,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StepperButton(
                text = "−",
                enabled = value > minValue,
                onClick = { if (value > minValue) onValueChange(value - 1) }
            )

            Text(
                text = if (value == 0) "Any" else value.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = Poppins,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.width(40.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            StepperButton(
                text = "+",
                enabled = value < maxValue,
                onClick = { if (value < maxValue) onValueChange(value + 1) }
            )
        }
    }
}

@Composable
private fun StepperButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (enabled) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 150),
        label = "stepperBackground"
    )

    val contentColor by animateColorAsState(
        targetValue = if (enabled) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        },
        animationSpec = tween(durationMillis = 150),
        label = "stepperContent"
    )

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = if (enabled) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                } else {
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                },
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = contentColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RoomSelectorPreview() {
    BalkanEstateTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            BalkanEstateRoomSelector(
                label = "Bedrooms",
                options = RoomOptions.bedroomOptions,
                selectedValue = 2,
                onValueSelected = {},
                icon = BedroomsIcon
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BedroomBathroomSelectorPreview() {
    BalkanEstateTheme {
        BalkanEstateBedroomBathroomSelector(
            minBedrooms = 3,
            minBathrooms = 2,
            onBedroomsChanged = {},
            onBathroomsChanged = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RoomStepperPreview() {
    BalkanEstateTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            BalkanEstateRoomStepper(
                label = "Bedrooms",
                value = 3,
                onValueChange = {},
                icon = BedroomsIcon
            )
            Spacer(modifier = Modifier.height(16.dp))
            BalkanEstateRoomStepper(
                label = "Bathrooms",
                value = 0,
                onValueChange = {},
                icon = BathroomsIcon
            )
        }
    }
}
