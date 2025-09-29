package com.zanoapps.onboarding.presentation.seller.pricing.components.subsFeatures

import androidx.compose.runtime.Composable
import com.zanoapps.core.presentation.designsystem.CheckIcon
import com.zanoapps.onboarding.presentation.seller.pricing.PricingFeature

@Composable
fun getYearlyFeatures() = listOf(
    PricingFeature(CheckIcon, "Up to 10 active property ads"),
    PricingFeature(CheckIcon, "Premium listing placement"),
    PricingFeature(CheckIcon, "Advanced analytics dashboard"),
    PricingFeature(CheckIcon, "Lead management system"),
    PricingFeature(CheckIcon, "Professional photography tips"),
    PricingFeature(CheckIcon, "Priority customer support")
)



