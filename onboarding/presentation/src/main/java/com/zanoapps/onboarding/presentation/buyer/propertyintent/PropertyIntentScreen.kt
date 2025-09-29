package com.zanoapps.onboarding.presentation.buyer.propertyintent


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.Poppins
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateActionButton
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateOutlinedActionButton
import com.zanoapps.core.presentation.designsystem.components.GradientBackground
import com.zanoapps.onboarding.domain.enums.buyer.PropertyIntent
import com.zanoapps.onboarding.presentation.components.BalkanEstateSelectionCard
import com.zanoapps.onboarding.presentation.components.ProgressBar
import com.zanoapps.onboarding.presentation.components.SelectionType
import com.zanoapps.onboarding.presentation.components.SkipSurvey

@Composable
fun PropertyIntentScreen(
    propertyIntent: PropertyIntent,
    onToggleIntent: (PropertyIntent) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier
) {

    var selectedOptionRadioButton by remember { mutableStateOf("") }

    GradientBackground {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Progress indicator
            ProgressBar(
                progress = 0.6f,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Are you looking to buy or rent?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = Poppins,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "This affects the properties we'll show you",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(PropertyIntent.entries.toTypedArray()) { propertyIntent ->
                    BalkanEstateSelectionCard(
                        title = propertyIntent.title,
                        description = propertyIntent.description,
                        isSelected = false,
                        onClick = { },
                        selectionType = SelectionType.CHECKBOX,
                        showSelectionIndicator = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (canNavigateBack) {
                    BalkanEstateOutlinedActionButton(
                        onClick = onBack,
                        text = "Back",
                        isLoading = false,
                        enabled = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                BalkanEstateActionButton(
                    onClick = onNext,
                    text = "Next",
                    isLoading = false,
                    enabled = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SkipSurvey(
                onSkip = onSkip
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun PropertyIntentScreenPreview() {
    BalkanEstateTheme {
        PropertyIntentScreen(
            propertyIntent = PropertyIntent.BUY,
            onToggleIntent = {},
            onNext = {  },
            onBack = {  },
            onSkip = {  },
            canNavigateBack = true
        )
    }
}