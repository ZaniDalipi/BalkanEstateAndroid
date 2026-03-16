package com.zanoapps.profile.presentation.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BackIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateGray
import com.zanoapps.core.presentation.designsystem.BalkanEstateGreen
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.CheckIcon
import com.zanoapps.core.presentation.designsystem.StarIcon
import androidx.compose.ui.tooling.preview.Preview
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SubscriptionScreenRoot(
    viewModel: SubscriptionViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    SubscriptionScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                SubscriptionAction.OnBackClick -> onNavigateBack()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun SubscriptionScreen(
    state: SubscriptionState,
    onAction: (SubscriptionAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onAction(SubscriptionAction.OnBackClick) }) {
                Icon(BackIcon, "Back", Modifier.size(20.dp))
            }
            Text("Subscription Plans", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(StarIcon, null, Modifier.size(48.dp), tint = BalkanEstateOrange)
            Spacer(Modifier.height(8.dp))
            Text("Choose Your Plan", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text("Unlock premium features for your property search", fontSize = 14.sp, color = BalkanEstateGray, textAlign = TextAlign.Center)

            Spacer(Modifier.height(16.dp))

            // Billing toggle
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = !state.isAnnual,
                    onClick = { onAction(SubscriptionAction.OnBillingToggle(false)) },
                    label = { Text("Monthly") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BalkanEstatePrimaryBlue, selectedLabelColor = Color.White)
                )
                FilterChip(
                    selected = state.isAnnual,
                    onClick = { onAction(SubscriptionAction.OnBillingToggle(true)) },
                    label = { Text("Annual (Save 20%)") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = BalkanEstatePrimaryBlue, selectedLabelColor = Color.White)
                )
            }

            Spacer(Modifier.height(16.dp))

            SubscriptionPlan.entries.forEach { plan ->
                PlanCard(
                    plan = plan,
                    isSelected = state.selectedPlan == plan,
                    isCurrent = state.currentPlan == plan,
                    isAnnual = state.isAnnual,
                    onClick = { onAction(SubscriptionAction.OnPlanSelected(plan)) }
                )
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { onAction(SubscriptionAction.OnSubscribe) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BalkanEstatePrimaryBlue),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isProcessing && state.selectedPlan != state.currentPlan
            ) {
                if (state.isProcessing) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text(
                        text = if (state.selectedPlan == SubscriptionPlan.FREE) "Downgrade to Free"
                        else "Subscribe to ${state.selectedPlan.displayName}",
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PlanCard(
    plan: SubscriptionPlan,
    isSelected: Boolean,
    isCurrent: Boolean,
    isAnnual: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isSelected -> BalkanEstatePrimaryBlue
        isCurrent -> BalkanEstateGreen
        else -> Color(0xFFE2E8F0)
    }
    val price = if (isAnnual) plan.annualPrice else plan.monthlyPrice
    val period = if (isAnnual) "/year" else "/month"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) BalkanEstatePrimaryBlue.copy(alpha = 0.03f) else Color.White
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(plan.displayName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
                        if (isCurrent) {
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(BalkanEstateGreen).padding(horizontal = 8.dp, vertical = 2.dp)
                            ) { Text("Current", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                        }
                        if (plan == SubscriptionPlan.PREMIUM) {
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(BalkanEstateOrange).padding(horizontal = 8.dp, vertical = 2.dp)
                            ) { Text("Popular", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                        }
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    if (price == 0.0) {
                        Text("Free", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = BalkanEstatePrimaryBlue)
                    } else {
                        Text("\u20AC${String.format("%.2f", price)}", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = BalkanEstatePrimaryBlue)
                        Text(period, fontSize = 12.sp, color = BalkanEstateGray)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            plan.features.forEach { feature ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                    Icon(CheckIcon, null, Modifier.size(16.dp), tint = BalkanEstateGreen)
                    Spacer(Modifier.width(8.dp))
                    Text(feature, fontSize = 13.sp, color = BalkanEstateGray)
                }
            }
        }
    }
}

@Preview
@Composable
private fun SubscriptionScreenPreview() {
    BalkanEstateTheme {
        SubscriptionScreen(
            state = SubscriptionState(),
            onAction = {}
        )
    }
}
