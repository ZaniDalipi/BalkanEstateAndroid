package com.zanoapps.onboarding.presentation.buyer.thankyoubuyer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.CheckIcon
import com.zanoapps.core.presentation.designsystem.R
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateActionButton
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateOutlinedActionButton
import com.zanoapps.core.presentation.designsystem.components.GradientBackground
import com.zanoapps.onboarding.presentation.buyer.OnBoardingBuyerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ThankYouRoot(
    onAction: (ThankYouAction) -> Unit
) {

    ThankYouScreen(
        onAction = onAction
    )

}

@Composable
fun ThankYouScreen(
    onAction: (ThankYouAction) -> Unit,
    iconSize: Dp = 25.dp,
    modifier: Modifier = Modifier
) {
    GradientBackground(
        modifier = Modifier
    ) {

        Column(
            modifier = modifier
                .weight(1f)
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(75.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = CheckIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(iconSize)

                    )
                }

            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.thank_you_title),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text =stringResource(R.string.thank_you_description),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
            )


        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 48.dp)
        ) {

            BalkanEstateOutlinedActionButton(
                onClick = {
                    onAction(ThankYouAction.OnBackClick)
                },
                text = stringResource(R.string.go_back),
                modifier = Modifier.padding(bottom = 12.dp),
                isLoading = false,
                enabled = true
            )


            BalkanEstateActionButton(
                text = stringResource(R.string.start_searching_properties),
                isLoading = false,
                enabled = true,
                onClick = {
                    onAction(ThankYouAction.OnSearchPropertiesClicked)
                }
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreFinalMessageScreenPreview() {
    BalkanEstateTheme {

        ThankYouScreen(
            onAction = {

            }
        )
    }
}