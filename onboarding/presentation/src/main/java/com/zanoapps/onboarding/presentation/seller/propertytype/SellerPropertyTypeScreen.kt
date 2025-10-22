package com.zanoapps.onboarding.presentation.seller.propertytype

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
import com.zanoapps.onboarding.domain.enums.seller.PropertyTypeSeller
import com.zanoapps.onboarding.presentation.buyer.currentlifesituation.CurrentLifeSituationAction
import com.zanoapps.onboarding.presentation.components.BalkanEstateSelectionCard
import com.zanoapps.onboarding.presentation.components.ProgressBar
import com.zanoapps.onboarding.presentation.components.SelectionType
import com.zanoapps.onboarding.presentation.components.SkipSurvey
import com.zanoapps.onboarding.presentation.seller.OnBoardingSellerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SellerPropertyTypeRoot(
    viewModel: OnBoardingSellerViewModel = koinViewModel(),
    onActionSellerPropertyType: (PropertyTypeSeller) -> Unit,
    onSkipClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onNextClicked: () -> Unit,
    ) {
    SellerPropertyTypeScreen (
        state = viewModel.propertyTypeState,
        onAction = { action ->
            viewModel.onPropertyTypeAction(action)
            when (action) {
                SellerPropertyTypeAction.OnBackClick -> onBackClicked()
                SellerPropertyTypeAction.OnNextClick -> onNextClicked()
                SellerPropertyTypeAction.OnSkipClick -> onSkipClicked()
                else -> Unit
            }

        }
    )

}

@Composable
fun SellerPropertyTypeScreen(
    state: PropertyTypeState,
    onAction: (SellerPropertyTypeAction) -> Unit,
) {
    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            ProgressBar(
                progress = 0.33f,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(R.string.propertyTypeTitle),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = Poppins
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.propertyTypeDescription),
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
                items(PropertyTypeSeller.entries.toTypedArray()) { propertyTypeSeller ->
                    BalkanEstateSelectionCard(
                        title = propertyTypeSeller.displayName,
                        description = propertyTypeSeller.description,
                        isSelected = state.propertyTypeSeller == propertyTypeSeller,
                        onClick = {
                            onAction(SellerPropertyTypeAction.OnPreferenceSelected(propertyTypeSeller))
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
                if (state.canNavigateBack) {
                    BalkanEstateOutlinedActionButton(
                        text = stringResource(R.string.go_back),
                        onClick = {
                            onAction(SellerPropertyTypeAction.OnBackClick)
                        },
                        isLoading = state.isLoading,
                        enabled = !state.isLoading,
                        modifier = Modifier.weight(1f)
                    )
                }
                BalkanEstateActionButton(
                    text = stringResource(R.string.next),
                    onClick = {
                        onAction(SellerPropertyTypeAction.OnNextClick)
                    },
                    isLoading = state.isLoading,
                    enabled = state.propertyTypeSeller != null && !state.isLoading,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SkipSurvey(
                onSkip = { SellerPropertyTypeAction.OnSkipClick}
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PropertyTypeScreenPreview() {
    BalkanEstateTheme {
        var selectedOptions by remember { mutableStateOf(PropertyTypeSeller.VILLA_LUXURY_HOME) }

        SellerPropertyTypeScreen (
            state = PropertyTypeState(selectedOptions),
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