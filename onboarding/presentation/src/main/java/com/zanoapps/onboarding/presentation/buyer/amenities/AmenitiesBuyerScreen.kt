package com.zanoapps.onboarding.presentation.buyer.amenities


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
import com.zanoapps.core.presentation.designsystem.components.animations.BalkanEstateExpressiveProgressIndicator
import com.zanoapps.onboarding.domain.enums.buyer.Amenity
import com.zanoapps.onboarding.presentation.buyer.OnBoardingBuyerViewModel
import com.zanoapps.onboarding.presentation.components.BalkanEstateSelectionCard
import com.zanoapps.onboarding.presentation.components.SelectionType
import com.zanoapps.onboarding.presentation.components.SkipSurvey
import org.koin.androidx.compose.koinViewModel


@Composable
fun AmenitiesScreenRoot(
    viewModel: OnBoardingBuyerViewModel = koinViewModel(),
    onActionOptionsSelected: (Amenity) -> Unit,
    onSkipClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onNextClicked: () -> Unit
) {
    AmenitiesBuyerScreen(
        state = viewModel.amenityState,
        onAction = { action ->
            viewModel.onAmenitiesAction(action)

            when (action) {
                is AmenitiesAction.OnBackClick -> onBackClicked()
                is AmenitiesAction.OnNextClick -> onNextClicked()
                is AmenitiesAction.OnSkipClick -> onSkipClicked()
                else -> Unit
            }
        },
    )
}

@Composable
fun AmenitiesBuyerScreen(
    state: AmenityState,
    onAction: (AmenitiesAction) -> Unit,
    modifier: Modifier = Modifier
) {


    GradientBackground {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Progress indicator
            BalkanEstateExpressiveProgressIndicator(
                progress = state.progress,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "What amenities are important to you?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = Poppins,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Select all that matter to you (you can choose multiple)",
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
                items(Amenity.entries.toTypedArray()) { amenity ->
                    BalkanEstateSelectionCard(
                        title = amenity.title,
                        description = amenity.description,
                        isSelected = state.savedAmenities.contains(amenity),
                        onClick = {
                            onAction(AmenitiesAction.OnPreferenceSelected(amenity))
                        },
                        selectionType = SelectionType.CHECKBOX,
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
                        onClick = {
                            onAction(AmenitiesAction.OnBackClick)
                        },
                        text = stringResource(R.string.go_back),
                        isLoading = state.isLoading,
                        enabled = !state.isLoading,
                        modifier = Modifier.weight(1f)
                    )
                }

                BalkanEstateActionButton(
                    onClick = {
                        onAction(AmenitiesAction.OnNextClick)
                    },
                    text = stringResource(
                        R.string.next
                    ),
                    isLoading = state.isLoading,
                    enabled = state.savedAmenities.isNotEmpty() && !state.isLoading,
                    modifier = Modifier.weight(1f)

                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SkipSurvey(
                onSkip = { onAction(AmenitiesAction.OnSkipClick) }
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun AmenityScreenPreview() {
    BalkanEstateTheme {
        var selectedAmenities by remember { mutableStateOf<List<Amenity>>(emptyList()) }

        AmenitiesBuyerScreen(
            state = AmenityState(selectedAmenities),
            onAction = { action ->
                when (action) {
                    is AmenitiesAction.OnPreferenceSelected -> {
                        selectedAmenities = if (selectedAmenities.contains(action.amenity)) {
                            selectedAmenities - action.amenity
                        } else {
                            selectedAmenities + action.amenity
                        }
                    }

                    AmenitiesAction.OnBackClick -> { /* Handle back */
                    }

                    AmenitiesAction.OnNextClick -> {
                        println("Selected amenities: $selectedAmenities")
                    }

                    AmenitiesAction.OnSkipClick -> { /* Handle skip */
                    }
                }
            },
        )
    }
}