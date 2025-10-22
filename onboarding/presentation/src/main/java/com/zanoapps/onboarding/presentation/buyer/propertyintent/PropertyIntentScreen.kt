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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.Poppins
import com.zanoapps.core.presentation.designsystem.R
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateActionButton
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateOutlinedActionButton
import com.zanoapps.core.presentation.designsystem.components.GradientBackground
import com.zanoapps.onboarding.domain.enums.buyer.PropertyIntent
import com.zanoapps.onboarding.presentation.buyer.OnBoardingBuyerViewModel
import com.zanoapps.onboarding.presentation.buyer.amenities.AmenitiesAction
import com.zanoapps.onboarding.presentation.components.BalkanEstateSelectionCard
import com.zanoapps.onboarding.presentation.components.ProgressBar
import com.zanoapps.onboarding.presentation.components.SelectionType
import com.zanoapps.onboarding.presentation.components.SkipSurvey
import org.koin.androidx.compose.koinViewModel


@Composable
fun PropertyIntentScreenRoot(
    viewModel: OnBoardingBuyerViewModel = koinViewModel(),
    onActionPropertyIntent: (PropertyIntent) -> Unit,
    onSkipClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onNextClicked: () -> Unit,

    ) {

    PropertyIntentScreen(
        state = viewModel.propertyIntentState,
        onAction = { action ->
            viewModel.onPropertyIntentAction(action)
            when (action) {
                PropertyIntentAction.OnBackClick -> onBackClicked()
                PropertyIntentAction.OnNextClick -> onNextClicked()
                PropertyIntentAction.OnSkipClick -> onSkipClicked()
                else -> Unit
            }

        }
    )


}

@Composable
fun PropertyIntentScreen(
    state: PropertyIntentState,
    onAction: (PropertyIntentAction) -> Unit,
    modifier: Modifier = Modifier
) {


    GradientBackground {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Progress indicator
            ProgressBar(
                progress = state.progress,
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
                        isSelected = state.propertyIntent == propertyIntent,
                        onClick = {
                            onAction(PropertyIntentAction.OnPreferenceSelected(propertyIntent))
                        },
                        selectionType = SelectionType.RADIO,
                        showSelectionIndicator = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (state.canNavigateBack) {
                    BalkanEstateOutlinedActionButton(
                        onClick = {
                            onAction(PropertyIntentAction.OnBackClick)
                        },
                        text = stringResource(R.string.go_back),
                        isLoading = state.isLoading,
                        enabled = !state.isLoading,
                        modifier = Modifier.weight(1f)
                    )
                }

                BalkanEstateActionButton(
                    onClick = {
                        onAction(PropertyIntentAction.OnNextClick)
                    },
                    text = stringResource(R.string.next),
                    isLoading = state.isLoading,
                    enabled = state.propertyIntent != null && !state.isLoading,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SkipSurvey(
                onSkip = { onAction(PropertyIntentAction.OnSkipClick) }
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun PropertyIntentScreenPreview() {
    BalkanEstateTheme {

        var selectedOptions by remember { mutableStateOf(PropertyIntent.BUY_TO_INVEST) }


        PropertyIntentScreen(
            state = PropertyIntentState(selectedOptions),
            onAction = { action ->
                when (action) {
                    is PropertyIntentAction.OnPreferenceSelected -> {

                    }

                    AmenitiesAction.OnBackClick -> { /* Handle back */
                    }

                    AmenitiesAction.OnNextClick -> {
                        println("Selected amenities: $selectedOptions")
                    }

                    AmenitiesAction.OnSkipClick -> {

                    }

                    PropertyIntentAction.OnBackClick -> {

                    }

                    PropertyIntentAction.OnNextClick -> {

                    }

                    PropertyIntentAction.OnSkipClick -> {

                    }
                }
            },
        )

    }

}