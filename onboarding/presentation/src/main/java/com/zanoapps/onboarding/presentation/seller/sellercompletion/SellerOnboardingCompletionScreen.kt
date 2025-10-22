package com.zanoapps.onboarding.presentation.seller.sellercompletion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.R
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateActionButton
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateOutlinedActionButton
import com.zanoapps.core.presentation.designsystem.components.GradientBackground
import com.zanoapps.onboarding.presentation.components.SuccessLogoComp
import com.zanoapps.onboarding.presentation.seller.OnBoardingSellerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SellerOnboardingCompletionRoot(
    viewModel: OnBoardingSellerViewModel = koinViewModel(),
    onAction: (SellerCompletionAction) -> Unit
) {

    SellerOnboardingCompletionScreen(
        state = viewModel.sellerCompletionState,
        onAction = onAction
    )


}

@Composable
fun SellerOnboardingCompletionScreen(
    state: SellerCompletionState,
    onAction: (SellerCompletionAction) -> Unit
) {
    GradientBackground(
        modifier = Modifier.fillMaxHeight()
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {


            SuccessLogoComp(
                modifier = Modifier
                    .fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.pre_final_message_seller_title),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.pre_final_message_seller_description),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(12.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {

                BalkanEstateActionButton(
                    text = stringResource(R.string.get_property_valuation),
                    isLoading = state.isLoading,
                    enabled = !state.isLoading,
                    onClick = {


                    }
                )
            }

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(R.string.benefits_create_account),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            BalkanEstateActionButton(
                text = stringResource(R.string.create_account),
                isLoading = state.isLoading,
                enabled = !state.isLoading,
                onClick = {
                    onAction(SellerCompletionAction.OnRegister)
                }
            )

            BalkanEstateOutlinedActionButton(
                onClick = {
                    onAction(SellerCompletionAction.OnBackClick)
                },
                text = stringResource(R.string.go_back),
                modifier = Modifier.padding(top = 12.dp),
                isLoading = false,
                enabled = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreFinalMessageScreenPreview() {
    BalkanEstateTheme {

        SellerOnboardingCompletionScreen(
            state = SellerCompletionState(
                isLoading = false
            ),
            onAction = {

            }
        )
    }
}