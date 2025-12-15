package com.zanoapps.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.ListViewIcon
import com.zanoapps.core.presentation.designsystem.MapViewIcon

@Composable
fun ListMapToggle(
    isListView: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // List option
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(if (isListView) BalkanEstatePrimaryBlue else Color.Transparent)
                .clickable { onToggle(true) }
                .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ListViewIcon,
                    contentDescription = "List View",
                    tint = if (isListView) Color.White else Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "List",
                    color = if (isListView) Color.White else Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = if (isListView) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }

        // Map option
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(if (!isListView) BalkanEstatePrimaryBlue else Color.Transparent)
                .clickable { onToggle(false) }
                .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = MapViewIcon,
                    contentDescription = "Map View",
                    tint = if (!isListView) Color.White else Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Map",
                    color = if (!isListView) Color.White else Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = if (!isListView) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

@Preview
@Composable
private fun ListMapTogglePreviewList() {
    BalkanEstateTheme {
        ListMapToggle(
            isListView = true,
            onToggle = {}
        )
    }
}

@Preview
@Composable
private fun ListMapTogglePreviewMap() {
    BalkanEstateTheme {
        ListMapToggle(
            isListView = false,
            onToggle = {}
        )
    }
}
