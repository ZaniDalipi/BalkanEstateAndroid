package com.zanoapps.balkanestateandroid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zanoapps.balkanestateandroid.utils.OnboardingDestinations
import com.zanoapps.balkanestateandroid.utils.SearchDestinations
import com.zanoapps.onboarding.domain.enums.buyer.LifeSituation
import com.zanoapps.onboarding.presentation.buyer.currentlifesituation.CurrentLifeSituationBuyerScreen
import com.zanoapps.onboarding.presentation.clientintent.ClientIntentScreenRoot
import kotlin.collections.plus

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = OnboardingDestinations.ROOT
    ) {
        onBoardingGraph(navController)
    }
}


private fun NavGraphBuilder.onBoardingGraph(navController: NavHostController) {
    navigation(
        startDestination = OnboardingDestinations.CLIENT_INTENT,
        route = OnboardingDestinations.ROOT,
    ) {
        composable(route = OnboardingDestinations.CLIENT_INTENT) {
            ClientIntentScreenRoot(
                onActionOptionSelectClicked = {
                    navController.navigate(OnboardingDestinations.ON_BOARDING_BUYER_LIFE_SITUATION)
                },
                onSkipClicked = {
                    navController.navigate(SearchDestinations.ROOT)

                }
            )

        }
        composable(route = OnboardingDestinations.ON_BOARDING_BUYER_LIFE_SITUATION) {
            var selectedOptions by remember { mutableStateOf<List<LifeSituation>>(emptyList()) }
            CurrentLifeSituationBuyerScreen(
                selectedOptionsLifeSituation = selectedOptions,
                onToggleBox = { lifeOption ->
                    selectedOptions = if (selectedOptions.contains(lifeOption)) {
                        selectedOptions - lifeOption
                    } else {
                        selectedOptions + lifeOption
                    }

                },
                onNext = { },
                onBack = {},
                onSkip = { },
                canNavigateBack = true,
            )

        }
    }
}
