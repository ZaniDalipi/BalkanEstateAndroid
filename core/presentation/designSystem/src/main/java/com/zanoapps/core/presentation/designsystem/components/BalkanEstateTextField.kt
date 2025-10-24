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
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.CheckIcon
import com.zanoapps.core.presentation.designsystem.CrossIcon
import com.zanoapps.core.presentation.designsystem.EmailIcon
import com.zanoapps.core.presentation.designsystem.FiltersIcon
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon
import java.util.logging.Filter

@Composable
fun BalkanEstateTextField(
    state: TextFieldState,
    startIcon: ImageVector?,
    endIcon: ImageVector?,
    hint: String,
    searchQuery: String?,
    modifier: Modifier = Modifier,
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    additionalInfo: String? = null


) {
    // this is a logic that we use in every textField and we use this to make sure what to show in
    // certain scenarios

    var isFocused by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
    ) {

        Spacer(modifier = Modifier.height(4.dp))



        BasicTextField(
            state = state,
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (isFocused) {
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.05f
                        )
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
                .border(
                    width = 1.dp,
                    color = if (isFocused) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    },
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
                // this is important to update the state of the focus
                .onFocusChanged(
                    onFocusChanged = {
                        isFocused = it.isFocused
                    }
                ),
//            decorator is used to place icons
            decorator = { innerBox ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (startIcon != null) {
                        Icon(
                            imageVector = startIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.width(16.dp))


                        Box(
                            modifier = Modifier
                                .weight(1f)

                        ) {
                            if (state.text.isEmpty() && !isFocused) {
                                Text(
                                    text = hint,
                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.4f
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            innerBox()
                        }
                        if (endIcon != null) {

                            Spacer(modifier = Modifier.width(16.dp))

                            Icon(
                                imageVector = endIcon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .padding(end = 8.dp)

                            )
                        }
                    }
                }
            }
        )
    }
}

@Preview()
@Composable
private fun RuniqueTextFieldPrev() {
    BalkanEstateTheme {
        BalkanEstateTextField(
            state = rememberTextFieldState(),
            startIcon = SaveSearchIcon,
            endIcon = CrossIcon,
            hint = "adressa",
            searchQuery = "Durres",
            additionalInfo = "must be valid email",
            modifier = Modifier.fillMaxWidth()
        )
    }
}