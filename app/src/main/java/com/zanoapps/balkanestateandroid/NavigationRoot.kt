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
import com.zanoapps.agent.presentation.agencies.AgenciesScreenRoot
import com.zanoapps.agent.presentation.agents.AgentsScreenRoot
import com.zanoapps.agent.presentation.detail.AgentDetailScreenRoot
import com.zanoapps.agent.presentation.detail.AgencyDetailScreenRoot
import com.zanoapps.auth.presentation.forgot.ForgotPasswordScreenRoot
import com.zanoapps.auth.presentation.login.LoginScreenRoot
import com.zanoapps.auth.presentation.register.RegisterScreenRoot
import com.zanoapps.balkanestateandroid.home.HomeNavigationCallback
import com.zanoapps.balkanestateandroid.home.HomeScreenRoot
import com.zanoapps.balkanestateandroid.utils.AuthDestinations
import com.zanoapps.balkanestateandroid.utils.MainDestinations
import com.zanoapps.balkanestateandroid.utils.OnboardingDestinations
import com.zanoapps.balkanestateandroid.utils.SearchDestinations
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateBottomNavigationBar
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateNavigationRail
import com.zanoapps.core.presentation.designsystem.components.BottomNavItem
import com.zanoapps.core.presentation.designsystem.util.rememberWindowSizeClass
import com.zanoapps.favourites.presentation.compare.PropertyComparisonScreenRoot
import com.zanoapps.favourites.presentation.favourites.FavouritesScreenRoot
import com.zanoapps.map.presentation.map.MapScreenRoot
import com.zanoapps.media.presentation.gallery.MediaGalleryScreenRoot
import com.zanoapps.messaging.presentation.inbox.InboxScreenRoot
import com.zanoapps.notification.presentation.notifications.NotificationScreenRoot
import com.zanoapps.notification.presentation.settings.NotificationSettingsScreenRoot
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
import com.zanoapps.profile.presentation.help.HelpSupportScreenRoot
import com.zanoapps.profile.presentation.legal.PrivacyPolicyScreenRoot
import com.zanoapps.profile.presentation.legal.TermsOfServiceScreenRoot
import com.zanoapps.profile.presentation.profile.ProfileScreenRoot
import com.zanoapps.profile.presentation.subscription.SubscriptionScreenRoot
import com.zanoapps.property_details.presentation.calculator.MortgageCalculatorScreenRoot
import com.zanoapps.property_details.presentation.create.CreateListingScreenRoot
import com.zanoapps.property_details.presentation.detail.PropertyDetailScreenRoot
import com.zanoapps.property_details.presentation.listings.MyListingsScreenRoot
import com.zanoapps.search.presentation.filter.FilterScreenRoot
import com.zanoapps.search.presentation.saved.SavedSearchesScreenRoot
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
        authGraph(navController)
        mainAppGraph(navController)
    }
}

// Main App Navigation Graph with bottom navigation screens
private fun NavGraphBuilder.mainAppGraph(navController: NavHostController) {
    navigation(
        startDestination = MainDestinations.HOME,
        route = MainDestinations.ROOT
    ) {
        // Home Screen (Landing page)
        composable(route = MainDestinations.HOME) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.SEARCH
            ) { _ ->
                HomeScreenRoot(
                    navigationCallback = object : HomeNavigationCallback {
                        override fun onNavigateToSearch(query: String) {
                            navController.navigate(MainDestinations.SEARCH)
                        }
                        override fun onNavigateToPropertyDetail(propertyId: String) {
                            navController.navigate(MainDestinations.propertyDetails(propertyId))
                        }
                        override fun onNavigateToCountry(country: String) {
                            navController.navigate(MainDestinations.SEARCH)
                        }
                        override fun onNavigateToCity(city: String) {
                            navController.navigate(MainDestinations.SEARCH)
                        }
                        override fun onNavigateToAgentDetail(agentId: String) {
                            navController.navigate(MainDestinations.agentDetail(agentId))
                        }
                        override fun onNavigateToAllProperties() {
                            navController.navigate(MainDestinations.SEARCH)
                        }
                        override fun onNavigateToAllAgents() {
                            navController.navigate(MainDestinations.TOP_AGENTS)
                        }
                        override fun onNavigateToPropertyType(type: String) {
                            navController.navigate(MainDestinations.SEARCH)
                        }
                    }
                )
            }
        }

        // Search Screen
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
                SavedSearchesScreenRoot()
            }
        }

        // Saved Properties / Favourites Screen
        composable(route = MainDestinations.SAVED) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.SAVED
            ) { _ ->
                FavouritesScreenRoot(
                    onNavigateToPropertyDetail = { propertyId ->
                        navController.navigate(MainDestinations.propertyDetails(propertyId))
                    },
                    onNavigateToCompare = { propertyIds ->
                        navController.navigate(MainDestinations.compareProperties(propertyIds))
                    }
                )
            }
        }

        // Inbox / Messaging Screen
        composable(route = MainDestinations.INBOX) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.INBOX
            ) { _ ->
                InboxScreenRoot()
            }
        }

        // Profile Screen
        composable(route = MainDestinations.PROFILE) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.PROFILE
            ) { _ ->
                ProfileScreenRoot(
                    onLogout = {
                        navController.navigate(AuthDestinations.ROOT) {
                            popUpTo(MainDestinations.ROOT) { inclusive = true }
                        }
                    },
                    onNavigateToSavedProperties = {
                        navController.navigate(MainDestinations.SAVED)
                    },
                    onNavigateToSavedSearches = {
                        navController.navigate(MainDestinations.SAVED_SEARCHES)
                    },
                    onNavigateToSubscription = {
                        navController.navigate(MainDestinations.SUBSCRIPTION)
                    },
                    onNavigateToMyListings = {
                        navController.navigate(MainDestinations.MY_LISTINGS)
                    },
                    onNavigateToNotificationSettings = {
                        navController.navigate(MainDestinations.NOTIFICATION_SETTINGS)
                    },
                    onNavigateToHelp = {
                        navController.navigate(MainDestinations.HELP_SUPPORT)
                    },
                    onNavigateToPrivacyPolicy = {
                        navController.navigate(MainDestinations.PRIVACY_POLICY)
                    },
                    onNavigateToTerms = {
                        navController.navigate(MainDestinations.TERMS_OF_SERVICE)
                    }
                )
            }
        }

        // Top Agents Screen
        composable(route = MainDestinations.TOP_AGENTS) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.SEARCH
            ) { _ ->
                AgentsScreenRoot(
                    onNavigateToAgentDetail = { agentId ->
                        navController.navigate(MainDestinations.agentDetail(agentId))
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
                AgenciesScreenRoot(
                    onNavigateToAgencyDetail = { agencyId ->
                        navController.navigate(MainDestinations.agencyDetail(agencyId))
                    }
                )
            }
        }

        // New Listing Screen
        composable(route = MainDestinations.NEW_LISTING) {
            CreateListingScreenRoot(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Subscription Screen
        composable(route = MainDestinations.SUBSCRIPTION) {
            SubscriptionScreenRoot(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Property Details Screen
        composable(route = MainDestinations.PROPERTY_DETAILS) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: ""
            PropertyDetailScreenRoot(
                propertyId = propertyId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProperty = { id ->
                    navController.navigate(MainDestinations.propertyDetails(id))
                },
                onNavigateToMortgageCalculator = {
                    navController.navigate(MainDestinations.MORTGAGE_CALCULATOR)
                },
                onNavigateToAgentDetail = { agentId ->
                    navController.navigate(MainDestinations.agentDetail(agentId))
                }
            )
        }

        // Notifications Screen
        composable(route = MainDestinations.NOTIFICATIONS) {
            NotificationScreenRoot(
                onNavigateToProperty = { propertyId ->
                    navController.navigate(MainDestinations.propertyDetails(propertyId))
                },
                onNavigateToSettings = {
                    navController.navigate(MainDestinations.NOTIFICATION_SETTINGS)
                }
            )
        }

        // Map Screen
        composable(route = MainDestinations.MAP) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.SEARCH
            ) { _ ->
                MapScreenRoot(
                    onNavigateToPropertyDetail = { propertyId ->
                        navController.navigate(MainDestinations.propertyDetails(propertyId))
                    }
                )
            }
        }

        // Media Gallery Screen
        composable(route = MainDestinations.MEDIA_GALLERY) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: ""
            MediaGalleryScreenRoot(
                propertyId = propertyId,
                onBack = { navController.popBackStack() }
            )
        }

        // Filter Screen
        composable(route = MainDestinations.FILTERS) {
            FilterScreenRoot(
                onNavigateBack = { navController.popBackStack() },
                onFiltersApplied = { navController.popBackStack() }
            )
        }

        // Agent Detail Screen
        composable(route = MainDestinations.AGENT_DETAIL) { backStackEntry ->
            AgentDetailScreenRoot(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToListings = { navController.navigate(MainDestinations.SEARCH) }
            )
        }

        // Agency Detail Screen
        composable(route = MainDestinations.AGENCY_DETAIL) { backStackEntry ->
            AgencyDetailScreenRoot(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Property Comparison Screen
        composable(route = MainDestinations.COMPARE_PROPERTIES) { backStackEntry ->
            PropertyComparisonScreenRoot(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPropertyDetail = { propertyId ->
                    navController.navigate(MainDestinations.propertyDetails(propertyId))
                }
            )
        }

        // Notification Settings Screen
        composable(route = MainDestinations.NOTIFICATION_SETTINGS) {
            NotificationSettingsScreenRoot(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Help & Support Screen
        composable(route = MainDestinations.HELP_SUPPORT) {
            HelpSupportScreenRoot(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Privacy Policy Screen
        composable(route = MainDestinations.PRIVACY_POLICY) {
            PrivacyPolicyScreenRoot(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Terms of Service Screen
        composable(route = MainDestinations.TERMS_OF_SERVICE) {
            TermsOfServiceScreenRoot(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // My Listings Screen
        composable(route = MainDestinations.MY_LISTINGS) {
            MyListingsScreenRoot(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCreateListing = {
                    navController.navigate(MainDestinations.NEW_LISTING)
                },
                onNavigateToEditListing = { propertyId ->
                    navController.navigate(MainDestinations.propertyDetails(propertyId))
                }
            )
        }

        // Mortgage Calculator Screen
        composable(route = MainDestinations.MORTGAGE_CALCULATOR) {
            MortgageCalculatorScreenRoot(
                onNavigateBack = { navController.popBackStack() }
            )
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
            navController.navigate(MainDestinations.NOTIFICATIONS)
        }

        override fun onNavigateToMap() {
            navController.navigate(MainDestinations.MAP)
        }

        override fun onNavigateToPropertyDetail(propertyId: String) {
            navController.navigate(MainDestinations.propertyDetails(propertyId))
        }

        override fun onNavigateToFilters() {
            navController.navigate(MainDestinations.FILTERS)
        }

        override fun onLogout() {
            navController.navigate(AuthDestinations.ROOT) {
                popUpTo(MainDestinations.ROOT) { inclusive = true }
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
                    navController.navigate(MainDestinations.ROOT) {
                        popUpTo(AuthDestinations.ROOT) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AuthDestinations.REGISTER)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(AuthDestinations.FORGOT_PASSWORD)
                }
            )
        }

        composable(route = AuthDestinations.REGISTER) {
            RegisterScreenRoot(
                onRegisterSuccess = {
                    navController.navigate(MainDestinations.ROOT) {
                        popUpTo(AuthDestinations.ROOT) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = AuthDestinations.FORGOT_PASSWORD) {
            ForgotPasswordScreenRoot(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = {
                    navController.navigate(AuthDestinations.LOGIN) {
                        popUpTo(AuthDestinations.ROOT)
                    }
                }
            )
        }
    }
}

// OnboardingNavigation.kt
private fun NavGraphBuilder.onBoardingGraph(navController: NavHostController) {
    navigation(
        startDestination = OnboardingDestinations.CLIENT_INTENT,
        route = OnboardingDestinations.ROOT,
    ) {
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

        composable(route = OnboardingDestinations.ON_BOARDING_BUYER_LIFE_SITUATION) {
            CurrentLifeSituationRoot(
                onBackClicked = { navController.navigate(OnboardingDestinations.CLIENT_INTENT) },
                onNextClicked = { navController.navigate(OnboardingDestinations.ON_BOARDING_BUYER_PROPERTY_INTENT) },
                onSkipClicked = {
                    navController.navigate(MainDestinations.ROOT) { popUpTo(OnboardingDestinations.ROOT) { inclusive = true } }
                },
                onActionCurrentLifeSituation = {},
            )
        }

        composable(route = OnboardingDestinations.ON_BOARDING_BUYER_PROPERTY_INTENT) {
            PropertyIntentScreenRoot(
                onBackClicked = { navController.popBackStack() },
                onNextClicked = { navController.navigate(OnboardingDestinations.ON_BOARDING_BUYER_AMENITIES) },
                onSkipClicked = {
                    navController.navigate(MainDestinations.ROOT) { popUpTo(OnboardingDestinations.ROOT) { inclusive = true } }
                },
                onActionPropertyIntent = {},
            )
        }

        composable(route = OnboardingDestinations.ON_BOARDING_BUYER_AMENITIES) {
            AmenitiesScreenRoot(
                onNextClicked = {
                    navController.navigate(OnboardingDestinations.ON_BOARDING_BUYER_FINAL_MESSAGE) { popUpTo(OnboardingDestinations.ROOT) { inclusive = true } }
                },
                onBackClicked = { navController.popBackStack() },
                onSkipClicked = {
                    navController.navigate(MainDestinations.ROOT) { popUpTo(OnboardingDestinations.ROOT) { inclusive = true } }
                },
                onActionOptionsSelected = {},
            )
        }

        composable(route = OnboardingDestinations.ON_BOARDING_BUYER_FINAL_MESSAGE) {
            ThankYouRoot(
                onAction = { action ->
                    when (action) {
                        ThankYouAction.OnBackClick -> navController.navigate(OnboardingDestinations.CLIENT_INTENT)
                        ThankYouAction.OnSearchPropertiesClicked -> {
                            navController.navigate(MainDestinations.ROOT) { popUpTo(OnboardingDestinations.ROOT) { inclusive = true } }
                        }
                    }
                },
            )
        }

        composable(route = OnboardingDestinations.ON_BOARDING_SELLER_PROPERTY_TYPE) {
            SellerPropertyTypeRoot(
                onActionSellerPropertyType = {},
                onNextClicked = {
                    navController.navigate(OnboardingDestinations.ON_BOARDING_SELLER_SELLING_TIME) { popUpTo(OnboardingDestinations.CLIENT_INTENT) { inclusive = true } }
                },
                onBackClicked = { navController.navigate(OnboardingDestinations.CLIENT_INTENT) },
                onSkipClicked = {
                    navController.navigate(MainDestinations.ROOT) { popUpTo(OnboardingDestinations.ROOT) { inclusive = true } }
                },
            )
        }

        composable(route = OnboardingDestinations.ON_BOARDING_SELLER_SELLING_TIME) {
            SellingTimeRoot(
                onActionSellingTime = {},
                onNextClicked = {
                    navController.navigate(OnboardingDestinations.ON_BOARDING_SELLER_MAIN_GOAL) { popUpTo(OnboardingDestinations.CLIENT_INTENT) { inclusive = true } }
                },
                onBackClicked = { navController.navigate(OnboardingDestinations.ON_BOARDING_SELLER_PROPERTY_TYPE) },
                onSkipClicked = {
                    navController.navigate(MainDestinations.ROOT) { popUpTo(OnboardingDestinations.ROOT) { inclusive = true } }
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
                onBackClicked = { navController.navigate(OnboardingDestinations.ON_BOARDING_SELLER_SELLING_TIME) },
                onSkipClicked = {
                    navController.navigate(MainDestinations.ROOT) { popUpTo(OnboardingDestinations.ROOT) { inclusive = true } }
                },
            )
        }

        composable(route = OnboardingDestinations.ON_BOARDING_SELLER_FINAL_MESSAGE) {
            SellerOnboardingCompletionRoot(
                onAction = { action ->
                    when (action) {
                        SellerCompletionAction.OnBackClick -> navController.navigate(OnboardingDestinations.CLIENT_INTENT)
                        SellerCompletionAction.OnRegister -> {
                            navController.navigate(MainDestinations.ROOT) { popUpTo(OnboardingDestinations.ROOT) { inclusive = true } }
                        }
                    }
                },
            )
        }
    }
}

// Main App Scaffold with Adaptive Navigation (Bottom Bar for mobile, Rail for tablet)
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
