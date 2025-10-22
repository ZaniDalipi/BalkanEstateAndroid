package com.zanoapps.onboarding.presentation.seller.sellingtime

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
import com.zanoapps.onboarding.domain.enums.seller.PropertyTypeSeller
import com.zanoapps.onboarding.domain.enums.seller.SellingTime
import com.zanoapps.onboarding.presentation.components.BalkanEstateSelectionCard
import com.zanoapps.onboarding.presentation.components.ProgressBar
import com.zanoapps.onboarding.presentation.components.SelectionType
import com.zanoapps.onboarding.presentation.components.SkipSurvey
import com.zanoapps.onboarding.presentation.seller.OnBoardingSellerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SellingTimeRoot(
    viewModel: OnBoardingSellerViewModel = koinViewModel(),
    onActionSellingTime: (SellingTime) -> Unit,
    onSkipClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onNextClicked: () -> Unit,
) {
    SellingTimeScreen(
        state = viewModel.sellingTimeState,
        onAction = {sellingTimeAction ->
            viewModel.onSellingTimeAction(sellingTimeAction)
            when(sellingTimeAction){
                SellingTimeAction.OnBackClick -> onBackClicked()
                SellingTimeAction.OnNextClick -> onNextClicked()
                SellingTimeAction.OnSkipClick -> onSkipClicked()
                else -> Unit
            }
        }
    )

}

@Composable
fun SellingTimeScreen(
    state: SellingTimeState,
    onAction: (SellingTimeAction) -> Unit
) {
    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            ProgressBar(
                progress = state.progress,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(R.string.sellingTimeTitle),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = Poppins
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.sellingTimeDescription),
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
                items(SellingTime.entries.toTypedArray()) { sellingTime ->
                    BalkanEstateSelectionCard(
                        title = sellingTime.displayName,
                        description = sellingTime.description,
                        isSelected = state.sellingTime == sellingTime,
                        onClick = {
                            onAction(SellingTimeAction.OnPreferenceSelected(sellingTime))
                        },
                        selectionType = SelectionType.RADIO,
                        showSelectionIndicator = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if(state.canNavigateBack) {
                    BalkanEstateOutlinedActionButton(
                        text = stringResource(R.string.go_back),
                        onClick = {
                            onAction(SellingTimeAction.OnBackClick)
                        },
                        isLoading = state.isLoading,
                        enabled = !state.isLoading,
                        modifier = Modifier.weight(1f)
                    )
                }
                BalkanEstateActionButton(
                    text = stringResource(R.string.next),
                    isLoading = state.isLoading,
                    enabled = state.sellingTime != null && !state.isLoading,
                    onClick = {
                        onAction(SellingTimeAction.OnNextClick)
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SkipSurvey(
                onSkip = {
                    onAction(SellingTimeAction.OnSkipClick)
                }
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SellingTimeScreenPreview() {
    BalkanEstateTheme {

        var selectedOptions by remember { mutableStateOf(SellingTime.THREE_MONTHS) }

        SellingTimeScreen (
            state = SellingTimeState(selectedOptions),
            onAction = {sellingTime ->
                when(sellingTime) {
                    SellingTimeAction.OnBackClick -> TODO()
                    SellingTimeAction.OnNextClick -> TODO()
                    SellingTimeAction.OnSkipClick -> TODO()
                    else -> Unit
                }
            }
        )

    }
}