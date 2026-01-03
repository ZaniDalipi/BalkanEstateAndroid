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
import com.zanoapps.auth.presentation.login.LoginScreenRoot
import com.zanoapps.auth.presentation.register.RegisterScreenRoot
import com.zanoapps.balkanestateandroid.utils.AuthDestinations
import com.zanoapps.balkanestateandroid.utils.FavouritesDestinations
import com.zanoapps.balkanestateandroid.utils.OnboardingDestinations
import com.zanoapps.balkanestateandroid.utils.ProfileDestinations
import com.zanoapps.balkanestateandroid.utils.SearchDestinations
import com.zanoapps.balkanestateandroid.utils.MessagingDestinations
import com.zanoapps.balkanestateandroid.utils.SettingsDestinations
import com.zanoapps.balkanestateandroid.utils.NotificationDestinations
import com.zanoapps.core.domain.model.BalkanEstateProperty
import com.zanoapps.favourites.presentation.favourites.FavouritesScreenRoot
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
import com.zanoapps.profile.presentation.profile.ProfileScreenRoot
import com.zanoapps.profile.presentation.edit_profile.EditProfileScreenRoot
import com.zanoapps.property_details.presentation.property_detail.PropertyDetailScreenRoot
import com.zanoapps.search.presentation.search.SearchPropertyScreenRoot
import com.zanoapps.messaging.presentation.conversations.ConversationsScreenRoot
import com.zanoapps.messaging.presentation.chat.ChatScreenRoot
import com.zanoapps.profile.presentation.settings.SettingsScreenRoot
import com.zanoapps.notification.presentation.notifications.NotificationsScreenRoot

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    // Shared state for selected property (for passing between screens)
    var selectedProperty by remember { mutableStateOf<BalkanEstateProperty?>(null) }

    NavHost(
        navController = navController,
        startDestination = OnboardingDestinations.ROOT
    ) {
        onBoardingGraph(navController)
        authGraph(navController)
        searchGraph(navController, onPropertySelected = { selectedProperty = it })
        profileGraph(navController)
        favouritesGraph(navController, onPropertySelected = { selectedProperty = it })
        messagingGraph(navController, onPropertySelected = { selectedProperty = it })
        settingsGraph(navController)
        notificationsGraph(navController)

        // Property Details (shared across features)
        composable(route = SearchDestinations.PROPERTY_DETAILS) {
            selectedProperty?.let { property ->
                PropertyDetailScreenRoot(
                    property = property,
                    onBackClick = { navController.popBackStack() },
                    onContactAgent = { /* TODO: Open contact form */ }
                )
            }
        }
    }
}

// Auth Navigation Graph
private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        startDestination = AuthDestinations.LOGIN,
        route = AuthDestinations.ROOT
    ) {
        composable(route = AuthDestinations.LOGIN) {
            LoginScreenRoot(
                onLoginSuccess = {
                    navController.navigate(SearchDestinations.ROOT) {
                        popUpTo(AuthDestinations.ROOT) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AuthDestinations.REGISTER)
                }
            )
        }

        composable(route = AuthDestinations.REGISTER) {
            RegisterScreenRoot(
                onRegistrationSuccess = {
                    navController.navigate(AuthDestinations.LOGIN) {
                        popUpTo(AuthDestinations.REGISTER) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
    }
}

// Search Navigation Graph
private fun NavGraphBuilder.searchGraph(
    navController: NavHostController,
    onPropertySelected: (BalkanEstateProperty) -> Unit
) {
    navigation(
        startDestination = SearchDestinations.BUYER_SEARCH,
        route = SearchDestinations.ROOT
    ) {
        composable(route = SearchDestinations.BUYER_SEARCH) {
            SearchPropertyScreenRoot()
        }
    }
}

// Profile Navigation Graph
private fun NavGraphBuilder.profileGraph(navController: NavHostController) {
    navigation(
        startDestination = ProfileDestinations.PROFILE_MAIN,
        route = ProfileDestinations.ROOT
    ) {
        composable(route = ProfileDestinations.PROFILE_MAIN) {
            ProfileScreenRoot(
                onBackClick = { navController.popBackStack() },
                onEditProfileClick = {
                    navController.navigate(ProfileDestinations.EDIT_PROFILE)
                },
                onFavouritesClick = {
                    navController.navigate(FavouritesDestinations.ROOT)
                },
                onSavedSearchesClick = { /* TODO: Navigate to saved searches */ },
                onMyListingsClick = { /* TODO: Navigate to my listings */ },
                onSettingsClick = {
                    navController.navigate(SettingsDestinations.ROOT)
                },
                onLogout = {
                    navController.navigate(AuthDestinations.ROOT) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(route = ProfileDestinations.EDIT_PROFILE) {
            EditProfileScreenRoot(
                onBackClick = { navController.popBackStack() },
                onProfileUpdated = { navController.popBackStack() },
                onAccountDeleted = {
                    navController.navigate(AuthDestinations.ROOT) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

// Favourites Navigation Graph
private fun NavGraphBuilder.favouritesGraph(
    navController: NavHostController,
    onPropertySelected: (BalkanEstateProperty) -> Unit
) {
    navigation(
        startDestination = FavouritesDestinations.FAVOURITES_LIST,
        route = FavouritesDestinations.ROOT
    ) {
        composable(route = FavouritesDestinations.FAVOURITES_LIST) {
            FavouritesScreenRoot(
                onBackClick = { navController.popBackStack() },
                onPropertyClick = { property ->
                    onPropertySelected(property)
                    navController.navigate(SearchDestinations.propertyDetails(property.id))
                },
                onExploreClick = {
                    navController.navigate(SearchDestinations.ROOT) {
                        popUpTo(FavouritesDestinations.ROOT) { inclusive = true }
                    }
                }
            )
        }
    }
}

// Onboarding Navigation Graph
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

        // Buyer Flow
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
                onActionCurrentLifeSituation = {},
            )
        }

        composable(route = OnboardingDestinations.ON_BOARDING_BUYER_PROPERTY_INTENT) {
            PropertyIntentScreenRoot(
                onBackClicked = { navController.popBackStack() },
                onNextClicked = {
                    navController.navigate(OnboardingDestinations.ON_BOARDING_BUYER_AMENITIES)
                },
                onSkipClicked = {
                    navController.navigate(SearchDestinations.ROOT) {
                        popUpTo(OnboardingDestinations.ROOT) { inclusive = true }
                    }
                },
                onActionPropertyIntent = {},
            )
        }

        composable(route = OnboardingDestinations.ON_BOARDING_BUYER_AMENITIES) {
            AmenitiesScreenRoot(
                onNextClicked = {
                    navController.navigate(OnboardingDestinations.ON_BOARDING_BUYER_FINAL_MESSAGE) {
                        popUpTo(OnboardingDestinations.ROOT) { inclusive = true }
                    }
                },
                onBackClicked = { navController.popBackStack() },
                onSkipClicked = {
                    navController.navigate(SearchDestinations.ROOT) {
                        popUpTo(OnboardingDestinations.ROOT) { inclusive = true }
                    }
                },
                onActionOptionsSelected = {},
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
                            navController.navigate(SearchDestinations.ROOT) {
                                popUpTo(OnboardingDestinations.ROOT) { inclusive = true }
                            }
                        }
                    }
                },
            )
        }

        // Seller Flow
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

        composable(route = OnboardingDestinations.ON_BOARDING_SELLER_FINAL_MESSAGE) {
            SellerOnboardingCompletionRoot(
                onAction = { action ->
                    when (action) {
                        SellerCompletionAction.OnBackClick -> {
                            navController.navigate(OnboardingDestinations.CLIENT_INTENT)
                        }
                        SellerCompletionAction.OnRegister -> {
                            navController.navigate(AuthDestinations.ROOT) {
                                popUpTo(OnboardingDestinations.ROOT) { inclusive = true }
                            }
                        }
                    }
                },
            )
        }
    }
}

// Messaging Navigation Graph
private fun NavGraphBuilder.messagingGraph(
    navController: NavHostController,
    onPropertySelected: (BalkanEstateProperty) -> Unit
) {
    navigation(
        startDestination = MessagingDestinations.CONVERSATIONS_LIST,
        route = MessagingDestinations.ROOT
    ) {
        composable(route = MessagingDestinations.CONVERSATIONS_LIST) {
            ConversationsScreenRoot(
                onBackClick = { navController.popBackStack() },
                onNavigateToChat = { conversationId ->
                    navController.navigate(MessagingDestinations.chat(conversationId))
                }
            )
        }

        composable(route = MessagingDestinations.CHAT) {
            ChatScreenRoot(
                onBackClick = { navController.popBackStack() },
                onNavigateToProperty = { propertyId ->
                    navController.navigate(SearchDestinations.propertyDetails(propertyId))
                }
            )
        }
    }
}

// Settings Navigation Graph
private fun NavGraphBuilder.settingsGraph(navController: NavHostController) {
    navigation(
        startDestination = SettingsDestinations.SETTINGS_MAIN,
        route = SettingsDestinations.ROOT
    ) {
        composable(route = SettingsDestinations.SETTINGS_MAIN) {
            SettingsScreenRoot(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

// Notifications Navigation Graph
private fun NavGraphBuilder.notificationsGraph(navController: NavHostController) {
    navigation(
        startDestination = NotificationDestinations.NOTIFICATIONS_LIST,
        route = NotificationDestinations.ROOT
    ) {
        composable(route = NotificationDestinations.NOTIFICATIONS_LIST) {
            NotificationsScreenRoot(
                onBackClick = { navController.popBackStack() },
                onNavigateToMessage = { conversationId ->
                    navController.navigate(MessagingDestinations.chat(conversationId))
                },
                onNavigateToProperty = { propertyId ->
                    navController.navigate(SearchDestinations.propertyDetails(propertyId))
                },
                onNavigateToSavedSearch = { searchId ->
                    // TODO: Navigate to saved search results
                }
            )
        }
    }
}
