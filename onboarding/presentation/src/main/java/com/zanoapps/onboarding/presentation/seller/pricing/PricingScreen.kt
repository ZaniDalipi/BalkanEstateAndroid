package com.zanoapps.onboarding.presentation.seller.pricing


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.BalkanEstateWhite95
import com.zanoapps.core.presentation.designsystem.BalkanEstateWhite95Background
import com.zanoapps.core.presentation.designsystem.EnterpriseIcon
import com.zanoapps.core.presentation.designsystem.Poppins
import com.zanoapps.core.presentation.designsystem.components.Badge
import com.zanoapps.core.presentation.designsystem.components.GradientBackground
import com.zanoapps.onboarding.presentation.seller.pricing.components.GuaranteesSection
import com.zanoapps.onboarding.presentation.seller.pricing.components.LimitedTimeOfferBanner
import com.zanoapps.onboarding.presentation.seller.pricing.components.PricingCard
import com.zanoapps.onboarding.presentation.seller.pricing.components.subsFeatures.getEnterpriseFeatures
import com.zanoapps.onboarding.presentation.seller.pricing.components.subsFeatures.getMonthlyFeatures
import com.zanoapps.onboarding.presentation.seller.pricing.components.subsFeatures.getYearlyFeatures
import kotlinx.coroutines.delay

@Composable
fun PricingScreen(
    onSelectPlan: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Timer state for countdown
    var timeLeft by remember { mutableStateOf(Pair(30, 0)) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            timeLeft = if (timeLeft.second > 0) {
                timeLeft.first to timeLeft.second - 1
            } else if (timeLeft.first > 0) {
                timeLeft.first - 1 to 59
            } else {
                timeLeft
            }
        }
    }
    GradientBackground {

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            BalkanEstateWhite95,
                            BalkanEstateWhite95Background
                        )
                    )
                )
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            // Limited Time Offer Banner
            LimitedTimeOfferBanner(
                timeLeft = timeLeft,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Choose Your Selling Plan",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    fontFamily = Poppins
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Get your property in front of thousands of potential buyers",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    fontFamily = Poppins
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            PricingCard(
                badge = Badge(
                    text = "MOST POPULAR - 60% OFF",
                    backgroundColor = BalkanEstateRed
                ),
                title = "Pro Yearly Offer",
                titleIcon = null,
                originalPrice = "€200",
                currentPrice = "€80",
                period = "/year",
                savings = "Save €120 today!",
                features = getYearlyFeatures(),
                ctaText = "Get Pro Annual - €80/year",
                ctaStyle = PricingCTAStyle.Primary,
                isHighlighted = true,
                trialText = "+ 15 DAYS FREE TRIAL",
                onCtaClick = { onSelectPlan("yearly") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PricingCard(
                badge = Badge(
                    text = "50% OFF",
                    backgroundColor = BalkanEstateOrange
                ),
                title = "Pro Monthly",
                originalPrice = "€25.00",
                currentPrice = "€12.50",
                savings = "Save €12.50 monthly!",
                features = getMonthlyFeatures(),
                ctaText = "Get Pro Monthly - €12.50/month",
                ctaStyle = PricingCTAStyle.Secondary,
                onCtaClick = { onSelectPlan("monthly") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Enterprise Plan
            PricingCard(
                badge = Badge(
                    text = "Best for corporations",
                    backgroundColor = BalkanEstateOrange
                ),
                title = "Enterprise Plan",
                titleIcon = EnterpriseIcon,
                currentPrice = "€1,000",
                period = "/year",
                features = getEnterpriseFeatures(),
                ctaText = "Perfect for Real Estate Companies",
                ctaStyle = PricingCTAStyle.Enterprise,
                isEnterprise = true,
                onCtaClick = { onSelectPlan("enterprise") }
            )

            Spacer(modifier = Modifier.height(32.dp))

            GuaranteesSection()

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

}


data class PricingFeature(
    val icon: ImageVector? = null,
    val text: String? = null,
    val title: String? = null,
    val description: String? = null
)

enum class PricingCTAStyle {
    Primary,
    Secondary,
    Enterprise
}


@Preview(showBackground = true)
@Composable
private fun PricingScreenPreview() {
    BalkanEstateTheme {
        PricingScreen()
    }
}





