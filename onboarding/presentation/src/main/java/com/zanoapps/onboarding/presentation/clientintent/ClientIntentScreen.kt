package com.zanoapps.onboarding.presentation.clientintent


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zanoapps.core.presentation.designsystem.BalkanEstateLogo
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.core.presentation.designsystem.EnterpriseIcon
import com.zanoapps.core.presentation.designsystem.HomeIcon
import com.zanoapps.core.presentation.designsystem.components.GradientBackground
import com.zanoapps.onboarding.domain.enums.ClientIntent
import com.zanoapps.onboarding.presentation.R
import com.zanoapps.onboarding.presentation.components.BalkanEstateSelectionCard
import com.zanoapps.onboarding.presentation.components.SelectionType
import org.koin.androidx.compose.koinViewModel

@Composable

fun ClientIntentScreenRoot(

    viewModel: ClientIntentViewModel  = koinViewModel(),
    onActionOptionSelectClicked: (ClientIntent) -> Unit,
    onSkipClicked: () -> Unit
) {

    ClientIntentScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is ClientIntentAction.OnOptionSelected -> {
                    viewModel.onAction(action)
                    onActionOptionSelectClicked(action.intent)
                }

                ClientIntentAction.OnSkipClick -> {
                    viewModel.onAction(action)
                    onSkipClicked()
                }
                else -> Unit
            }
        }
    )
}


@Composable
fun ClientIntentScreen(
    state: ClientIntentState,
    onAction: (ClientIntentAction) -> Unit,
    modifier: Modifier = Modifier
) {
    GradientBackground {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(18.dp)
        ) {


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = BalkanEstateLogo,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)

            ) {
                Text(
                    text = stringResource(R.string.welcome_to_property_journey_title),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.welcome_to_property_journey_description),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    BalkanEstateSelectionCard(
                        title = ClientIntent.BUY_RENT.title,
                        description = ClientIntent.BUY_RENT.description,
                        selectionType = SelectionType.NONE,
                        icon = EnterpriseIcon,
                        isSelected = state.clientSelectedOption == ClientIntent.BUY_RENT,
                        onClick = {
                            onAction(ClientIntentAction.OnOptionSelected(ClientIntent.BUY_RENT))
                        }
                    )
                    BalkanEstateSelectionCard(
                        title = ClientIntent.SELL.title,
                        description = ClientIntent.SELL.description,
                        selectionType = SelectionType.NONE,
                        icon = HomeIcon,
                        isSelected = state.clientSelectedOption == ClientIntent.SELL,
                        onClick = {
                            onAction(ClientIntentAction.OnOptionSelected(ClientIntent.SELL))
                        }
                    )
                }

            }



            Spacer(modifier = Modifier.height(16.dp))


            TextButton(
                onClick = {
                    onAction(ClientIntentAction.OnSkipClick)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(R.string.skip_survey),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Screen() {
    BalkanEstateTheme {

        var selectedOption by remember {
            mutableStateOf(ClientIntent.BUY_RENT)
        }

        ClientIntentScreen(
           state = ClientIntentState(selectedOption),
            onAction = { }
        )

    }

}