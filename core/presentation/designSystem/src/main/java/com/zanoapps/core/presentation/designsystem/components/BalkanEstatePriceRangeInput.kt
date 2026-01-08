package com.zanoapps.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.Poppins

/**
 * A price range input component with min and max text fields.
 */
@Composable
fun BalkanEstatePriceRangeInput(
    minPrice: String,
    maxPrice: String,
    onMinPriceChanged: (String) -> Unit,
    onMaxPriceChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    currency: String = "€",
    minPlaceholder: String = "Min",
    maxPlaceholder: String = "Max",
    errorMessage: String? = null
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PriceInputField(
                value = minPrice,
                onValueChange = onMinPriceChanged,
                placeholder = minPlaceholder,
                currency = currency,
                modifier = Modifier.weight(1f),
                isError = errorMessage != null
            )

            Text(
                text = "–",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            PriceInputField(
                value = maxPrice,
                onValueChange = onMaxPriceChanged,
                placeholder = maxPlaceholder,
                currency = currency,
                modifier = Modifier.weight(1f),
                isError = errorMessage != null
            )
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = BalkanEstateRed,
                fontFamily = Poppins
            )
        }
    }
}

/**
 * Individual price input field with currency prefix
 */
@Composable
private fun PriceInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    currency: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor = when {
        isError -> BalkanEstateRed
        isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    }

    val backgroundColor = when {
        isFocused -> MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
        else -> MaterialTheme.colorScheme.surface
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currency,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = Poppins,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = value,
                onValueChange = { newValue ->
                    // Only allow digits
                    if (newValue.all { it.isDigit() }) {
                        onValueChange(newValue)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused },
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 14.sp,
                                fontFamily = Poppins,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

/**
 * A numeric input field for square footage or other measurements
 */
@Composable
fun BalkanEstateNumericRangeInput(
    minValue: String,
    maxValue: String,
    onMinValueChanged: (String) -> Unit,
    onMaxValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    unit: String = "m²",
    minPlaceholder: String = "Min",
    maxPlaceholder: String = "Max",
    errorMessage: String? = null
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NumericInputField(
                value = minValue,
                onValueChange = onMinValueChanged,
                placeholder = minPlaceholder,
                unit = unit,
                modifier = Modifier.weight(1f),
                isError = errorMessage != null
            )

            Text(
                text = "–",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            NumericInputField(
                value = maxValue,
                onValueChange = onMaxValueChanged,
                placeholder = maxPlaceholder,
                unit = unit,
                modifier = Modifier.weight(1f),
                isError = errorMessage != null
            )
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = BalkanEstateRed,
                fontFamily = Poppins
            )
        }
    }
}

/**
 * Individual numeric input field with unit suffix
 */
@Composable
private fun NumericInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    unit: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor = when {
        isError -> BalkanEstateRed
        isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    }

    val backgroundColor = when {
        isFocused -> MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
        else -> MaterialTheme.colorScheme.surface
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        onValueChange(newValue)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused },
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 14.sp,
                                fontFamily = Poppins,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                        innerTextField()
                    }
                }
            )

            if (unit.isNotEmpty()) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = unit,
                    fontSize = 12.sp,
                    fontFamily = Poppins,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PriceRangeInputPreview() {
    BalkanEstateTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            BalkanEstatePriceRangeInput(
                minPrice = "100000",
                maxPrice = "500000",
                onMinPriceChanged = {},
                onMaxPriceChanged = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            BalkanEstatePriceRangeInput(
                minPrice = "500000",
                maxPrice = "100000",
                onMinPriceChanged = {},
                onMaxPriceChanged = {},
                errorMessage = "Minimum price cannot exceed maximum"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NumericRangeInputPreview() {
    BalkanEstateTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            BalkanEstateNumericRangeInput(
                minValue = "50",
                maxValue = "200",
                onMinValueChanged = {},
                onMaxValueChanged = {},
                unit = "m²"
            )
        }
    }
}
