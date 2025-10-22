package com.zanoapps.balkanestateandroid

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zanoapps.balkanestateandroid.utils.OnboardingDestinations
import com.zanoapps.balkanestateandroid.utils.SearchDestinations
import com.zanoapps.onboarding.presentation.buyer.amenities.AmenitiesScreenRoot
import com.zanoapps.onboarding.presentation.buyer.currentlifesituation.CurrentLifeSituationRoot
import com.zanoapps.onboarding.presentation.buyer.propertyintent.PropertyIntentScreenRoot
import com.zanoapps.onboarding.presentation.buyer.thankyoubuyer.ThankYouAction
import com.zanoapps.onboarding.presentation.buyer.thankyoubuyer.ThankYouRoot
import com.zanoapps.onboarding.presentation.clientintent.ClientIntentScreenRoot
import com.zanoapps.onboarding.presentation.seller.maingoal.SellerMainGoalRoot
import com.zanoapps.onboarding.presentation.seller.propertytype.SellerPropertyTypeRoot
import com.zanoapps.onboarding.presentation.seller.sellercompletion.SellerCompletionAction
import com.zanoapps.onboarding.presentation.seller.sellercompletion.SellerOnboardingCompletionRoot
import com.zanoapps.onboarding.presentation.seller.sellingtime.SellingTimeRoot

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
                onNavigateToBuyRentPath = {
                    navController.navigate(OnboardingDestinations.ON_BOARDING_BUYER_LIFE_SITUATION) {
                        popUpTo(OnboardingDestinations.CLIENT_INTENT) { inclusive = true }
                    }
                },
                onNavigateToSellPath = {
                    navController.navigate(OnboardingDestinations.ON_BOARDING_SELLER_PROPERTY_TYPE) {
                        popUpTo(OnboardingDestinations.CLIENT_INTENT) { inclusive = true }
                    }
                },
                onSkipClicked = {
                    navController.navigate(SearchDestinations.ROOT) {
                        popUpTo(OnboardingDestinations.CLIENT_INTENT) { inclusive = true }
                    }
                },
            )

        }

        //Buyer
        // Screen 2: Life Situation
        composable(route = OnboardingDestinations.ON_BOARDING_BUYER_LIFE_SITUATION) {
            CurrentLifeSituationRoot(
                onBackClicked = {
                    navController.navigate(OnboardingDestinations.CLIENT_INTENT)
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
                    navController.navigate(OnboardingDestinations.ON_BOARDING_BUYER_FINAL_MESSAGE) {
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

        composable(route = OnboardingDestinations.ON_BOARDING_BUYER_FINAL_MESSAGE) {
            ThankYouRoot(
                onAction = { action ->
                    when (action) {
                        ThankYouAction.OnBackClick -> {
                            navController.navigate(OnboardingDestinations.CLIENT_INTENT)
                        }

                        ThankYouAction.OnSearchPropertiesClicked -> {

                        }
                    }
                },
            )

        }


        /* Seller */


        // Screen 1: Property Type
        composable(route = OnboardingDestinations.ON_BOARDING_SELLER_PROPERTY_TYPE) {
            SellerPropertyTypeRoot(
                onActionSellerPropertyType = {},
                onNextClicked = {
                    navController.navigate(OnboardingDestinations.ON_BOARDING_SELLER_SELLING_TIME) {
                        popUpTo(OnboardingDestinations.CLIENT_INTENT) { inclusive = true }
                    }
                },
                onBackClicked = {
                    navController.navigate(OnboardingDestinations.CLIENT_INTENT)
                },
                onSkipClicked = {
                    navController.navigate(SearchDestinations.ROOT) {
                        popUpTo(OnboardingDestinations.ROOT) { inclusive = true }
                    }
                },
            )
        }

        // Screen 2: SellingTime
        composable(route = OnboardingDestinations.ON_BOARDING_SELLER_SELLING_TIME) {
            SellingTimeRoot(
                onActionSellingTime = {},
                onNextClicked = {
                    navController.navigate(OnboardingDestinations.ON_BOARDING_SELLER_MAIN_GOAL) {
                        popUpTo(OnboardingDestinations.CLIENT_INTENT) { inclusive = true }
                    }
                },
                onBackClicked = {
                    navController.navigate(OnboardingDestinations.ON_BOARDING_SELLER_PROPERTY_TYPE)
                },
                onSkipClicked = {
                    navController.navigate(SearchDestinations.ROOT) {
                        popUpTo(OnboardingDestinations.ROOT) { inclusive = true }
                    }
                },
            )
        }

        // Screen 3: Main Goal
        composable(route = OnboardingDestinations.ON_BOARDING_SELLER_MAIN_GOAL) {
            SellerMainGoalRoot(
                onActionMainGoal = {},
                onNextClicked = {
                    navController.navigate(OnboardingDestinations.ON_BOARDING_SELLER_FINAL_MESSAGE) {
                        popUpTo(OnboardingDestinations.ON_BOARDING_SELLER_FINAL_MESSAGE) {
                            inclusive = true
                        }
                    }
                },
                onBackClicked = {
                    navController.navigate(OnboardingDestinations.ON_BOARDING_SELLER_SELLING_TIME)
                },
                onSkipClicked = {
                    navController.navigate(SearchDestinations.ROOT) {
                        popUpTo(OnboardingDestinations.ROOT) { inclusive = true }
                    }
                },
            )
        }

        // Screen 4: Final Message
        composable(route = OnboardingDestinations.ON_BOARDING_SELLER_FINAL_MESSAGE) {
            SellerOnboardingCompletionRoot(
                onAction = { action ->
                    when (action) {
                        SellerCompletionAction.OnBackClick -> {
                            navController.navigate(OnboardingDestinations.CLIENT_INTENT)
                        }

                        SellerCompletionAction.OnRegister -> {
                        }

                    }
                },
            )
        }
    }
}