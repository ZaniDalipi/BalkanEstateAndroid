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
import com.zanoapps.onboarding.domain.enums.seller.PropertyTypeSeller
import com.zanoapps.onboarding.presentation.components.BalkanEstateSelectionCard
import com.zanoapps.onboarding.presentation.components.ProgressBar
import com.zanoapps.onboarding.presentation.components.SelectionType
import com.zanoapps.onboarding.presentation.components.SkipSurvey

@Composable
fun SellerPropertyTypeScreen(
    sellerPropertyType: List<PropertyTypeSeller>,
    onToggleSelection: (PropertyTypeSeller) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier
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
                items(PropertyTypeSeller.entries.toTypedArray()) { type ->
                    BalkanEstateSelectionCard(
                        title = type.displayName,
                        description = type.description,
                        isSelected = sellerPropertyType.contains(type),
                        onClick = {
                            onToggleSelection(type)
                        },
                        selectionType = SelectionType.CHECKBOX,
                        showSelectionIndicator = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Selected: ${sellerPropertyType.size} types",
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
                        text = stringResource(R.string.go_back),
                        isLoading = false,
                        enabled = true,
                        onClick = { },
                        modifier = Modifier.weight(1f)
                    )
                }
                BalkanEstateActionButton(
                    text = stringResource(R.string.next),
                    isLoading = false,
                    enabled = sellerPropertyType.isNotEmpty(),
                    onClick = onNext,
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
fun PropertyTypeScreenPreview() {
    BalkanEstateTheme {

        var propertiesListSelected by remember { mutableStateOf<List<PropertyTypeSeller>>(emptyList()) }

        SellerPropertyTypeScreen(
            sellerPropertyType = propertiesListSelected,
            onToggleSelection = { property ->
                propertiesListSelected = if (propertiesListSelected.contains(property)) {
                    propertiesListSelected - property
                } else {
                    propertiesListSelected + property
                }

            },
            onNext = {
                println("Selected properties: $propertiesListSelected")
            },
            onBack = { },
            onSkip = { },
            canNavigateBack = true,
        )
    }
}