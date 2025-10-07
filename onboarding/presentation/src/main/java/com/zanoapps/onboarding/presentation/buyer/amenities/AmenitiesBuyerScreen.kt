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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.zanoapps.core.presentation.designsystem.components.animations.BalkanEstateExpressiveProgressIndicator
import com.zanoapps.onboarding.domain.enums.buyer.Amenity
import com.zanoapps.onboarding.presentation.components.BalkanEstateSelectionCard
import com.zanoapps.onboarding.presentation.components.SelectionType
import com.zanoapps.onboarding.presentation.components.SkipSurvey

@Composable
fun AmenitiesBuyerScreen(
    selectedAmenities: List<Amenity>,
    onToggleAmenity: (Amenity) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier
) {

    var progress by remember { mutableFloatStateOf(0f) }



    GradientBackground {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Progress indicator
            BalkanEstateExpressiveProgressIndicator(
                progress = progress,
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
                        isSelected = selectedAmenities.contains(amenity),
                        onClick = {
                            onToggleAmenity(amenity)
                        },
                        selectionType = SelectionType.CHECKBOX,
                        showSelectionIndicator = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Selected: ${selectedAmenities.size} amenities",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (canNavigateBack) {
                    BalkanEstateOutlinedActionButton(
                        onClick = {
                                if (progress > 0.5f) progress -= 0.1f
                        },
                        text = "Back",
                        isLoading = false,
                        enabled = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                BalkanEstateActionButton(
                    onClick = {
                        if (progress < 1f) progress += 0.1f
                    },
                    text = "Next",
                    isLoading = false,
                    enabled = selectedAmenities.isNotEmpty(),
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
private fun AmenityScreenPreview() {
    BalkanEstateTheme {

        var selectedAmenities by remember { mutableStateOf<List<Amenity>>(emptyList()) }

        AmenitiesBuyerScreen(
            selectedAmenities = selectedAmenities,
            onToggleAmenity = { amenity ->
                selectedAmenities = if (selectedAmenities.contains(amenity)) {
                    selectedAmenities - amenity // Remove if already selected
                } else {
                    selectedAmenities + amenity // Add if not selected
                }
            },
            onNext = {
                println("Selected amenities: $selectedAmenities")
            },
            onBack = {},
            onSkip = { },
            canNavigateBack = true,
        )
    }
}