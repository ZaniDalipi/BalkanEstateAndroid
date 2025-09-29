package com.zanoapps.onboarding.presentation.seller

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import com.zanoapps.core.presentation.designsystem.components.GradientBackground
import com.zanoapps.onboarding.presentation.components.SuccessLogoComp

@Composable
fun PreFinalMessageSellerScreen(
    onSearchPropertyClicked: () -> Unit,
    onCreateAccountClicked: () -> Unit,
    iconSize: Dp = 25.dp,
    modifier: Modifier = Modifier
) {
    GradientBackground(
        modifier = Modifier.fillMaxHeight()
    ) {

        Column(
            modifier = modifier
                .weight(1f)
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
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


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 48.dp)
            ) {

                BalkanEstateActionButton(
                    text = stringResource(R.string.get_property_valuation),
                    isLoading = false,
                    enabled = true,
                    onClick = { }
                )
            }
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(18.dp),
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
                isLoading = false,
                enabled = true,
                onClick = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreFinalMessageScreenPreview() {
    BalkanEstateTheme {

        PreFinalMessageSellerScreen(
            onSearchPropertyClicked = { },
            onCreateAccountClicked = {  },
        )
    }
}