package com.zanoapps.balkanestateandroid

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zanoapps.balkanestateandroid.utils.MainDestinations
import com.zanoapps.balkanestateandroid.utils.OnboardingDestinations
import com.zanoapps.balkanestateandroid.utils.SearchDestinations
import com.zanoapps.core.presentation.designsystem.AddSearchIcon
import com.zanoapps.core.presentation.designsystem.AgencyIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateOrange
import com.zanoapps.core.presentation.designsystem.BalkanEstatePrimaryBlue
import com.zanoapps.core.presentation.designsystem.BalkanEstateRed
import com.zanoapps.core.presentation.designsystem.EditPenIcon
import com.zanoapps.core.presentation.designsystem.HomeIcon
import com.zanoapps.core.presentation.designsystem.InboxIcon
import com.zanoapps.core.presentation.designsystem.LogoutIcon
import com.zanoapps.core.presentation.designsystem.PersonIcon
import com.zanoapps.core.presentation.designsystem.SavedHomesIcon
import com.zanoapps.core.presentation.designsystem.StarIcon
import com.zanoapps.core.presentation.designsystem.components.BalkanEstateBottomNavigationBar
import com.zanoapps.core.presentation.designsystem.components.BottomNavItem
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
        startDestination = MainDestinations.HOME,
        route = MainDestinations.ROOT
    ) {
        // Home Screen
        composable(route = MainDestinations.HOME) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.HOME
            ) {
                HomeScreenContent(
                    onNavigateToSearch = {
                        navController.navigate(MainDestinations.SEARCH)
                    }
                )
            }
        }

        // Search Screen
        composable(route = MainDestinations.SEARCH) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.SEARCH
            ) {
                SearchPropertyScreenRoot(
                    navigationCallback = createSearchNavigationCallback(navController)
                )
            }
        }

        // Saved Properties Screen
        composable(route = MainDestinations.SAVED) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.SAVED
            ) {
                SavedPropertiesScreenContent()
            }
        }

        // Inbox Screen
        composable(route = MainDestinations.INBOX) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.INBOX
            ) {
                InboxScreenContent()
            }
        }

        // Profile Screen
        composable(route = MainDestinations.PROFILE) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.PROFILE
            ) {
                ProfileScreenContent(
                    onLogout = {
                        navController.navigate(OnboardingDestinations.ROOT) {
                            popUpTo(MainDestinations.ROOT) { inclusive = true }
                        }
                    }
                )
            }
        }

        // Saved Searches Screen
        composable(route = MainDestinations.SAVED_SEARCHES) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.SAVED
            ) {
                SavedSearchesScreenContent()
            }
        }

        // Top Agents Screen
        composable(route = MainDestinations.TOP_AGENTS) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.HOME
            ) {
                TopAgentsScreenContent()
            }
        }

        // Agencies Screen
        composable(route = MainDestinations.AGENCIES) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.HOME
            ) {
                AgenciesScreenContent()
            }
        }

        // New Listing Screen
        composable(route = MainDestinations.NEW_LISTING) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.PROFILE
            ) {
                NewListingScreenContent()
            }
        }

        // Subscription Screen
        composable(route = MainDestinations.SUBSCRIPTION) {
            MainAppScaffold(
                navController = navController,
                currentRoute = MainDestinations.PROFILE
            ) {
                SubscriptionScreenContent()
            }
        }
    }

    // Also keep SearchDestinations.ROOT for backward compatibility
    composable(route = SearchDestinations.ROOT) {
        MainAppScaffold(
            navController = navController,
            currentRoute = MainDestinations.SEARCH
        ) {
            SearchPropertyScreenRoot(
                navigationCallback = createSearchNavigationCallback(navController)
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
            // Navigate to notifications - for now go to inbox
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
                    navController.navigate(MainDestinations.ROOT) {
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
                    navController.navigate(MainDestinations.ROOT) {
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
                    navController.navigate(MainDestinations.ROOT) {
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
                            navController.navigate(MainDestinations.ROOT) {
                                popUpTo(OnboardingDestinations.ROOT) { inclusive = true }
                            }
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
                    navController.navigate(MainDestinations.ROOT) {
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
                    navController.navigate(MainDestinations.ROOT) {
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
                    navController.navigate(MainDestinations.ROOT) {
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

// Main App Scaffold with Bottom Navigation
@Composable
private fun MainAppScaffold(
    navController: NavHostController,
    currentRoute: String,
    content: @Composable () -> Unit
) {
    val selectedItem = when (currentRoute) {
        MainDestinations.HOME -> BottomNavItem.Home
        MainDestinations.SEARCH -> BottomNavItem.Search
        MainDestinations.SAVED -> BottomNavItem.Saved
        MainDestinations.INBOX -> BottomNavItem.Inbox
        MainDestinations.PROFILE -> BottomNavItem.Profile
        else -> BottomNavItem.Home
    }

    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        bottomBar = {
            BalkanEstateBottomNavigationBar(
                selectedItem = selectedItem,
                onItemSelected = { item ->
                    val route = when (item) {
                        BottomNavItem.Home -> MainDestinations.HOME
                        BottomNavItem.Search -> MainDestinations.SEARCH
                        BottomNavItem.Saved -> MainDestinations.SAVED
                        BottomNavItem.Inbox -> MainDestinations.INBOX
                        BottomNavItem.Profile -> MainDestinations.PROFILE
                    }
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            popUpTo(MainDestinations.ROOT) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            content()
        }
    }
}

// Placeholder Screen Contents
@Composable
private fun HomeScreenContent(
    onNavigateToSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = HomeIcon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = BalkanEstatePrimaryBlue
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Welcome to Balkan Estate",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Find your dream property in the Balkans",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onNavigateToSearch,
            colors = ButtonDefaults.buttonColors(containerColor = BalkanEstateOrange),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Start Searching", color = Color.White)
        }
    }
}

@Composable
private fun SavedPropertiesScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = SavedHomesIcon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = BalkanEstatePrimaryBlue
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Saved Properties",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your saved properties will appear here",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun InboxScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = InboxIcon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = BalkanEstatePrimaryBlue
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Inbox",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your messages will appear here",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ProfileScreenContent(
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(BalkanEstatePrimaryBlue),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = PersonIcon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "My Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Manage your account settings",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(
            onClick = onLogout,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = BalkanEstateRed),
            border = BorderStroke(1.dp, BalkanEstateRed),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = LogoutIcon,
                contentDescription = null,
                tint = BalkanEstateRed,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout", color = BalkanEstateRed)
        }
    }
}

@Composable
private fun SavedSearchesScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = AddSearchIcon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = BalkanEstatePrimaryBlue
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Saved Searches",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your saved searches will appear here",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TopAgentsScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = StarIcon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = BalkanEstateOrange
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Top Agents",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Discover top-rated real estate agents",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AgenciesScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = AgencyIcon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = BalkanEstatePrimaryBlue
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Agencies",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Browse real estate agencies",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun NewListingScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = EditPenIcon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = BalkanEstateOrange
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Create New Listing",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "List your property for sale or rent",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SubscriptionScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = StarIcon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = BalkanEstateOrange
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Subscription Plans",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BalkanEstatePrimaryBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Upgrade your account for premium features",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}