package com.zanoapps.search.presentation.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.HomeIcon

import com.zanoapps.search.presentation.R
import com.zanoapps.search.presentation.search.ViewMode

@Composable
fun ViewModeToggle(
    currentMode: ViewMode,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onToggle),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // List option
            ViewModeOption(
                icon = HomeIcon,
                text = stringResource(R.string.view_mode_list),
                isSelected = currentMode == ViewMode.LIST,
                onClick = { if (currentMode != ViewMode.LIST) onToggle() }
            )
            
            // Map option
            ViewModeOption(
                icon = HomeIcon,
                text = stringResource(R.string.view_mode_map),
                isSelected = currentMode == ViewMode.MAP,
                onClick = { if (currentMode != ViewMode.MAP) onToggle() }
            )
        }
    }
}

@Composable
private fun ViewModeOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            Color.Transparent
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) {
                    Color.White
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(20.dp)
            )
            
            if (isSelected) {
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ViewModeTogglePreview() {
    BalkanEstateTheme {
        ViewModeToggle(
            currentMode = ViewMode.MAP,
            onToggle = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ViewModeToggleListPreview() {
    BalkanEstateTheme {
        ViewModeToggle(
            currentMode = ViewMode.LIST,
            onToggle = {}
        )
    }
}
