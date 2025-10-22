package com.zanoapps.onboarding.presentation.seller.pricing.components.subsFeatures

import androidx.compose.runtime.Composable
import com.zanoapps.core.presentation.designsystem.CheckIcon
import com.zanoapps.onboarding.presentation.seller.pricing.PricingFeature


fun getEnterpriseFeatures() = listOf(
    PricingFeature(
        title = "Unlimited Property Listings",
        description = "Post as many properties as you need"
    ),
    PricingFeature(
        title = "3 Priority Ads per month",
        description = "Always shown first to users"
    ),
    PricingFeature(
        title = "Dedicated Account Manager",
        description = "Personal support for your business"
    )
)