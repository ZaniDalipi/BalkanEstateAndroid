package com.zanoapps.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.EmailIcon
import com.zanoapps.core.presentation.designsystem.FiltersIcon
import com.zanoapps.core.presentation.designsystem.SaveSearchIcon


@Composable
fun BalkanEstateSearchBar(
    state: TextFieldState,
    hint: String,
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    hasActiveFilters: Boolean,
    modifier: Modifier = Modifier

) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    BasicTextField(
        state = state,
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        lineLimits = TextFieldLineLimits.SingleLine,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = if (isFocused) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                },
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .onFocusChanged {
                isFocused = it.isFocused
            },
        decorator = { innerBox ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = SaveSearchIcon, // You might want to create a SearchIcon
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.width(12.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        if (state.text.isEmpty() && !isFocused) {
                            Text(
                                text = hint,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                fontSize = 16.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        innerBox()
                    }
                }

                // Filter icon button on the right
                if (onFilterClick != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .padding(8.dp)
                            .clickable { onFilterClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = FiltersIcon,
                            contentDescription = "Filter",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    )
}

// Updated preview to match the search bar
@Preview
@Composable
private fun SearchBarPreview() {
    BalkanEstateTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            BalkanEstateSearchBar(
                state = rememberTextFieldState(),
                hint = "Search for properties...",
                onFilterClick = { /* Handle filter click */ },
                query = "",
                onQueryChange = {

                },
                hasActiveFilters = false,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}