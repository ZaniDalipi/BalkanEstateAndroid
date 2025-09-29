package com.zanoapps.onboarding.presentation.seller.pricing.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.zanoapps.core.presentation.designsystem.LightingIcon
import com.zanoapps.core.presentation.designsystem.MoneyBackIcon
import com.zanoapps.core.presentation.designsystem.SuccessChartIcon

@Composable
fun GuaranteesSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        GuaranteeItem(
            icon = MoneyBackIcon,
            text = "30-day money-back guarantee",
            iconColor = Color(0xFFF59E0B)
        )

        Spacer(modifier = Modifier.height(12.dp))

        GuaranteeItem(
            icon = SuccessChartIcon,
            text = "3x more views than free listings",
            iconColor = Color(0xFF10B981)
        )

        Spacer(modifier = Modifier.height(12.dp))

        GuaranteeItem(
            icon = LightingIcon,
            text = "Instant activation",
            iconColor = Color(0xFFFFEB3B)
        )
    }
}

@Composable
private fun GuaranteeItem(
    icon: ImageVector,
    text: String,
    iconColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
