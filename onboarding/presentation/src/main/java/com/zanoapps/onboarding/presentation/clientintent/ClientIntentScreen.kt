package com.zanoapps.onboarding.presentation.clientintent


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.zanoapps.core.presentation.designsystem.components.GradientBackground
import com.zanoapps.onboarding.domain.enums.ClientIntent
import com.zanoapps.onboarding.presentation.R
import com.zanoapps.onboarding.presentation.components.BalkanEstateSelectionCard
import com.zanoapps.onboarding.presentation.components.SelectionType
import org.koin.androidx.compose.koinViewModel

@Composable

fun ClientIntentScreenRoot(

    viewModel: ClientIntentViewModel = koinViewModel(),
    onNavigateToBuyRentPath: () -> Unit,
    onNavigateToSellPath: () -> Unit,
    onSkipClicked: () -> Unit
) {

    val state = viewModel.state

    LaunchedEffect(state.shouldNavigate) {
        if (state.shouldNavigate) {
            when (state.navigationPath) {
                "buy_rent_path" -> onNavigateToBuyRentPath()
                "sell_path" -> onNavigateToSellPath()
                "skip_path" -> onSkipClicked()
            }
            viewModel.resetNavigation()
        }
    }

    ClientIntentScreen(
        state = state,
        onAction = { action ->
            viewModel.onAction(action)
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

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(ClientIntent.entries.toTypedArray()) { clientIntent ->
                        BalkanEstateSelectionCard(
                            title = clientIntent.title,
                            description = clientIntent.description,
                            isSelected = state.clientSelectedOption == clientIntent,
                            onClick = {

                                onAction(ClientIntentAction.OnOptionSelected(clientIntent))
                            },
                            selectionType = SelectionType.NONE,
                            showSelectionIndicator = true
                        )
                    }
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