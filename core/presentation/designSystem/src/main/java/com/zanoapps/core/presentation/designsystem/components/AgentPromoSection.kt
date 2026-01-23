package com.zanoapps.core.presentation.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
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
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.Poppins

/**
 * Agent Promo Section - "Are you a real estate agent?" card
 * Shows promotional content for agents with proper routing based on auth state
 *
 * @param isLoggedIn Whether the user is logged in
 * @param onGetStartedClick Callback when "Get Started" is clicked - should navigate to subscription if logged in, login if not
 * @param onBrowseAgenciesClick Callback when "Browse Agencies" is clicked
 * @param modifier Optional modifier
 */
@Composable
fun AgentPromoSection(
    isLoggedIn: Boolean = false,
    onGetStartedClick: () -> Unit,
    onBrowseAgenciesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Are you a real estate agent?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                fontFamily = Poppins
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Join BalkanEstate to showcase your expertise, connect with potential clients, and grow your business. Get access to premium tools and advertising opportunities to help you succeed in the Balkan real estate market.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Get Started Button - navigates based on auth state
            Button(
                onClick = onGetStartedClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BalkanEstatePrimaryBlue
                )
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Browse Agencies Button
            OutlinedButton(
                onClick = onBrowseAgenciesClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BalkanEstatePrimaryBlue),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = BalkanEstatePrimaryBlue
                )
            ) {
                Text(
                    text = "Browse Agencies",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AgentPromoSectionPreview() {
    BalkanEstateTheme {
        AgentPromoSection(
            isLoggedIn = false,
            onGetStartedClick = {},
            onBrowseAgenciesClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AgentPromoSectionLoggedInPreview() {
    BalkanEstateTheme {
        AgentPromoSection(
            isLoggedIn = true,
            onGetStartedClick = {},
            onBrowseAgenciesClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
