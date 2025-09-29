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
import com.zanoapps.onboarding.domain.enums.seller.SellingTime
import com.zanoapps.onboarding.presentation.components.BalkanEstateSelectionCard
import com.zanoapps.onboarding.presentation.components.ProgressBar
import com.zanoapps.onboarding.presentation.components.SelectionType
import com.zanoapps.onboarding.presentation.components.SkipSurvey

@Composable
fun SellingTimeScreen(
    selectedType: SellingTime,
    onToggleSelection: (SellingTime) -> Unit,
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
                progress = 0.66f,
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
                        isSelected = false,
                        onClick = {},
                        selectionType = SelectionType.CHECKBOX,
                        showSelectionIndicator = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if(canNavigateBack) {
                    BalkanEstateOutlinedActionButton(
                        text = stringResource(R.string.go_back),
                        isLoading = false,
                        enabled = true,
                        onClick = {  },
                        modifier = Modifier.weight(1f)
                    )
                }
                BalkanEstateActionButton(
                    text = stringResource(R.string.next),
                    isLoading = false,
                    enabled = true,
                    onClick = {  },
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
fun SellingTimeScreenPreview() {
    BalkanEstateTheme {
        SellingTimeScreen(
            selectedType = SellingTime.NO_RUSH,
            onToggleSelection = { },
            onNext = { },
            onBack = { },
            onSkip = { },
            canNavigateBack = true,
        )
    }
}