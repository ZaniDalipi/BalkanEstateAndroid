package com.zanoapps.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
    singleLine: Boolean = true,
    maxLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = placeholder?.let { { Text(it) } },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon ?: if (error != null) {
                {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            } else null,
            isError = error != null,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onAny = { onImeAction() }
            ),
            visualTransformation = visualTransformation,
            enabled = enabled,
            readOnly = readOnly,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = if (error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun ValidatedNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    placeholder: String? = null,
    prefix: String? = null,
    suffix: String? = null,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
    enabled: Boolean = true
) {
    ValidatedTextField(
        value = value,
        onValueChange = { newValue ->
            // Only allow numeric input
            if (newValue.isEmpty() || newValue.all { it.isDigit() || it == '.' || it == ',' }) {
                onValueChange(newValue)
            }
        },
        label = label,
        placeholder = placeholder,
        leadingIcon = prefix?.let { { Text(it, modifier = Modifier.padding(start = 12.dp)) } },
        trailingIcon = suffix?.let { { Text(it, modifier = Modifier.padding(end = 12.dp)) } },
        keyboardType = KeyboardType.Number,
        imeAction = imeAction,
        onImeAction = onImeAction,
        error = error,
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun ValidatedEmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    label: String = "Email",
    placeholder: String = "Enter your email",
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
    enabled: Boolean = true
) {
    ValidatedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onImeAction = onImeAction,
        error = error,
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun ValidatedPhoneField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    label: String = "Phone",
    placeholder: String = "+355 69 XXX XXXX",
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
    enabled: Boolean = true
) {
    ValidatedTextField(
        value = value,
        onValueChange = { newValue ->
            // Only allow phone-like input
            if (newValue.isEmpty() || newValue.all { it.isDigit() || it == '+' || it == ' ' || it == '-' }) {
                onValueChange(newValue)
            }
        },
        label = label,
        placeholder = placeholder,
        keyboardType = KeyboardType.Phone,
        imeAction = imeAction,
        onImeAction = onImeAction,
        error = error,
        enabled = enabled,
        modifier = modifier
    )
}
