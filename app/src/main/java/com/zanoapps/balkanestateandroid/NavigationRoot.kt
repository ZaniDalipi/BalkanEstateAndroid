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
import com.zanoapps.onboarding.presentation.buyer.OnBoardingBuyerViewModel
import com.zanoapps.onboarding.presentation.buyer.amenities.AmenitiesScreenRoot
import com.zanoapps.onboarding.presentation.buyer.currentlifesituation.CurrentLifeSituationRoot
import com.zanoapps.onboarding.presentation.buyer.propertyintent.PropertyIntentScreenRoot
import com.zanoapps.onboarding.presentation.clientintent.ClientIntentScreenRoot

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


// OnboardingNavigation.kt
private fun NavGraphBuilder.onBoardingGraph(navController: NavHostController) {
    navigation(
        startDestination = OnboardingDestinations.CLIENT_INTENT,
        route = OnboardingDestinations.ROOT,
    ) {
        // Screen 1: Client Intent
        composable(route = OnboardingDestinations.CLIENT_INTENT) {
            ClientIntentScreenRoot(
                onActionOptionSelectClicked = { selectedIntent ->
                    // ViewModel will be handled inside the composable
                    navController.navigate(OnboardingDestinations.ON_BOARDING_BUYER_LIFE_SITUATION)
                },
                onSkipClicked = {
                    navController.navigate(SearchDestinations.ROOT) {
                        popUpTo(OnboardingDestinations.ROOT) { inclusive = true }
                    }
                }
            )
        }

        // Screen 2: Life Situation
        composable(route = OnboardingDestinations.ON_BOARDING_BUYER_LIFE_SITUATION) {
            CurrentLifeSituationRoot(
                onBackClicked = {
                    navController.popBackStack()
                },
                onNextClicked = {
                    navController.navigate(OnboardingDestinations.ON_BOARDING_BUYER_PROPERTY_INTENT)
                },
                onSkipClicked = {
                    navController.navigate(SearchDestinations.ROOT) {
                        popUpTo(OnboardingDestinations.ROOT) { inclusive = true }
                    }
                },
                onActionCurrentLifeSituation = {

                },
            )
        }

        // Screen 3: Property Intent
        composable(route = OnboardingDestinations.ON_BOARDING_BUYER_PROPERTY_INTENT) {
            PropertyIntentScreenRoot(
                onBackClicked = {
                    navController.popBackStack()
                },
                onNextClicked = {
                    navController.navigate(OnboardingDestinations.ON_BOARDING_BUYER_AMENITIES)
                },
                onSkipClicked = {
                    navController.navigate(SearchDestinations.ROOT) {
                        popUpTo(OnboardingDestinations.ROOT) { inclusive = true }
                    }
                },
                onActionPropertyIntent = {

                },
            )
        }

        // Screen 4: Amenities
        composable(route = OnboardingDestinations.ON_BOARDING_BUYER_AMENITIES) {
            AmenitiesScreenRoot(
                onNextClicked = {
                    navController.navigate(SearchDestinations.ROOT) {
                        popUpTo(OnboardingDestinations.ROOT) { inclusive = true }
                    }
                },
                onBackClicked = {
                    navController.popBackStack()
                },
                onSkipClicked = {
                    navController.navigate(SearchDestinations.ROOT) {
                        popUpTo(OnboardingDestinations.ROOT) { inclusive = true }
                    }
                },
                onActionOptionsSelected = {

                },
            )
        }
    }
}