package com.zanoapps.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateLogo
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.Poppins

/**
 * Footer component for BalkanEstate app
 * Displays company info, links, and copyright
 */
@Composable
fun BalkanEstateFooter(
    onAboutClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onContactClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1E293B))
            .padding(24.dp)
    ) {
        // Logo and tagline
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = BalkanEstateLogo,
                contentDescription = "Balkan Estate Logo",
                tint = BalkanEstatePrimaryBlue,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Row {
                Text(
                    text = "Balkan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = Poppins
                )
                Text(
                    text = "Estate",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BalkanEstateBlue,
                    fontFamily = Poppins
                )
                Text(
                    text = "AI",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your AI-powered real estate platform for the Balkans. Find your dream property across 10 countries with intelligent search and personalized recommendations.",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f),
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

        Spacer(modifier = Modifier.height(24.dp))

        // Links
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FooterLink(text = "About", onClick = onAboutClick)
            FooterLink(text = "Privacy", onClick = onPrivacyClick)
            FooterLink(text = "Terms", onClick = onTermsClick)
            FooterLink(text = "Contact", onClick = onContactClick)
        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

        Spacer(modifier = Modifier.height(16.dp))

        // Copyright
        Text(
            text = "© 2024 BalkanEstate. All rights reserved.",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Countries served
        Text(
            text = "Serving Albania • Serbia • North Macedonia • Kosovo • Montenegro • Bosnia • Croatia • Slovenia • Bulgaria • Greece",
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.4f),
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun FooterLink(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = Color.White.copy(alpha = 0.8f),
        fontWeight = FontWeight.Medium,
        modifier = Modifier.clickable { onClick() }
    )
}

@Preview(showBackground = true)
@Composable
private fun BalkanEstateFooterPreview() {
    BalkanEstateTheme {
        BalkanEstateFooter()
    }
}
