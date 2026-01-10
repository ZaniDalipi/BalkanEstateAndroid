package com.zanoapps.balkanestateandroid

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zanoapps.agent.presentation.agents.AgentsScreenRoot
import com.zanoapps.balkanestateandroid.utils.MainDestinations
import com.zanoapps.balkanestateandroid.utils.OnboardingDestinations
import com.zanoapps.balkanestateandroid.utils.SearchDestinations
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateBottomNavigationBar
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateNavigationRail
import com.zanoapps.core.presentation.designsystem.components.BottomNavItem
import com.zanoapps.core.presentation.designsystem.util.rememberWindowSizeClass
import com.zanoapps.favourites.presentation.FavouritesScreenRoot
import com.zanoapps.messaging.presentation.inbox.InboxScreenRoot
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
import com.zanoapps.profile.presentation.ProfileScreenRoot
import com.zanoapps.property_details.presentation.create.CreateListingScreenRoot
import com.zanoapps.property_details.presentation.details.PropertyDetailsScreenRoot
import com.zanoapps.search.presentation.filters.FiltersScreenRoot
import com.zanoapps.search.presentation.savedsearches.SavedSearchesScreenRoot
import com.zanoapps.search.presentation.search.SearchNavigationCallback
import com.zanoapps.search.presentation.search.SearchPropertyScreenRoot

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = OnboardingDestinations.ROOT
    ) {
        onBoardingGraph(navController)
        mainAppGraph(navController)
    }
}

// Main App Navigation Graph with bottom navigation screens
private fun NavGraphBuilder.mainAppGraph(navController: NavHostController) {
    navigation(
        startDestination = MainDestinations.SEARCH,
        route = MainDestinations.ROOT
    ) {
        // Search Screen (Main landing screen)
        composable(route = MainDestinations.SEARCH) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.SEARCH
            ) { showDrawer ->
                SearchPropertyScreenRoot(
                    navigationCallback = createSearchNavigationCallback(navController),
                    showDrawer = showDrawer
                )
            }
        }

        // Saved Searches Screen
        composable(route = MainDestinations.SAVED_SEARCHES) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.SAVED_SEARCHES
            ) { _ ->
                SavedSearchesScreenRoot(
                    onNavigateToSearchResults = { searchId ->
                        // Navigate to search results with the saved search applied
                        navController.navigate(MainDestinations.SEARCH)
                    },
                    onNavigateToCreateSearch = {
                        navController.navigate(MainDestinations.SEARCH)
                    }
                )
            }
        }

        // Saved Properties Screen
        composable(route = MainDestinations.SAVED) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.SAVED
            ) { _ ->
                FavouritesScreenRoot(
                    onNavigateToPropertyDetails = { propertyId ->
                        navController.navigate(MainDestinations.propertyDetails(propertyId))
                    }
                )
            }
        }

        // Inbox Screen
        composable(route = MainDestinations.INBOX) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.INBOX
            ) { _ ->
                InboxScreenRoot(
                    onNavigateToChat = { conversationId ->
                        // Navigate to chat screen - for now stay on inbox
                    }
                )
            }
        }

        // Profile Screen
        composable(route = MainDestinations.PROFILE) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.PROFILE
            ) { _ ->
                ProfileScreenRoot(
                    onNavigateToFavourites = {
                        navController.navigate(MainDestinations.SAVED)
                    },
                    onNavigateToSavedSearches = {
                        navController.navigate(MainDestinations.SAVED_SEARCHES)
                    },
                    onNavigateToMyListings = {
                        navController.navigate(MainDestinations.NEW_LISTING)
                    },
                    onLogout = {
                        navController.navigate(OnboardingDestinations.ROOT) {
                            popUpTo(MainDestinations.ROOT) { inclusive = true }
                        }
                    }
                )
            }
        }

        // Property Details Screen
        composable(route = MainDestinations.PROPERTY_DETAILS) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: ""
            PropertyDetailsScreenRoot(
                propertyId = propertyId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProperty = { newPropertyId ->
                    navController.navigate(MainDestinations.propertyDetails(newPropertyId))
                }
            )
        }

        // Top Agents Screen
        composable(route = MainDestinations.TOP_AGENTS) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.SEARCH
            ) { _ ->
                AgentsScreenRoot(
                    onNavigateToAgentDetails = { agentId ->
                        // Navigate to agent details
                    },
                    onNavigateToAgencyDetails = { agencyId ->
                        // Navigate to agency details
                    }
                )
            }
        }

        // Agencies Screen
        composable(route = MainDestinations.AGENCIES) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.SEARCH
            ) { _ ->
                AgentsScreenRoot(
                    onNavigateToAgentDetails = { agentId ->
                        // Navigate to agent details
                    },
                    onNavigateToAgencyDetails = { agencyId ->
                        // Navigate to agency details
                    }
                )
            }
        }

        // New Listing Screen
        composable(route = MainDestinations.NEW_LISTING) {
            CreateListingScreenRoot(
                onNavigateBack = { navController.popBackStack() },
                onListingCreated = {
                    navController.navigate(MainDestinations.SEARCH) {
                        popUpTo(MainDestinations.ROOT)
                    }
                }
            )
        }

        // Filters Screen
        composable(route = MainDestinations.FILTERS) {
            FiltersScreenRoot(
                onNavigateBack = { navController.popBackStack() },
                onFiltersApplied = { filters ->
                    navController.popBackStack()
                }
            )
        }

        // Subscription Screen
        composable(route = MainDestinations.SUBSCRIPTION) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.PROFILE
            ) { _ ->
                // Subscription screen placeholder - can be implemented later
                Box(modifier = Modifier.fillMaxSize())
            }
        }
    }

    // Also keep SearchDestinations.ROOT for backward compatibility
    composable(route = SearchDestinations.ROOT) {
        MainAppScaffold(
            navController = navController,
            currentRoute = MainDestinations.SEARCH
        ) { showDrawer ->
            SearchPropertyScreenRoot(
                navigationCallback = createSearchNavigationCallback(navController),
                showDrawer = showDrawer
            )
        }
    }
}

// Helper function to create navigation callback for search screen
private fun createSearchNavigationCallback(navController: NavHostController): SearchNavigationCallback {
    return object : SearchNavigationCallback {
        override fun onNavigateToSavedSearches() {
            navController.navigate(MainDestinations.SAVED_SEARCHES)
        }

        override fun onNavigateToSavedProperties() {
            navController.navigate(MainDestinations.SAVED)
        }

        override fun onNavigateToTopAgents() {
            navController.navigate(MainDestinations.TOP_AGENTS)
        }

        override fun onNavigateToAgencies() {
            navController.navigate(MainDestinations.AGENCIES)
        }

        override fun onNavigateToNewListing() {
            navController.navigate(MainDestinations.NEW_LISTING)
        }

        override fun onNavigateToSubscription() {
            navController.navigate(MainDestinations.SUBSCRIPTION)
        }

        override fun onNavigateToInbox() {
            navController.navigate(MainDestinations.INBOX)
        }

        override fun onNavigateToProfile() {
            navController.navigate(MainDestinations.PROFILE)
        }

        override fun onNavigateToFavorites() {
            navController.navigate(MainDestinations.SAVED)
        }

        override fun onNavigateToNotifications() {
            navController.navigate(MainDestinations.INBOX)
        }

        override fun onLogout() {
            navController.navigate(OnboardingDestinations.ROOT) {
                popUpTo(MainDestinations.ROOT) { inclusive = true }
            }
        }
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
                    navController.navigate(MainDestinations.ROOT) {
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
                    navController.navigate(MainDestinations.ROOT) {
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
                    navController.navigate(MainDestinations.ROOT) {
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
                    navController.navigate(MainDestinations.ROOT) {
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
                            navController.navigate(MainDestinations.ROOT) {
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
                    navController.navigate(MainDestinations.ROOT) {
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
                    navController.navigate(MainDestinations.ROOT) {
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
                        popUpTo(OnboardingDestinations.ON_BOARDING_SELLER_FINAL_MESSAGE) { inclusive = true }
                    }
                },
                onBackClicked = {
                    navController.navigate(OnboardingDestinations.ON_BOARDING_SELLER_SELLING_TIME)
                },
                onSkipClicked = {
                    navController.navigate(MainDestinations.ROOT) {
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
                            navController.navigate(MainDestinations.ROOT) {
                                popUpTo(OnboardingDestinations.ROOT) { inclusive = true }
                            }
                        }
                    }
                },
            )
        }
    }
}

// Main App Scaffold with Adaptive Navigation
@Composable
private fun MainAppScaffold(
    navController: NavHostController,
    currentRoute: String,
    content: @Composable (showDrawer: Boolean) -> Unit
) {
    val windowSizeClass = rememberWindowSizeClass()
    val useNavigationRail = windowSizeClass.shouldUseNavigationRail
    val showDrawer = windowSizeClass.shouldShowDrawer

    val selectedItem = when (currentRoute) {
        MainDestinations.SEARCH -> BottomNavItem.Search
        MainDestinations.SAVED_SEARCHES -> BottomNavItem.SavedSearches
        MainDestinations.SAVED -> BottomNavItem.SavedProperties
        MainDestinations.INBOX -> BottomNavItem.Inbox
        MainDestinations.PROFILE -> BottomNavItem.Profile
        else -> BottomNavItem.Search
    }

    val onItemSelected: (BottomNavItem) -> Unit = { item ->
        val route = when (item) {
            BottomNavItem.Search -> MainDestinations.SEARCH
            BottomNavItem.SavedSearches -> MainDestinations.SAVED_SEARCHES
            BottomNavItem.SavedProperties -> MainDestinations.SAVED
            BottomNavItem.Inbox -> MainDestinations.INBOX
            BottomNavItem.Profile -> MainDestinations.PROFILE
        }
        if (route != currentRoute) {
            navController.navigate(route) {
                popUpTo(MainDestinations.ROOT) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    val onFabClick: () -> Unit = {
        navController.navigate(MainDestinations.NEW_LISTING)
    }

    if (useNavigationRail) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
        ) {
            BalkanEstateNavigationRail(
                selectedItem = selectedItem,
                onItemSelected = onItemSelected,
                onFabClick = onFabClick,
                modifier = Modifier.fillMaxHeight()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                content(showDrawer)
            }
        }
    } else {
        Scaffold(
            containerColor = Color(0xFFF8FAFC),
            bottomBar = {
                BalkanEstateBottomNavigationBar(
                    selectedItem = selectedItem,
                    onItemSelected = onItemSelected,
                    onFabClick = onFabClick,
                    showFab = true
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                content(showDrawer)
            }
        }
    }
}
