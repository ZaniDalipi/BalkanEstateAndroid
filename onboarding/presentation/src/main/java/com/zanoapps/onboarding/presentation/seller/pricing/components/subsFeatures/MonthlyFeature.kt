package com.zanoapps.onboarding.presentation.seller.pricing.components.subsFeatures

import androidx.compose.runtime.Composable
import com.zanoapps.core.presentation.designsystem.CheckIcon
import com.zanoapps.onboarding.presentation.seller.pricing.PricingFeature


@Composable
fun getMonthlyFeatures() = listOf(
    PricingFeature(CheckIcon, "Up to 3 active property ads"),
    PricingFeature(CheckIcon, "Standard listing placement"),
    PricingFeature(CheckIcon, "Basic analytics"),
    PricingFeature(CheckIcon, "Email support"),
    PricingFeature(CheckIcon, "Mobile app access")
)

