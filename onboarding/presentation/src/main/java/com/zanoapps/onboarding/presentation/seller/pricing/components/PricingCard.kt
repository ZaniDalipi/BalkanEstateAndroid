package com.zanoapps.onboarding.presentation.seller.pricing.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateDarkGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateEnterpriseColorCardView
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray40
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.CheckIcon
import com.zanoapps.core.presentation.designsystem.Poppins
import com.zanoapps.core.presentation.designsystem.components.Badge
import com.zanoapps.core.presentation.designsystem.components.animations.pressAnimation
import com.zanoapps.onboarding.presentation.seller.pricing.PricingFeature
import com.zanoapps.onboarding.presentation.seller.pricing.SubscriptionPlan
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PricingCard(
    title: String,
    currentPrice: String,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badge: Badge? = null,
    titleIcon: ImageVector? = null,
    originalPrice: String? = null,
    period: String = "",
    visible: Boolean = true,
    delayMillis: Long = 0,
    savings: String? = null,
    features: List<PricingFeature> = emptyList(),
    style: SubscriptionPlan = SubscriptionPlan.PRO_ANNUAL,
    isHighlighted: Boolean = false,
    isEnterprise: Boolean = false,
    trialText: String? = null
) {
    val cardColors = when {
        isHighlighted -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )

        isEnterprise -> CardDefaults.cardColors(
            containerColor = BalkanEstateEnterpriseColorCardView
        )

        else -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    }

    val borderModifier = if (isHighlighted) {
        Modifier.border(
            width = 3.dp,
            color = BalkanEstateGreen,
            shape = RoundedCornerShape(24.dp)
        )
    } else {
        Modifier.border(
            width = 3.dp,
            color = BalkanEstateGray40,
            shape = RoundedCornerShape(24.dp)
        )
    }

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(visible) {
        if (visible) {
            delay(delayMillis)
            isVisible = true
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.9f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "cardScale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(400),
        label = "cardAlpha"
    )


    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .then(borderModifier)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
                translationY = if (!isVisible) 30f else 0f
            }
            .pressAnimation(),
        shape = RoundedCornerShape(24.dp),
        colors = cardColors,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isHighlighted || isEnterprise) 12.dp else 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Badge
            badge?.let {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = it.backgroundColor,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = it.text,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Trial Banner
            trialText?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = BalkanEstateGreen,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = Poppins
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Title
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                titleIcon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isEnterprise) Color.White else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isEnterprise) Color.White else MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pricing
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                originalPrice?.let { original ->
                    Text(
                        text = original,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isEnterprise) Color(0xFF94A3B8) else BalkanEstateDarkGray,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = currentPrice,
                        fontWeight = FontWeight.Bold,
                        color = if (isEnterprise) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontSize = 36.sp,
                        fontFamily = Poppins
                    )

                    if (period.isNotEmpty()) {
                        Text(
                            text = period,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (isEnterprise) Color(0xFF94A3B8) else BalkanEstateDarkGray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }

                savings?.let { savingsText ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = savingsText,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF059669)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Features
            features.forEach { feature ->
                when {
                    isEnterprise && feature.title != null &&feature.description != null -> {
                        EnterpriseFeatureItem(
                            title = feature.title,
                            description = feature.description
                        )
                    }

                    feature.text != null -> {
                        StandardFeatureItem(
                            icon = feature.icon ?: CheckIcon,
                            text = feature.text,
                            isEnterprise = isEnterprise,
                            isHighlighted = isHighlighted
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // CTA Button
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = getButtonColors(style),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Poppins
                )
            }
        }
    }
}

@Composable
private fun getButtonColors(style: SubscriptionPlan) = when (style) {
    SubscriptionPlan.PRO_ANNUAL -> ButtonDefaults.buttonColors(
        containerColor = BalkanEstateGreen,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )

    SubscriptionPlan.PRO_MONTHLY -> ButtonDefaults.outlinedButtonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary

    )

    SubscriptionPlan.ENTERPRISE -> ButtonDefaults.buttonColors(
        containerColor = BalkanEstateOrange,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )
}
