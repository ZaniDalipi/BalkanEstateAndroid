package com.zanoapps.onboarding.presentation.buyer.currentlifesituation


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
import androidx.compose.ui.Alignment
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
import com.zanoapps.onboarding.domain.enums.buyer.LifeSituation
import com.zanoapps.onboarding.presentation.buyer.OnBoardingBuyerViewModel
import com.zanoapps.onboarding.presentation.components.BalkanEstateSelectionCard
import com.zanoapps.onboarding.presentation.components.ProgressBar
import com.zanoapps.onboarding.presentation.components.SelectionType
import com.zanoapps.onboarding.presentation.components.SkipSurvey
import org.koin.androidx.compose.koinViewModel


@Composable
fun CurrentLifeSituationRoot(
    viewModel: OnBoardingBuyerViewModel = koinViewModel(),
    onActionCurrentLifeSituation: (LifeSituation) -> Unit,
    onBackClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onSkipClicked: () -> Unit,
    ) {

    CurrentLifeSituationBuyerScreen(
        state = viewModel.lifeSituation,
        onAction = { action ->
            viewModel.onLifeSituationAction(action)

            // Handle navigation actions
            when (action) {
                is CurrentLifeSituationAction.OnBackClick -> onBackClicked()
                is CurrentLifeSituationAction.OnNextClick -> onNextClicked()
                is CurrentLifeSituationAction.OnSkipClick -> onSkipClicked()
                else -> Unit
            }
        },
    )

    
}

@Composable
fun CurrentLifeSituationBuyerScreen(
    state: CurrentLifeSituationBuyerState,
    onAction: (CurrentLifeSituationAction) -> Unit,
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
                text = "What's your current life situation?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = Poppins,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "This helps us understand your housing needs better",
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
                items(LifeSituation.entries.toTypedArray()) { lifeSituation ->
                    BalkanEstateSelectionCard(
                        title = lifeSituation.title,
                        description = lifeSituation.description,
                        isSelected = state.savedLifeSituation == lifeSituation,
                        onClick = {
                            onAction(CurrentLifeSituationAction.OnPreferenceSelected(lifeSituation))
                        },
                        selectionType = SelectionType.RADIO,
                        showSelectionIndicator = true
                    )
                }
            }



            Spacer(modifier = Modifier.height(16.dp))

            // Navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (state.canNavigateBack) {
                    BalkanEstateOutlinedActionButton(
                        onClick = {
                            onAction(CurrentLifeSituationAction.OnBackClick)
                        },
                        text = stringResource(R.string.go_back),
                        isLoading = state.isLoading,
                        enabled = !state.isLoading,
                        modifier = Modifier.weight(1f)
                    )
                }

                BalkanEstateActionButton(
                    onClick = {
                        onAction(CurrentLifeSituationAction.OnNextClick)
                    },
                    text = stringResource(R.string.next),
                    isLoading = state.isLoading,
                    enabled = state.savedLifeSituation != null && !state.isLoading,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SkipSurvey(
                onSkip = { onAction(CurrentLifeSituationAction.OnSkipClick) }
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun CurrentLifeSituationScreenPreview() {
    BalkanEstateTheme {

        var selectedOptions by remember { mutableStateOf(LifeSituation.GROWING_FAMILY) }
        CurrentLifeSituationBuyerScreen(
            state = CurrentLifeSituationBuyerState(selectedOptions),
            onAction = {currentLifeSituationAction ->
                when(currentLifeSituationAction) {
                    CurrentLifeSituationAction.OnBackClick -> TODO()
                    CurrentLifeSituationAction.OnNextClick -> TODO()
                    CurrentLifeSituationAction.OnSkipClick -> TODO()
                    is CurrentLifeSituationAction.OnPreferenceSelected -> TODO()
                }
            }
        )
    }
}